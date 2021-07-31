package com.ednevnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.SemestarEntity;
import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.repositories.SemestarRepository;
import com.ednevnik.services.KorisnikService;
import com.ednevnik.services.SemestarService;
import com.ednevnik.utils.RESTError;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/semestar")
@Log4j2
public class SemestarController {

	@Autowired
	private SemestarRepository semestarRepository;

	@Autowired
	private SemestarService semestarService;

	@Autowired
	private KorisnikService korisnikService;

	@GetMapping
	public ResponseEntity<?> prikaziSemestar() {
		return new ResponseEntity<>(semestarService.getSemestar(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping
	public ResponseEntity<?> createSemestar(@RequestParam ESemestarEntity eSemestar) {
		SemestarEntity semestarDb = semestarRepository.findAll().stream().findFirst().orElse(null);
		if (semestarDb != null) {
			return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), "Semestar se vec nalazi u bazi."),
					HttpStatus.FORBIDDEN);
		}
		SemestarEntity semestar = new SemestarEntity();
		semestar.setESemestar(eSemestar);
		semestarRepository.save(semestar);
		log.info(korisnikService.getKorisnik() + " je kreirao novi semestar.");
		return new ResponseEntity<>(semestar, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping
	public ResponseEntity<?> izmeniSemestar(@RequestParam ESemestarEntity eSemestar) {
		SemestarEntity semestar = semestarService.getSemestar();
		semestar.setESemestar(eSemestar);
		semestarRepository.save(semestar);
		log.info(korisnikService.getKorisnik() + " je izmenio semestar.");
		return new ResponseEntity<>(semestar, HttpStatus.OK);
	}
}
