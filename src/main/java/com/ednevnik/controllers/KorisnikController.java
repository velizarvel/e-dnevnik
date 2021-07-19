package com.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.entities.factories.KorisnikFactory;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.service.KorisnikService;
import com.ednevnik.utils.BindingErrorMessage;
import com.ednevnik.utils.RESTError;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/korisnici")
public class KorisnikController {

	@Autowired
	private KorisnikRepository korisnikRepository;
	@Autowired
	private KorisnikService korisnikService;

	// CREATE

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping()
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
		log.info(korisnikService.getKorisnik() + " je kreirao korisnika sa ulogom " + korisnik.getUloga() + ".");
		return new ResponseEntity<>(noviKorisnik, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping("/lista")
	public ResponseEntity<?> kreirajKorisnike(@Valid @RequestBody List<KorisnikDTO> korisnici, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(BindingErrorMessage.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		List<KorisnikEntity> noviKorisnici = new ArrayList<>();

		for (KorisnikDTO korisnik : korisnici) {
			KorisnikEntity noviKorisnik = KorisnikFactory.createKorisnik(korisnik);
			if (noviKorisnik instanceof RoditeljEntity) {
				if (korisnik.getEmail().isEmpty()) {
					return new ResponseEntity<>("Email je obavezno polje", HttpStatus.BAD_REQUEST);
				}
			}

			korisnikRepository.save(noviKorisnik);
			noviKorisnici.add(noviKorisnik);
		}

		log.info(korisnikService.getKorisnik() + " je kreirao " + noviKorisnici.size() + " nova korisnika.");
		return new ResponseEntity<>(noviKorisnici, HttpStatus.OK);
	}

	// READ

	@GetMapping("/mojProfil")
	public ResponseEntity<?> prikaziProfil() {
		return new ResponseEntity<>(korisnikService.getKorisnik(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping
	public ResponseEntity<?> prikaziSveKorisnike() {
		return new ResponseEntity<>(korisnikRepository.findAll(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping("/{id}")
	public ResponseEntity<?> pronadjiKorisnikaPoId(@PathVariable Integer id) {
		Optional<KorisnikEntity> korisnik = korisnikRepository.findById(id);
		if (korisnik.isPresent()) {
			return new ResponseEntity<>(korisnikRepository.findAll(), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new RESTError(HttpStatus.BAD_REQUEST.value(), "Korisnik sa id: " + id + " se ne nalazi u bazi"),
				HttpStatus.BAD_REQUEST);

	}

	// UPDATE

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping("/{id}")
	public ResponseEntity<?> azurirajKorisnika(@Valid @RequestBody KorisnikDTO korisnikDTO, @PathVariable Integer id,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(BindingErrorMessage.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		KorisnikEntity korisnikDb = korisnikService.updateKorisnik(korisnikDTO, id);

		if (korisnikDb == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Korisnik sa id: " + id + " se ne nalazi u bazi"),
					HttpStatus.BAD_REQUEST);
		}

		korisnikRepository.save(korisnikDb);
		log.info(korisnikService.getKorisnik() + " je azurirao korisnika sa id: " + id + ".");

		return new ResponseEntity<>(korisnikDb, HttpStatus.OK);
	}

	// DELETE

	@Secured("ROLE_ADMINISTRATOR")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> izbrisiKorisnika(@PathVariable Integer id) {
		Optional<KorisnikEntity> korisnik = korisnikRepository.findById(id);
		if (!korisnik.isPresent()) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen korisnik sa id: " + id + "."),
					HttpStatus.BAD_REQUEST);
		}
		korisnikRepository.deleteById(korisnik.get().getId());
		return new ResponseEntity<>(
				new RESTError(HttpStatus.OK.value(), "Uspesno je obrisan korisnik sa id: " + id + "."), HttpStatus.OK);
	}

}
