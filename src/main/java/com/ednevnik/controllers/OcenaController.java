package com.ednevnik.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.repositories.OcenaRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.services.OcenaService;

@RestController
@RequestMapping("/ocene")
public class OcenaController {

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private OcenaService ocenaService;

	@Autowired
	private UcenikRepository ucenikRepository;

	@PostMapping("/{predmetId}/{ucenikId}")
	@Secured({ "ROLE_NASTAVNIK", "ROLE_ADMINISTRATOR" })
	public ResponseEntity<?> oceniUcenika(@PathVariable Integer predmetId, @PathVariable Integer ucenikId,
			@RequestParam Integer vrednostOcene, @RequestParam String aktivnost,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate datum) throws Exception {

		UcenikInfoDTO ucenikInfo = ocenaService.oceniUcenika(predmetId, ucenikId, vrednostOcene, aktivnost, datum);

		return new ResponseEntity<>(ucenikInfo, HttpStatus.OK);
	}

	@GetMapping("/{ucenikId}")
	public ResponseEntity<?> findAll(@PathVariable Integer ucenikId) {
		List<OcenaEntity> sveOceneUcenika = ocenaRepository.pronadjiUcenike(ucenikRepository.findById(ucenikId).get());
		return new ResponseEntity<>(sveOceneUcenika, HttpStatus.OK);
	}

}
