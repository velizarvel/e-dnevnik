package com.ednevnik.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.entities.factories.KorisnikFactory;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.RoditeljRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.utils.BindingErrorMessage;

@RestController
public class KorisnikController {

	@Autowired
	private KorisnikRepository korisnikRepository;
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private UcenikRepository ucenikRepository;

	@GetMapping("/korisnici")
	public String index() {
		return "Dobrodosli";
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping("/korisnici")
	public ResponseEntity<?> kreirajKorisnika(@Valid @RequestBody KorisnikDTO korisnik, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(BindingErrorMessage.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		KorisnikEntity noviKorisnik = KorisnikFactory.createKorisnik(korisnik);

		if (noviKorisnik instanceof RoditeljEntity) {
			if (korisnik.getEmail().isEmpty()) {
				return new ResponseEntity<>("Email je obavezno polje", HttpStatus.BAD_REQUEST);
			}
		}

		korisnikRepository.save(noviKorisnik);

		return new ResponseEntity<>(noviKorisnik, HttpStatus.OK);
	}

	@GetMapping("nastavnici")
	public ResponseEntity<?> prikaziSveNastavnike() {
		return new ResponseEntity<>(nastavnikRepository.findAll(), HttpStatus.OK);
	}

}
