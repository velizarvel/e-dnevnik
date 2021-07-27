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

import com.ednevnik.facade.AuthenticationFacade;
import com.ednevnik.utils.RESTError;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	private static String message;

	@Autowired
	private AuthenticationFacade authentication;

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> constraintHandleError(ConstraintViolationException e) {
		String messageEx = "";
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			if (!messageEx.isEmpty()) {
				messageEx += "\n";
			}
			messageEx += violation.getMessage();
		}
		log.warn(messageEx);
		return new ResponseEntity<>(new RESTError(HttpStatus.PRECONDITION_FAILED.value(), messageEx.toString()),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDeniedHandleError(AccessDeniedException e, ServletWebRequest request) {
		String messageEx = "Nije vam odobren pristup za trazenu aktivnost. Za metod: "
				+ request.getRequest().getMethod() + " na stranici: " + request.getRequest().getRequestURI()
				+ " nemate odgovarajucu korisnicku rolu.";

		log.warn("Korisnik " + authentication.getAuthentication().getName() + " je pokusao da pristupi stranici: "
				+ request.getRequest().getRequestURI() + " metod: " + request.getRequest().getMethod()
				+ " za koju nema odobrenje.");
		return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), messageEx), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> expiredJwtHandleError(ExpiredJwtException e, ServletWebRequest request) {
		String messageEx = "Niste se ulogovali, ili vam je istekla sesija.";

		return new ResponseEntity<>(new RESTError(HttpStatus.UNAUTHORIZED.value(), messageEx), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> entityNotFoundHandleError(EntityNotFoundException e) {

		return new ResponseEntity<>(new RESTError(HttpStatus.BAD_REQUEST.value(), GlobalExceptionHandler.message),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TeacherAccessDeniedException.class)
	public ResponseEntity<?> teacherAccessDeniedHandleError(TeacherAccessDeniedException e) {

		log.warn(e.getMessage());

		return new ResponseEntity<>(new RESTError(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
	}

	public static String getMessage(String entity, Integer id) {
		GlobalExceptionHandler.message = entity + " sa id: " + id + " se ne nalazi u bazi";
		return GlobalExceptionHandler.message;
	}
}
