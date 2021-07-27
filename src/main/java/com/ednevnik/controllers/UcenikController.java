package com.ednevnik.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.repositories.OdeljenjeRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.services.UcenikService;
import com.ednevnik.utils.RESTError;

@RestController
@RequestMapping("ucenici")
public class UcenikController {

	@Autowired
	UcenikRepository ucenikRepository;

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Autowired
	UcenikService ucenikService;

	@GetMapping()
	@Secured({ "ROLE_ADMINISTRATOR", "ROLE_NASTAVNIK", "ROLE_RODITELJ", "ROLE_UCENIK" })
	public ResponseEntity<?> prikaziUcenike() {

		Set<UcenikInfoDTO> uceniciInfoDTO = ucenikService.prikaziUcenike();

		return new ResponseEntity<>(uceniciInfoDTO, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping("/{id}/{odeljenjeId}")
	public ResponseEntity<?> upsertOdeljenje(@PathVariable Integer id, @PathVariable Integer odeljenjeId) {
		UcenikEntity ucenik = ucenikRepository.findById(id).orElse(null);
		if (ucenik == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Ucenik sa id: " + id + " se ne nalazi u bazi"),
					HttpStatus.BAD_REQUEST);
		}
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(odeljenjeId).orElse(null);
		if (odeljenje == null) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(),
					"Odeljenje sa id: " + odeljenjeId + " se ne nalazi u bazi"), HttpStatus.BAD_REQUEST);
		}
		ucenik.setOdeljenje(odeljenje);
		ucenikRepository.save(ucenik);

		return new ResponseEntity<>(ucenik, HttpStatus.OK);
	}

}
