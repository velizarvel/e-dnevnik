package com.ednevnik.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.OdeljenjeRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.services.KorisnikService;
import com.ednevnik.services.OdeljenjeService;
import com.ednevnik.utils.RESTError;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping(path = "/odeljenje")
public class OdeljenjeController {

	@Autowired
	private OdeljenjeRepository odeljenjeRepository;

	@Autowired
	private OdeljenjeService odeljenjeService;

	@Autowired
	private PredmetRepository predmetRepository;

	@Autowired
	private NastavnikRepository nastavnikRepository;

	@Autowired
	private KorisnikService korisnikService;

	// CREATE

	@PostMapping
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> kreirajOdeljenje(@Valid @RequestParam Integer razred,
			@Valid @RequestParam Integer brojOdeljenja, @Valid @RequestParam Integer generacija) {
		OdeljenjeEntity odeljenje = new OdeljenjeEntity();
		odeljenje.setGeneracija(generacija);
		odeljenje.setOdeljenje(brojOdeljenja);
		odeljenje.setRazred(razred);

		odeljenjeRepository.save(odeljenje);

		log.info("Kreirano je odeljenje " + odeljenje.getRazredIOdeljenje());
		return new ResponseEntity<>(odeljenje, HttpStatus.CREATED);
	}

	// READ

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping
	public ResponseEntity<?> prikaziSvaOdeljenja() {
		return new ResponseEntity<>(odeljenjeRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> pronadjiPredmetPoId(@PathVariable Integer id) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", id)));

		return new ResponseEntity<>(odeljenje, HttpStatus.OK);
	}

	@GetMapping("/{id}/predmetiINastavnici")
	public ResponseEntity<?> prikaziSvePredmeteINastavnike(@PathVariable Integer id) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", id)));

		return new ResponseEntity<>(odeljenje.getPredmetNastavnikMapa(), HttpStatus.OK);
	}

	@GetMapping("/{odeljenjeId}/{predmetId}")
	public ResponseEntity<?> pronadjiPredmetPoId(@PathVariable Integer odeljenjeId, @PathVariable Integer predmetId) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(odeljenjeId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", odeljenjeId)));

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		return new ResponseEntity<>(odeljenjeService.getNastavnikKojiPredajePredmetOdeljenju(odeljenje, predmet),
				HttpStatus.OK);
	}

	// UPDATE

	@PutMapping("/{id}")
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> azurirajOdeljenje(@Valid @RequestParam Integer razred,
			@Valid @RequestParam Integer brojOdeljenja, @Valid @RequestParam Integer generacija,
			@PathVariable Integer id) {

		OdeljenjeEntity odeljenje = odeljenjeService.updateOdeljenje(razred, brojOdeljenja, generacija, id);

		log.info("Azurirano je odeljenje " + odeljenje.getRazredIOdeljenje());
		return new ResponseEntity<>(odeljenje, HttpStatus.CREATED);
	}

	@PutMapping("/{odeljenjeId}/{predmetId}/{nastavnikId}")
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> dodajPredmetINastavnika(@PathVariable Integer odeljenjeId, @PathVariable Integer predmetId,
			@PathVariable Integer nastavnikId) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(odeljenjeId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", odeljenjeId)));

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Nastavnik", nastavnikId)));

		Map<PredmetEntity, NastavnikEntity> predmetNastavnikMapa = odeljenjeService.dodajPredmetINastavnika(odeljenje,
				nastavnik, predmet);

		if (predmetNastavnikMapa == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Nastavnik " + nastavnik.getIme() + " "
							+ nastavnik.getPrezime() + " ne predaje predmet " + predmet.getNazivPredmeta() + "."),
					HttpStatus.BAD_REQUEST);
		}

		log.info("Dodat je nastavnik " + nastavnik.getKorisnickoIme() + " " + nastavnik.getPrezime()
				+ " da predaje predmet " + predmet.getNazivPredmeta() + " u odeljenju "
				+ odeljenje.getRazredIOdeljenje());
		return new ResponseEntity<>(predmetNastavnikMapa, HttpStatus.OK);
	}

	// DELETE

	@Secured("ROLE_ADMINISTRATOR")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> izbrisiOdeljenje(@PathVariable Integer id) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", id)));

		if (odeljenje.getUcenici().size() > 0) {
			return new ResponseEntity<>(new RESTError(HttpStatus.OK.value(), "Ne moze se obrisati odeljenje : " + id
					+ " jer je povezano sa " + odeljenje.getUcenici().size() + " ucenika."), HttpStatus.OK);
		}

		odeljenjeRepository.delete(odeljenje);

		log.info("Korisnik " + korisnikService.getKorisnik().getKorisnickoIme() + " je obrisao odeljenje sa id: " + id
				+ ".");

		return new ResponseEntity<>(
				new RESTError(HttpStatus.OK.value(), "Uspesno je obrisano odeljenje sa id: " + id + "."),
				HttpStatus.OK);
	}

}
