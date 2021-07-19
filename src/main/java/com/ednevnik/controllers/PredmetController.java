package com.ednevnik.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.security.Views;
import com.ednevnik.service.KorisnikService;
import com.ednevnik.utils.CustomValidation;
import com.ednevnik.utils.RESTError;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("predmeti")
@Log4j2
public class PredmetController {

	@Autowired
	private PredmetRepository predmetRepository;

	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private NastavnikRepository nastavnikRepository;

	// CREATE

	@PostMapping
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> kreirajPredmet(@Valid @RequestParam Integer fondCasova,
			@Valid @RequestParam String nazivPredmeta) {

		PredmetEntity predmet = new PredmetEntity();
		predmet.setFondCasova(fondCasova);
		predmet.setNazivPredmeta(nazivPredmeta);
		return new ResponseEntity<>(predmetRepository.save(predmet), HttpStatus.OK);
	}

	// READ

	@GetMapping
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> prikaziSvePredmete() {
		return new ResponseEntity<>(predmetRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> pronadjiPredmetPoId(@PathVariable Integer id) {
		PredmetEntity predmet = predmetRepository.findById(id).orElse(null);
		if (predmet == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Predmet sa id: " + id + " se ne nalazi u bazi"),
					HttpStatus.BAD_REQUEST);

		}
		return new ResponseEntity<>(predmet, HttpStatus.OK);
	}

	@PutMapping("/{predmetId}/{nastavnikId}")
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> dodajNastavnikaKojiPredaje(@PathVariable Integer predmetId,
			@PathVariable Integer nastavnikId) {

		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).orElse(null);
		if (nastavnik == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + nastavnikId + "."),
					HttpStatus.BAD_REQUEST);
		}
		PredmetEntity predmet = predmetRepository.findById(predmetId).orElse(null);
		if (predmet == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + predmetId + "."),
					HttpStatus.BAD_REQUEST);
		}

		predmet.getNastavnici().add(nastavnik);

		predmetRepository.save(predmet);

		return new ResponseEntity<>(predmet, HttpStatus.OK);
	}

	// READ

	@PutMapping("/{id}")
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> azurirajPredmet(@Valid @RequestParam Integer fondCasova,
			@Valid @RequestParam String nazivPredmeta, @PathVariable Integer id) {
		Optional<PredmetEntity> predmetDb = predmetRepository.findById(id);
		if (!predmetDb.isPresent()) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + id + "."),
					HttpStatus.BAD_REQUEST);
		}
		predmetDb.get().setFondCasova(CustomValidation.setIfNotNull(predmetDb.get().getFondCasova(), fondCasova));
		predmetDb.get()
				.setNazivPredmeta(CustomValidation.setIfNotNull(predmetDb.get().getNazivPredmeta(), nazivPredmeta));

		return new ResponseEntity<>(predmetRepository.save(predmetDb.get()), HttpStatus.OK);
	}

	// DELETE

	@DeleteMapping("/{id}")
	@Secured("ROLE_ADMINISTRATOR")
	@JsonView(Views.AdminView.class)
	public ResponseEntity<?> izbrisiPredmet(@PathVariable Integer id) {
		Optional<PredmetEntity> predmet = predmetRepository.findById(id);
		if (!predmet.isPresent()) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nije pronadjen predmet sa id: " + id + "."),
					HttpStatus.BAD_REQUEST);
		}
		predmetRepository.delete(predmet.get());
		String message = "Korisnik " + korisnikService.getKorisnik() + " je obrisao predmet sa id: " + id + ".";
		log.info(message);
		return new ResponseEntity<>(new RESTError(HttpStatus.OK.value(), message), HttpStatus.OK);
	}

}
