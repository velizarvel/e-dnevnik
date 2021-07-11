package com.ednevnik.entities.dto;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class KorisnikTokenDTO {

	@JsonView(Views.PublicView.class)
	private String korisnickoIme;
	@JsonView(Views.PublicView.class)
	private String token;

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
