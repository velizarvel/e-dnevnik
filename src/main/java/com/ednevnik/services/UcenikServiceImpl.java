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
import com.ednevnik.mappers.UcenikMapper;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.repositories.UcenikRepository;

@Service
public class UcenikServiceImpl implements UcenikService {

	@Autowired
	KorisnikService korisnikService;

	@Autowired
	UcenikRepository ucenikRepository;

	@Autowired
	PredmetRepository predmetRepository;

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

}
