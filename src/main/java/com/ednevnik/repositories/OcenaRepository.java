package com.ednevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.enums.EAktivnostEntity;
import com.ednevnik.entities.enums.ESemestarEntity;

public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {

	@Query("SELECT o FROM OcenaEntity o WHERE ucenik = :ucenik GROUP BY predmet")
	public List<OcenaEntity> pronadjiUcenike(UcenikEntity ucenik);

	@Query("SELECT o FROM OcenaEntity o WHERE ucenik = :ucenik AND predmet = :predmet AND semestar = :semestar")
	public List<OcenaEntity> pronadjiOceneUcenikaZaPredmet(UcenikEntity ucenik, PredmetEntity predmet,
			ESemestarEntity semestar);

	@Query("SELECT o FROM OcenaEntity o WHERE ucenik = :ucenik AND aktivnost = :aktivnost AND semestar = :semestar")
	public List<OcenaEntity> pronadjiZakljuceneOceneZaUcenika(UcenikEntity ucenik, EAktivnostEntity aktivnost,
			ESemestarEntity semestar);
}
