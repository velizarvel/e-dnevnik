package com.ednevnik.controllers;

import java.util.HashSet;
import java.util.List;
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

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.dto.NastavnikInfoDTO;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.mappers.NastavnikMapper;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.PredmetRepository;
import com.ednevnik.services.KorisnikService;
import com.ednevnik.services.NastavnikService;

@RestController
@RequestMapping("/nastavnici")
public class NastavnikController {

	@Autowired
	NastavnikRepository nastavnikRepository;

	@Autowired
	NastavnikService nastavnikService;

	@Autowired
	PredmetRepository predmetRepository;

	@Autowired
	KorisnikService korisnikService;

	@GetMapping()
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> prikaziNastavnike() {

		Set<NastavnikInfoDTO> nastavniciDTO = new HashSet<>();

		List<NastavnikEntity> nastavnici = (List<NastavnikEntity>) nastavnikRepository.findAll();

		nastavnici.forEach(n -> {
			NastavnikInfoDTO nastavnikInfo = NastavnikMapper.INSTANCE.nastavnikEntityToNastavnikInfoDTO(n);
			nastavnikService.addPredmetiIOdeljenja(nastavnikInfo, n);
			nastavniciDTO.add(nastavnikInfo);
		});

		return new ResponseEntity<>(nastavniciDTO, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@GetMapping("/{id}")
	public ResponseEntity<?> pronadjiNastavnikaPoId(@PathVariable Integer id) {

		NastavnikEntity nastavnik = nastavnikRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Nastavnik", id)));

		NastavnikInfoDTO nastavnikInfo = NastavnikMapper.INSTANCE.nastavnikEntityToNastavnikInfoDTO(nastavnik);
		nastavnikService.addPredmetiIOdeljenja(nastavnikInfo, nastavnik);

		return new ResponseEntity<>(nastavnikInfo, HttpStatus.OK);

	}

	@PutMapping("/{nastavnikId}/{predmetId}")
	public ResponseEntity<?> dodajPredmetKojiPredaje(@PathVariable Integer nastavnikId,
			@PathVariable Integer predmetId) {

		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Nastavnik", nastavnikId)));

		PredmetEntity predmet = predmetRepository.findById(predmetId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Predmet", predmetId)));

		predmet.getNastavnici().add(nastavnik);

		predmetRepository.save(predmet);

		return new ResponseEntity<>(nastavnik, HttpStatus.OK);
	}

}
