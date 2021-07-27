package com.ednevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.UcenikEntity;

public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {

	@Query("SELECT o FROM OcenaEntity o WHERE ucenik = :ucenik GROUP BY predmet")
	public List<OcenaEntity> pronadjiUcenike(UcenikEntity ucenik);
	
}
