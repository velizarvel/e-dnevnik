package com.ednevnik.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.repositories.UcenikRepository;

@Service
public class UcenikServiceImpl implements UcenikService{

	@Autowired
	KorisnikService korisnikService;

	@Autowired
	UcenikRepository ucenikRepository;
	
	@Override
	public List<UcenikEntity> prikaziUcenike() {
		
		List<UcenikEntity> ucenici = new ArrayList<UcenikEntity>();
		KorisnikEntity korisnik = korisnikService.getKorisnik();
		if(korisnik instanceof UcenikEntity) {
			ucenici.add((UcenikEntity) korisnik);
		} else if(korisnik instanceof RoditeljEntity) {
			ucenici =((RoditeljEntity)korisnik).getUcenici();
		} else if(korisnik instanceof NastavnikEntity) {
//			List<OdeljenjeEntity> odeljenja = ((NastavnikEntity)korisnik).getOdeljenja();
//			for (OdeljenjeEntity odeljenje : odeljenja) {
//				for (UcenikEntity ucenik : odeljenje.getUcenici()) {
//					ucenici.add(ucenik);
//				}
//			}
		}else {
			ucenici = (List<UcenikEntity>) ucenikRepository.findAll();
		}
		
		return ucenici;
	}

}
