package com.ednevnik.entities.dto;

import javax.validation.constraints.NotBlank;

import com.ednevnik.entities.enums.EUlogaEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KorisnikDTO {

	private Integer id;

	@NotBlank(message = "Korisnicko ime je obavezno polje")
	private String korisnickoIme;

	@NotBlank(message = "Sifra je obavezno polje")
	private String sifra;

	private EUlogaEntity uloga;

	@NotBlank(message = "Ime je obavezno polje")
	private String ime;

	@NotBlank(message = "Prezime je obavezno polje")
	private String prezime;

	private String email;

	private Integer odeljenjeId;

	private boolean obrisano = Boolean.FALSE;

}
