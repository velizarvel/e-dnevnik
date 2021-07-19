package com.ednevnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.EUlogaEntity;
import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.facade.AuthenticationFacade;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.utils.CustomValidation;

@Service
class KorisnikServiceImpl implements KorisnikService {

	@Autowired
	KorisnikRepository korisnikRepository;

	@Autowired
	AuthenticationFacade authentication;

	@Override
	public KorisnikEntity getKorisnik() {
		String korisnickoIme = authentication.getAuthentication().getName();
		return korisnikRepository.findByKorisnickoIme(korisnickoIme);

	}

	@Override
	public KorisnikEntity updateKorisnik(KorisnikDTO korisnikDTO, Integer id) {

		KorisnikEntity korisnik = korisnikRepository.findById(id).orElse(null);

		if (korisnik == null) {
			return null;
		}

		if (korisnik instanceof RoditeljEntity) {
			((RoditeljEntity) korisnik).setEmail(
					CustomValidation.setIfNotNull(((RoditeljEntity) korisnik).getEmail(), korisnikDTO.getEmail()));
		}

		korisnik.setIme(CustomValidation.setIfNotNull(korisnik.getIme(), korisnikDTO.getIme()));
		korisnik.setPrezime(CustomValidation.setIfNotNull(korisnik.getPrezime(), korisnikDTO.getPrezime()));

		return korisnik;
	}

}
