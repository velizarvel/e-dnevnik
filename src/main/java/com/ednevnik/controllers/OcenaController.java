package com.ednevnik.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;
import com.ednevnik.entities.enums.EAktivnostEntity;
import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.repositories.OcenaRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.services.KorisnikService;
import com.ednevnik.services.OcenaService;
import com.ednevnik.utils.RESTError;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/ocene")
@Log4j2
public class OcenaController {

	@Autowired
	private OcenaRepository ocenaRepository;

	@Autowired
	private OcenaService ocenaService;

	@Autowired
	private UcenikRepository ucenikRepository;

	@Autowired
	private KorisnikService korisnikService;

	@PostMapping("/{predmetId}/{ucenikId}")
	@Secured({ "ROLE_NASTAVNIK", "ROLE_ADMINISTRATOR" })
	public ResponseEntity<?> oceniUcenika(@PathVariable Integer predmetId, @PathVariable Integer ucenikId,
			@RequestParam Integer vrednostOcene, @RequestParam EAktivnostEntity aktivnost,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate datum) throws Exception {

		if (aktivnost.equals(EAktivnostEntity.ZAKLJUCNA_OCENA)) {
			return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), "Ne mozete uneti zakljucnu ocenu."),
					HttpStatus.FORBIDDEN);
		}

		UcenikInfoDTO ucenikInfo = ocenaService.oceniUcenika(predmetId, ucenikId, vrednostOcene, aktivnost, datum);

		return new ResponseEntity<>(ucenikInfo, HttpStatus.OK);
	}

	@PostMapping("/{predmetId}/{ucenikId}/{semestar}")
	public ResponseEntity<?> pribaviProsecnuOcenuUcenikaZaPredmet(@PathVariable Integer predmetId,
			@PathVariable Integer ucenikId, @PathVariable ESemestarEntity semestar) {
		return new ResponseEntity<>(ocenaService.pribaviProsecnuOcenuUcenikaZaPredmet(ucenikId, predmetId, semestar),
				HttpStatus.OK);
	}

	@GetMapping("/{ucenikId}")
	public ResponseEntity<?> findAll(@PathVariable Integer ucenikId) {
		List<OcenaEntity> sveOceneUcenika = ocenaRepository.pronadjiUcenike(ucenikRepository.findById(ucenikId).get());
		return new ResponseEntity<>(sveOceneUcenika, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping("/{ocenaId}")
	public ResponseEntity<?> azurirajOcenu(@PathVariable Integer ocenaId, @RequestParam Integer vrednostOcene,
			@RequestParam EAktivnostEntity aktivnost, @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate datum) {
		if (aktivnost.equals(EAktivnostEntity.ZAKLJUCNA_OCENA)) {
			return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), "Ne mozete uneti zakljucnu ocenu."),
					HttpStatus.FORBIDDEN);
		}

		OcenaEntity ocena = ocenaService.updateOcena(ocenaId, vrednostOcene, aktivnost, datum);
		log.info("Korisnik " + korisnikService.getKorisnik() + " je azurirao ocenu sa id: " + ocenaId + ".");
		return new ResponseEntity<>(ocena, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@DeleteMapping("/{ocenaId}")
	public ResponseEntity<?> deleteOcena(@PathVariable Integer ocenaId) {
		OcenaEntity ocena = ocenaRepository.findById(ocenaId)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ocena", ocenaId)));

		ocenaRepository.delete(ocena);
		String message = "Korisnik " + korisnikService.getKorisnik() + " je obrisao ocenu sa id: " + ocenaId + ".";
		log.info(message);
		return new ResponseEntity<>(new RESTError(HttpStatus.OK.value(), message), HttpStatus.OK);
	}

	@PostMapping("/zakljucna/{predmetId}/{ucenikId}")
	@Secured({ "ROLE_NASTAVNIK", "ROLE_ADMINISTRATOR" })
	public ResponseEntity<?> zakljuciOcenu(@PathVariable Integer predmetId, @PathVariable Integer ucenikId,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate datum) throws Exception {

		UcenikInfoDTO ucenikInfo = ocenaService.zakljuciOcenu(ucenikId, predmetId, datum);

		return new ResponseEntity<>(ucenikInfo, HttpStatus.OK);
	}

}
