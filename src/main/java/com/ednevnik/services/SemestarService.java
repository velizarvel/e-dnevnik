package com.ednevnik.services;

import com.ednevnik.entities.SemestarEntity;
import com.ednevnik.entities.enums.ESemestarEntity;

public interface SemestarService {
	
	public SemestarEntity getSemestar();
	
	public ESemestarEntity getESemestar();
	
}
