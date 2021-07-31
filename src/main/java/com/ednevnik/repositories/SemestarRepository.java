package com.ednevnik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ednevnik.entities.SemestarEntity;

public interface SemestarRepository extends JpaRepository<SemestarEntity, Integer> {

}
