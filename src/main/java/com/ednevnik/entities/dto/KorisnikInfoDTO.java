package com.ednevnik.entities.dto;

import com.ednevnik.entities.EUlogaEntity;
import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KorisnikInfoDTO {

	@JsonView(Views.AdminView.class)
	private Integer id;

	@JsonView(Views.UcenikView.class)
	private String korisnickoIme;

	@JsonView(Views.AdminView.class)
	private EUlogaEntity uloga;

	@JsonView(Views.UcenikView.class)
	private String imeIPrezime;

}
