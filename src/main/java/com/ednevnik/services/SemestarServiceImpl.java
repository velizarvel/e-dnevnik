package com.ednevnik.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.SemestarEntity;
import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.repositories.SemestarRepository;

@Service
public class SemestarServiceImpl implements SemestarService {

	@Autowired
	private SemestarRepository semestarRepository;

	@Override
	public SemestarEntity getSemestar() {
		return semestarRepository.findAll().stream().findFirst()
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Semestar", 0)));
	}

	@Override
	public ESemestarEntity getESemestar() {
		return getSemestar().getESemestar();
	}

}
