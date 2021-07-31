package com.ednevnik.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.entities.enums.EAktivnostEntity;
import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.mappers.UcenikMapper;
import com.ednevnik.repositories.OcenaRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.repositories.UcenikRepository;

@Service
public class UcenikServiceImpl implements UcenikService {

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private UcenikRepository ucenikRepository;

	@Autowired
	PredmetRepository predmetRepository;

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private SemestarService semestarService;

	@Override
	public Set<UcenikInfoDTO> prikaziUcenike() {

		Set<UcenikInfoDTO> ucenici = new HashSet<UcenikInfoDTO>();
		KorisnikEntity korisnik = korisnikService.getKorisnik();
		UcenikInfoDTO ucenikInfo;

		if (korisnik instanceof UcenikEntity) {
			ucenikInfo = ucenikEntityToUcenikInfoDTO((UcenikEntity) korisnik);
			ucenici.add(ucenikInfo);
		} else if (korisnik instanceof RoditeljEntity) {
			((RoditeljEntity) korisnik).getUcenici().forEach(u -> {
				UcenikInfoDTO info = ucenikEntityToUcenikInfoDTO(u);
				ucenici.add(info);
			});
		} else if (korisnik instanceof NastavnikEntity) {
			Set<OdeljenjeEntity> odeljenja = ((NastavnikEntity) korisnik).getOdeljenja();
			for (OdeljenjeEntity odeljenje : odeljenja) {
				for (UcenikEntity ucenik : odeljenje.getUcenici()) {
					ucenikInfo = ucenikEntityToUcenikInfoDTO(ucenik);
					ucenici.add(ucenikInfo);
				}
			}
		} else {
			List<UcenikEntity> sviUcenici = (List<UcenikEntity>) ucenikRepository.findAll();
			for (UcenikEntity ucenik : sviUcenici) {
				ucenikInfo = ucenikEntityToUcenikInfoDTO(ucenik);
				ucenici.add(ucenikInfo);
			}
		}

		return ucenici;
	}

	@Override
	public UcenikInfoDTO ucenikEntityToUcenikInfoDTO(UcenikEntity ucenik) {

		UcenikInfoDTO ucenikInfo = UcenikMapper.INSTANCE.ucenikEntityToUcenikInfoDTO(ucenik);

		List<PredmetEntity> predmeti = predmetRepository.findByOceneUcenik(ucenik);

		for (PredmetEntity predmet : predmeti) {
			List<String> ocene = new ArrayList<String>();
			for (OcenaEntity ocena : predmet.getOcene()) {
				ocene.add(ocena.toString());
			}
			ucenikInfo.getPodaciOOcenama().put(predmet.toString(), ocene);
		}

		return ucenikInfo;
	}

	public void izracunajProsek(UcenikEntity ucenik) {
		int brojPredmeta = ucenik.getOdeljenje().getPredmetNastavnikMapa().size();
		List<OcenaEntity> zakljuceneOcene = ocenaRepository.pronadjiZakljuceneOceneZaUcenika(ucenik,
				EAktivnostEntity.ZAKLJUCNA_OCENA, semestarService.getESemestar());

		if (brojPredmeta > zakljuceneOcene.size()) {
			return;
		}

		Double sum = zakljuceneOcene.stream().mapToDouble(x -> x.getVrednostOcene()).average().orElse(0.0);

		if (semestarService.getESemestar().equals(ESemestarEntity.PRVO_POLUGODISTE)) {
			ucenik.setProsekPolugodiste(sum);
			ucenik.setUspehPolugodiste(ucenik.getEUspehPolugodiste());
		} else if (semestarService.getESemestar().equals(ESemestarEntity.DRUGO_POLUGODISTE)) {
			ucenik.setProsekKrajGodine(sum);
			ucenik.setUspehKrajGodine(ucenik.getEUspehKrajGodine());
		}

		ucenikRepository.save(ucenik);
	}

}
