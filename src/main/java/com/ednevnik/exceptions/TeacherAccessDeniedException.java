package com.ednevnik.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class TeacherAccessDeniedException extends AccessDeniedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeacherAccessDeniedException(String msg) {
		super(msg);
	}

}
