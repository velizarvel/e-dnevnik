package com.ednevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.UcenikEntity;

public interface PredmetRepository extends CrudRepository<PredmetEntity, Integer> {

	public List<PredmetEntity> findByOceneUcenik(UcenikEntity ucenik);

}
