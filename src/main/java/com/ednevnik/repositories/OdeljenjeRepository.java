package com.ednevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.OdeljenjeEntity;

public interface OdeljenjeRepository extends CrudRepository<OdeljenjeEntity, Integer> {

	public List<OdeljenjeEntity> findByNastavniciId(Integer id);

}
