package com.ednevnik.services;

import java.util.Set;

import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;

public interface UcenikService {

	public Set<UcenikInfoDTO> prikaziUcenike();

	public UcenikInfoDTO ucenikEntityToUcenikInfoDTO(UcenikEntity ucenik);

	public void izracunajProsek(UcenikEntity ucenik);

}
