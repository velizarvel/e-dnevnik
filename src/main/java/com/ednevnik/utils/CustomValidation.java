package com.ednevnik.utils;

public class CustomValidation {

	public static <T> T setIfNotNull(T oldProperty, T newProperty) {
		return newProperty.equals(null) ? oldProperty : newProperty;
	}

}
