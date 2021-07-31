package com.ednevnik.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class GradeException extends AccessDeniedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GradeException(String msg) {
		super(msg);
	}

}
