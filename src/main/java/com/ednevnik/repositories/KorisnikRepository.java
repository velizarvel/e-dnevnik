package com.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.KorisnikEntity;

public interface KorisnikRepository extends CrudRepository<KorisnikEntity, Integer>{

	public KorisnikEntity findByKorisnickoIme(String korisnickoIme);
	
}
