package com.ednevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.enums.EUlogaEntity;

public interface KorisnikGenericRepository<T extends KorisnikEntity> extends CrudRepository<T, Integer> {

	public T findByKorisnickoIme(String korisnickoIme);
	
	public List<T> findByImeAndPrezime(String ime, String prezime);
	
	public List<T> findByUloga(EUlogaEntity uloga);
	
	public T deleteByKorisnickoIme(String korisnickoIme);
	
}
