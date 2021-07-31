package com.ednevnik.services;

import java.time.LocalDate;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.entities.enums.EAktivnostEntity;
import com.ednevnik.entities.enums.ESemestarEntity;

public interface OcenaService {

	public UcenikInfoDTO oceniUcenika(Integer predmetId, Integer ucenikId, Integer vrednostOcene,
			EAktivnostEntity aktivnost, LocalDate datum) throws Exception;

	public Double pribaviProsecnuOcenuUcenikaZaPredmet(Integer ucenikId, Integer predmetId, ESemestarEntity semestar);

	public OcenaEntity updateOcena(Integer id, Integer vrednostOcene, EAktivnostEntity aktivnost, LocalDate datum);

	public UcenikInfoDTO zakljuciOcenu(Integer ucenikId, Integer predmetId, LocalDate datum) throws Exception;
}
