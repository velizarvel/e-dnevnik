package com.ednevnik.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	public static String getPassEncoded(String sifra) {
		return new BCryptPasswordEncoder().encode(sifra);
	}

	public static boolean validatePassword(String sifra, String enkodiranaSifra) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(sifra, enkodiranaSifra);
	}

	public static void main(String[] args) {
		System.out.println(getPassEncoded("admin"));

	}

}