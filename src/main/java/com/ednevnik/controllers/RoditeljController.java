package com.ednevnik.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.repositories.RoditeljRepository;
import com.ednevnik.repositories.UcenikRepository;

@RestController
@RequestMapping("/roditelji")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepository;

	@Autowired
	private UcenikRepository ucenikRepository;

	@GetMapping()
	@Secured("ROLE_ADMINISTRATOR")
	public ResponseEntity<?> prikaziRoditelje() {

		List<RoditeljEntity> roditelji = (List<RoditeljEntity>) roditeljRepository.findAll();

		return new ResponseEntity<>(roditelji, HttpStatus.OK);
	}

	@Secured("ROLE_ADMINISTRATOR")
	@PutMapping("/{roditeljId}/{ucenikId}")
	public ResponseEntity<?> dodajUcenika(@PathVariable Integer roditeljId, @PathVariable Integer ucenikId) {

		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).orElseThrow(
				() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Roditelj", roditeljId)));

		UcenikEntity ucenik = ucenikRepository.findById(ucenikId)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Ucenik", ucenikId)));

		roditelj.getUcenici().add(ucenik);
		roditeljRepository.save(roditelj);

		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}

}
