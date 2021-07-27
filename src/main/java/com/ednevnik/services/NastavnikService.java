package com.ednevnik.services;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.dto.NastavnikInfoDTO;

public interface NastavnikService {

	public void addPredmetiIOdeljenja(NastavnikInfoDTO nastavnikInfoDTO, NastavnikEntity nastavnikEntity);

}
