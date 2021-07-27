package com.ednevnik.services;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.dto.NastavnikInfoDTO;
import com.ednevnik.repositories.OdeljenjeRepository;

@Service
public class NastavnikServiceImpl implements NastavnikService {

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Override
	public void addPredmetiIOdeljenja(NastavnikInfoDTO nastavnikInfoDTO, NastavnikEntity nastavnikEntity) {

		List<OdeljenjeEntity> predajeOdeljenjima = odeljenjeRepository.findByNastavniciId(nastavnikEntity.getId());

		for (OdeljenjeEntity predajeOdeljenju : predajeOdeljenjima) {
			for (Entry<PredmetEntity, NastavnikEntity> entry : predajeOdeljenju.getPredmetNastavnikMapa().entrySet()) {
				if (entry.getValue().equals(nastavnikEntity)) {
					nastavnikInfoDTO.getPredajePredmeteOdeljenjima()
							.add(predajeOdeljenju.getRazredIOdeljenje() + " - " + entry.getKey().getNazivPredmeta());
				}
			}
		}
	}

}
