package com.ednevnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.repositories.RoditeljRepository;
import com.ednevnik.repositories.UcenikRepository;
import com.ednevnik.utils.RESTError;

@RestController
@RequestMapping("/roditelji")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepository;

	@Autowired
	private UcenikRepository ucenikRepository;

	@PutMapping("/{roditeljId}/{ucenikId}")
	public ResponseEntity<?> dodajUcenika(@PathVariable Integer roditeljId, @PathVariable Integer ucenikId) {

		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).orElse(null);
		if (roditelj == null) {
			return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(),
					"Roditelj sa id: " + roditeljId + " se ne nalazi u bazi"), HttpStatus.BAD_REQUEST);
		}
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).orElse(null);
		if (ucenik == null) {
			return new ResponseEntity<>(
					new RESTError(HttpStatus.BAD_REQUEST.value(), "Ucenik sa id: " + ucenikId + " se ne nalazi u bazi"),
					HttpStatus.BAD_REQUEST);
		}
		roditelj.getUcenici().add(ucenik);
		roditeljRepository.save(roditelj);

		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}

}
