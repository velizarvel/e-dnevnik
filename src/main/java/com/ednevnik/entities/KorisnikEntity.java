package com.ednevnik.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "korisnik")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE korisnik SET obrisano = true WHERE id=? AND verzija=?")
@Where(clause = "obrisano=false")
public class KorisnikEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@JsonView(Views.AdminView.class)
	private Integer id;

	@Column(name = "korisnicko_ime")
	@JsonView(Views.UcenikView.class)
	@NotBlank(message = "Korisnicko ime ne moze biti prazno polje")
	private String korisnickoIme;

	@JsonIgnore
	private String sifra;

	@JsonView(Views.AdminView.class)
	@Enumerated(EnumType.STRING)
	protected EUlogaEntity uloga;

	@JsonView(Views.UcenikView.class)
	@NotBlank(message = "Ime ne moze biti prazno polje")
	protected String ime;

	@JsonView(Views.UcenikView.class)
	@NotBlank(message = "Prezime ne moze biti prazno polje")
	protected String prezime;

	protected boolean obrisano;

	@Version
	@JsonView(Views.AdminView.class)
	protected Integer verzija;

	public KorisnikEntity(String korisnickoIme, String sifra) {
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
	}

	public KorisnikEntity(String korisnickoIme, String sifra, String ime, String prezime) {
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
	}

}
