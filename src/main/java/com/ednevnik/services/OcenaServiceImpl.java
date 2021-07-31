package com.ednevnik.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.entities.enums.EAktivnostEntity;
import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.entities.enums.EUlogaEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.exceptions.GradeException;
import com.ednevnik.exceptions.TeacherAccessDeniedException;
import com.ednevnik.repositories.OcenaRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.utils.CustomValidation;

@Service
public class OcenaServiceImpl implements OcenaService {

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private PredmetRepository predmetRepository;

	@Autowired
	private UcenikRepository ucenikRepository;

	@Autowired
	private UcenikService ucenikService;

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private OdeljenjeService odeljenjeService;

	@Autowired
	EmailService emailService;

	@Autowired
	SemestarService semestarService;

	@Override
	public UcenikInfoDTO oceniUcenika(Integer predmetId, Integer ucenikId, Integer vrednostOcene,
			EAktivnostEntity aktivnost, LocalDate datum) throws Exception {

		KorisnikEntity korisnik = korisnikService.getKorisnik();

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		UcenikEntity ucenik = ucenikRepository.findById(ucenikId)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ucenik", ucenikId)));
		
		if(!ucenik.getOdeljenje().getPredmetNastavnikMapa().containsKey(predmet)) {
			throw new GradeException("Ucenik " + ucenik.getIme() + " " + ucenik.getPrezime() + " ne pohadja predmet " + predmet.getNazivPredmeta() + ".");
		}

		proveriPristup(korisnik, predmet, ucenik);
		
		ESemestarEntity semestar = semestarService.getESemestar();
		
		List<OcenaEntity> ocenePredmeta = ocenaRepository.pronadjiOceneUcenikaZaPredmet(ucenik, predmet, semestar);
		if(ocenePredmeta.stream().filter(o -> o.getAktivnost().equals(EAktivnostEntity.ZAKLJUCNA_OCENA)).count()>0) {
			throw new GradeException("Ne mozete uneti novu ocenu jer je iz predmeta " + predmet.getNazivPredmeta() + " vec zakljucena ocena.");
		}

		OcenaEntity ocena = new OcenaEntity();
		ocena.setSemestar(semestarService.getESemestar());
		ocena.setPredmet(predmet);
		ocena.setUcenik(ucenik);
		ocena.setDatum(datum);
		ocena.setVrednostOcene(vrednostOcene);
		ocena.setAktivnost(aktivnost);
		ocenaRepository.save(ocena);

		emailService.sendTemplateMessage(ocena);

		if (ocena.getAktivnost().equals(EAktivnostEntity.ZAKLJUCNA_OCENA)) {
			ucenikService.izracunajProsek(ucenik);
		}

		UcenikInfoDTO ucenikInfo = ucenikService.ucenikEntityToUcenikInfoDTO(ucenik);

		return ucenikInfo;

	}

	@Override
	public Double pribaviProsecnuOcenuUcenikaZaPredmet(Integer ucenikId, Integer predmetId, ESemestarEntity semestar) {

		UcenikEntity ucenik = ucenikRepository.findById(ucenikId)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ucenik", ucenikId)));

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		List<OcenaEntity> ocene = ocenaRepository.pronadjiOceneUcenikaZaPredmet(ucenik, predmet, semestar);

		if (ocene.size() == 0) {
			throw new GradeException("Ucenik " + ucenik.getIme() + " " + ucenik.getPrezime()
					+ " nije dobio nijednu ocenu iz predmeta " + predmet.getNazivPredmeta() + ".");
		}

		Double sum = 0.0;

		for (OcenaEntity ocena : ocene) {
			if (ocena.getAktivnost().equals(EAktivnostEntity.ZAKLJUCNA_OCENA)) {
				throw new GradeException("Uceniku " + ucenik.getIme() + " " + ucenik.getPrezime() + " iz predmeta "
						+ predmet.getNazivPredmeta() + " zakljucena ocena " + ocena.getVrednostOcene());
			}
			sum += ocena.getVrednostOcene();
		}

		return sum / ocene.size();
	}

	@Override
	public OcenaEntity updateOcena(Integer id, Integer vrednostOcene, EAktivnostEntity aktivnost, LocalDate datum) {
		OcenaEntity ocena = ocenaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ocena", id)));

		ocena.setVrednostOcene(CustomValidation.setIfNotNull(ocena.getVrednostOcene(), vrednostOcene));
		ocena.setAktivnost(CustomValidation.setIfNotNull(ocena.getAktivnost(), aktivnost));
		ocena.setDatum(CustomValidation.setIfNotNull(ocena.getDatum(), datum));

		ocenaRepository.save(ocena);
		return ocena;
	}

	@Override
	public UcenikInfoDTO zakljuciOcenu(Integer ucenikId, Integer predmetId, LocalDate datum) throws Exception {

		Double prosecnaOcena = pribaviProsecnuOcenuUcenikaZaPredmet(ucenikId, predmetId,
				semestarService.getESemestar());

		Integer vrednostOcene = (int) Math.round(prosecnaOcena);

		return oceniUcenika(predmetId, ucenikId, vrednostOcene, EAktivnostEntity.ZAKLJUCNA_OCENA, datum);

	}

	private void proveriPristup(KorisnikEntity korisnik, PredmetEntity predmet, UcenikEntity ucenik) {
		boolean isAdmin = korisnik.getUloga().equals(EUlogaEntity.ROLE_ADMINISTRATOR);
		boolean isNastavnik = korisnik.getUloga().equals(EUlogaEntity.ROLE_NASTAVNIK);

		if (!isAdmin && !isNastavnik) {
			throw new AccessDeniedException("Nemate odgovarajucu ulogu za unos ocene");
		}

		if (isNastavnik) {
			boolean predaje = odeljenjeService.daLiNastavnikPredajePredmetOdeljenju(ucenik.getOdeljenje(),
					((NastavnikEntity) korisnik), predmet);
			if (!predaje) {
				throw new TeacherAccessDeniedException("Nije moguce izvrsiti upis ocene. Nastavnik " + korisnik.getIme()
						+ " " + korisnik.getPrezime() + " ne predaje predmet: " + predmet.getNazivPredmeta()
						+ " uceniku " + ucenik.getIme() + " " + ucenik.getPrezime() + " koji se nalazi u odeljenju "
						+ ucenik.getOdeljenje().getRazredIOdeljenje());
			}
		}
	}

}
