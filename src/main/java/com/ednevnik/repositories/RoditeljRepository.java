package com.ednevnik.repositories;

import com.ednevnik.entities.RoditeljEntity;

public interface RoditeljRepository extends KorisnikGenericRepository<RoditeljEntity> {

	public RoditeljEntity findByEmail(String email);

}
