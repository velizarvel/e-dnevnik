package com.ednevnik.service;

import org.springframework.stereotype.Service;

import com.ednevnik.entities.OdeljenjeEntity;

public interface OdeljenjeService {

	public OdeljenjeEntity updateOdeljenje(Integer razred, Integer brojOdeljenja, Integer generacija, Integer id);

}
