package com.ednevnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.security.Views;
import com.ednevnik.service.UcenikService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("ucenici")
public class UcenikController {

	@Autowired
	UcenikService ucenikService;

	@GetMapping()
	@JsonView(Views.UcenikView.class)
	public ResponseEntity<?> prikaziUcenike() {
		return new ResponseEntity<>(ucenikService.prikaziUcenike(), HttpStatus.OK);
	}

}
