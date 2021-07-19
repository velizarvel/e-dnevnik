package com.ednevnik.repositories;

import java.util.List;

import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.UcenikEntity;

public interface UcenikRepository extends KorisnikGenericRepository<UcenikEntity> {

	public List<UcenikEntity> findByOdeljenje(OdeljenjeEntity odeljenje);

}
