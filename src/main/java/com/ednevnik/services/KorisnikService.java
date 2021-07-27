package com.ednevnik.services;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.dto.KorisnikDTO;

public interface KorisnikService {

	public KorisnikEntity getKorisnik();
	
	public KorisnikEntity updateKorisnik(KorisnikDTO korisnikDTO, Integer id);
	
}
