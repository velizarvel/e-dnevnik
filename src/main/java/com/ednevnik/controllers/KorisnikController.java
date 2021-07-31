package com.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;

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

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.entities.dto.KorisnikInfoDTO;
import com.ednevnik.entities.enums.EUlogaEntity;
import com.ednevnik.entities.factories.KorisnikFactory;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.mappers.KorisnikMapper;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.repositories.OdeljenjeRepository;
import com.ednevnik.services.KorisnikService;
import com.ednevnik.utils.BindingErrorMessage;
import com.ednevnik.utils.Encryption;
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
	@Autowired
	private OdeljenjeRepository odeljenjeRepository;

	// CREATE

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping()
	public ResponseEntity<?> kreirajKorisnika(@Valid @RequestBody KorisnikDTO korisnik, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(BindingErrorMessage.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		KorisnikEntity noviKorisnik = KorisnikFactory.createKorisnik(korisnik);

		if (noviKorisnik instanceof UcenikEntity) {
			if (korisnik.getOdeljenjeId() == null) {
				throw new EntityNotFoundException(
						GlobalExceptionHandler.getMessage("Odeljenje", korisnik.getOdeljenjeId()));
			}
			OdeljenjeEntity odeljenje = odeljenjeRepository.findById(korisnik.getOdeljenjeId())
					.orElseThrow(() -> new EntityNotFoundException(
							GlobalExceptionHandler.getMessage("Odeljenje", korisnik.getOdeljenjeId())));
			((UcenikEntity) noviKorisnik).setOdeljenje(odeljenje);
		}

		korisnikRepository.save(noviKorisnik);
		log.info(korisnikService.getKorisnik() + " je kreirao korisnika sa ulogom " + korisnik.getUloga() + ".");
		KorisnikInfoDTO korisnikInfo = KorisnikMapper.INSTANCE.korisnikEntityToKorisnikInfoDTO(noviKorisnik);
		return new ResponseEntity<>(korisnikInfo, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PostMapping("/lista")
	public ResponseEntity<?> kreirajKorisnikeIzListe(@Valid @RequestBody List<KorisnikDTO> korisnici,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(BindingErrorMessage.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		List<KorisnikEntity> noviKorisnici = new ArrayList<>();

		for (KorisnikDTO korisnik : korisnici) {
			KorisnikEntity noviKorisnik = KorisnikFactory.createKorisnik(korisnik);

			if (noviKorisnik instanceof UcenikEntity) {
				if (korisnik.getOdeljenjeId() == null) {
					throw new EntityNotFoundException(
							GlobalExceptionHandler.getMessage("Odeljenje", korisnik.getOdeljenjeId()));
				}
				OdeljenjeEntity odeljenje = odeljenjeRepository.findById(korisnik.getOdeljenjeId())
						.orElseThrow(() -> new EntityNotFoundException(
								GlobalExceptionHandler.getMessage("Odeljenje", korisnik.getOdeljenjeId())));
				((UcenikEntity) noviKorisnik).setOdeljenje(odeljenje);
			}

			noviKorisnici.add(noviKorisnik);
		}

		korisnikRepository.saveAll(noviKorisnici);

		log.info(korisnikService.getKorisnik() + " je kreirao " + noviKorisnici.size() + " novih korisnika.");
		return new ResponseEntity<>(noviKorisnici, HttpStatus.OK);
	}

	// READ

	@GetMapping("/mojProfil")
	public ResponseEntity<?> prikaziProfil() {
		return new ResponseEntity<>(korisnikService.getKorisnik(), HttpStatus.OK);
	}

	@GetMapping("/mojProfilMapper")
	public ResponseEntity<?> prikaziProfilMapper() {
		KorisnikEntity korisnik = korisnikService.getKorisnik();
		KorisnikInfoDTO korisnikInfo = KorisnikMapper.INSTANCE.korisnikEntityToKorisnikInfoDTO(korisnik);
		return new ResponseEntity<>(korisnikInfo, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping
	public ResponseEntity<?> prikaziSveKorisnike() {
		return new ResponseEntity<>(korisnikRepository.findAll(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping("/{id}")
	public ResponseEntity<?> pronadjiKorisnikaPoId(@PathVariable Integer id) {

		KorisnikEntity korisnik = korisnikRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Korisnik", id)));

		return new ResponseEntity<>(korisnik, HttpStatus.OK);
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

		korisnikRepository.save(korisnikDb);
		log.info(korisnikService.getKorisnik() + " je azurirao korisnika sa id: " + id + ".");

		return new ResponseEntity<>(korisnikDb, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping("sifra/{id}")
	public ResponseEntity<?> promeniSifru(@RequestParam String novaSifra, @RequestParam String staraSifra,
			@PathVariable Integer id) {

		KorisnikEntity korisnikDb = korisnikRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Korisnik", id)));

		if (!Encryption.validatePassword(staraSifra, korisnikDb.getSifra())) {
			return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(),
					"Nepravilno unesena trenutna sifra."),
					HttpStatus.FORBIDDEN);
		}

		korisnikDb.setSifra(Encryption.getPassEncoded(novaSifra));
		korisnikRepository.save(korisnikDb);
		log.info(korisnikService.getKorisnik() + " je azurirao korisnika sa id: " + id + ".");

		return new ResponseEntity<>(korisnikDb, HttpStatus.OK);
	}

	// DELETE

	@Secured("ROLE_ADMINISTRATOR")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> izbrisiKorisnika(@PathVariable Integer id) {

		KorisnikEntity korisnik = korisnikRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Korisnik", id)));

		if (korisnik.getUloga().equals(EUlogaEntity.ROLE_ADMINISTRATOR)) {
			List<KorisnikEntity> korisnici = korisnikRepository.findByUloga(EUlogaEntity.ROLE_ADMINISTRATOR);
			if (korisnici.size() == 1) {
				return new ResponseEntity<>(
						new RESTError(HttpStatus.FORBIDDEN.value(),
								"Korisnik " + korisnik.getKorisnickoIme()
										+ " se ne moze obrisati, jer je jedini administrator u bazi."),
						HttpStatus.FORBIDDEN);
			}
		}

		korisnikRepository.deleteById(korisnik.getId());

		log.info("Korisnik" + korisnikService.getKorisnik() + " je izbrsao korisnika sa id: " + id + ".");
		return new ResponseEntity<>(
				new RESTError(HttpStatus.OK.value(), "Uspesno je obrisan korisnik sa id: " + id + "."), HttpStatus.OK);
	}

}
