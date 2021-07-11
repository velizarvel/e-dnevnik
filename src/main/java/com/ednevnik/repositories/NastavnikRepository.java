package com.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ednevnik.entities.NastavnikEntity;

public interface NastavnikRepository extends CrudRepository<NastavnikEntity, Integer>{
	
}
