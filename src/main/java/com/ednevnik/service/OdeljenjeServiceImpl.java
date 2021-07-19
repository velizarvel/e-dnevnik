package com.ednevnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.repositories.OdeljenjeRepository;
import com.ednevnik.utils.CustomValidation;

@Service
public class OdeljenjeServiceImpl implements OdeljenjeService {

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Override
	public OdeljenjeEntity updateOdeljenje(Integer razred, Integer brojOdeljenja, Integer generacija, Integer id) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id).orElse(null);

		if (odeljenje == null) {
			return null;
		}

		odeljenje.setGeneracija(CustomValidation.setIfNotNull(odeljenje.getGeneracija(), generacija));
		odeljenje.setOdeljenje(CustomValidation.setIfNotNull(odeljenje.getOdeljenje(), brojOdeljenja));
		odeljenje.setRazred(CustomValidation.setIfNotNull(odeljenje.getRazred(), razred));

		odeljenjeRepository.save(odeljenje);

		return odeljenje;
	}

}
