package com.ednevnik.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.EUlogaEntity;
import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.exceptions.TeacherAccessDeniedException;
import com.ednevnik.repositories.OcenaRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.repositories.UcenikRepository;

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

	@Override
	public UcenikInfoDTO oceniUcenika(Integer predmetId, Integer ucenikId, Integer vrednostOcene, String aktivnost,
			LocalDate datum) throws Exception {

		KorisnikEntity korisnik = korisnikService.getKorisnik();

		boolean isAdmin = korisnik.getUloga().equals(EUlogaEntity.ROLE_ADMINISTRATOR);
		boolean isNastavnik = korisnik.getUloga().equals(EUlogaEntity.ROLE_NASTAVNIK);

		if (!isAdmin && !isNastavnik) {
			throw new AccessDeniedException("Nemate odgovarajucu ulogu za unos ocene");
		}

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		UcenikEntity ucenik = ucenikRepository.findById(ucenikId)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ucenik", ucenikId)));

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

		OcenaEntity ocena = new OcenaEntity();
		ocena.setPredmet(predmet);
		ocena.setUcenik(ucenik);
		ocena.setDatum(datum);
		ocena.setVrednostOcene(vrednostOcene);
		ocena.setAktivnost(aktivnost);
		ocenaRepository.save(ocena);
		
		emailService.sendTemplateMessage(ocena);

		UcenikInfoDTO ucenikInfo = ucenikService.ucenikEntityToUcenikInfoDTO(ucenik);
		
		return ucenikInfo;

	}

}
