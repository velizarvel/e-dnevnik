package com.ednevnik.exceptions;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.ednevnik.facade.AuthenticationFacade;
import com.ednevnik.utils.RESTError;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@Autowired
	private AuthenticationFacade authentication;

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> constraintHandleError(ConstraintViolationException e, WebRequest request) {
		String message = "";
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += violation.getMessage();
		}
		log.warn(message);
		return new ResponseEntity<>(new RESTError(HttpStatus.PRECONDITION_FAILED.value(), message.toString()),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDeniedHandleError(AccessDeniedException e, ServletWebRequest request) {
		String message = "Nije vam odobren pristup za trazenu aktivnost. Za metod: " + request.getRequest().getMethod() + " na stranici: " +  request.getRequest().getRequestURI() + " nemate odgovarajucu korisnicku rolu.";

		log.warn("Korisnik " + authentication.getAuthentication().getName()
				+ " je pokusao da pristupi stranici: " + request.getRequest().getRequestURI()  + " metod: " + request.getRequest().getMethod() + " za koju nema odobrenje.");
		return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> expiredJwtHandleError(ExpiredJwtException e, ServletWebRequest request) {
		String message = "Niste se ulogovali, ili vam je istekla sesija.";

		return new ResponseEntity<>(new RESTError(HttpStatus.UNAUTHORIZED.value(), message), HttpStatus.UNAUTHORIZED);
	}
}
