package com.ednevnik.services;

import java.time.LocalDate;

import com.ednevnik.entities.dto.UcenikInfoDTO;

public interface OcenaService {

	public UcenikInfoDTO oceniUcenika(Integer predmetId, Integer ucenikId, Integer vrednostOcene, String aktivnost, LocalDate datum) throws Exception;

}
