package com.ednevnik.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.service.KorisnikService;
import com.ednevnik.utils.RESTError;

@RestController
@RequestMapping("/nastavnici")
public class NastavnikController {

	@Autowired
	NastavnikRepository nastavnikRepository;

	@Autowired
	PredmetRepository predmetRepository;

	@Autowired
	KorisnikService korisnikService;

	@PutMapping("/{nastavnikId}/{predmetId}")
	public ResponseEntity<?> dodajPredmetKojiPredaje(@PathVariable Integer nastavnikId,
			@PathVariable Integer predmetId) {

		Optional<NastavnikEntity> nastavnik = nastavnikRepository.findById(nastavnikId);
		if (!nastavnik.isPresent()) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + nastavnikId + "."),
					HttpStatus.BAD_REQUEST);
		}
		Optional<PredmetEntity> predmet = predmetRepository.findById(predmetId);
		if (!predmet.isPresent()) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + predmetId + "."),
					HttpStatus.BAD_REQUEST);
		}

//		nastavnik.get().getPredmeti().add(predmet.get());
		predmet.get().getNastavnici().add(nastavnik.get());

		predmetRepository.save(predmet.get());

		return new ResponseEntity<>(nastavnik, HttpStatus.OK);
	}

}
