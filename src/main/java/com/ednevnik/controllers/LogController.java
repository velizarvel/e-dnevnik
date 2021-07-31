package com.ednevnik.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.services.KorisnikService;
import com.ednevnik.utils.RESTError;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/log")
@Secured("ROLE_ADMINISTRATOR")
@Log4j2
public class LogController {

	@Autowired
	private KorisnikService korisnikService;

	@Value("${logging.file.name}")
	private String LOG_PATH;

	@PostMapping("/download")
	public ResponseEntity<?> downloadLogs() {

		File file = new File(LOG_PATH);
		Date timestamp = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = file.getName().concat("_").concat(formatter.format(timestamp));

		try {

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "attachment; filename=" + fileName);

			log.info("Korisnik " + korisnikService.getKorisnik().getKorisnickoIme() + " je download-ovao fajl "
					+ fileName);

			return ResponseEntity.ok().headers(responseHeaders).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (FileNotFoundException e) {
			log.warn("Doslo je do greske prilikom ucitavanja fajla sa lokacije : " + LOG_PATH);
			return new ResponseEntity<>(new RESTError(HttpStatus.NOT_FOUND.value(), "Fajl nije pronadjen."),
					HttpStatus.NOT_FOUND);
		}

	}

}
