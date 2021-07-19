package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "odeljenje")
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SQLDelete(sql = "UPDATE odeljenje SET obrisano = true WHERE id=? and verzija = ?")
@Where(clause = "obrisano=false")
public class OdeljenjeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private boolean obrisano = Boolean.FALSE;
	
	@Min(value = 1, message = "Broj razreda moze biti najmanje {value}")
	@Max(value = 8, message = "Broj razreda moze biti najvise {value}")
	@NotNull(message = "Razred mora biti unet.")
	@JsonView(Views.UcenikView.class)
	private Integer razred;

	@Min(value = 1, message = "Broj odeljenja moze biti najmanje {value}")
	@Max(value = 10, message = "Broj odeljenja moze biti najvise {value}")
	@NotNull(message = "Odeljenje mora biti uneto.")
	@JsonView(Views.UcenikView.class)
	private Integer odeljenje;

	@NotNull(message = "Godiste generacije mora biti uneseno.")
	@Min(value = 2000, message = "Godiste generacije mora biti vece od {value}.")
	@Max(value = 2100, message = "Godiste generacije mora biti manje od  {value}.")
	@JsonView(Views.UcenikView.class)
	private Integer generacija;

	@OneToMany(mappedBy = "odeljenje", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UcenikEntity> ucenici = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "odeljenja")
	@JsonView(Views.RoditeljView.class)
	List<NastavnikEntity> nastavnici = new ArrayList<NastavnikEntity>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "predmet_nastavnik_map", joinColumns = {
			@JoinColumn(name = "odeljenje_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "nastavnik_id", referencedColumnName = "id") })
	@MapKeyJoinColumn(name = "predmet_id")
	@JsonView(Views.UcenikView.class)
	private Map<PredmetEntity, NastavnikEntity> predmetNastavnikMapa = new HashMap<PredmetEntity, NastavnikEntity>();

	@Version
	@JsonView(Views.AdminView.class)
	protected Integer verzija;
	
	public String getRazredIOdeljenje() {
		int broj = this.razred;
		String rezultat = "";
		int temp;

		String[] rimskiBrojevi = { "V", "IV", "I" };
		Integer[] arapskiBrojevi = { 5, 4, 1 };
		for (int i = 0; i < arapskiBrojevi.length; i++) {
			temp = broj / arapskiBrojevi[i];
			broj %= arapskiBrojevi[i];

			while (temp > 0) {
				rezultat += rimskiBrojevi[i];
				temp--;
			}
		}
		return rezultat + "/" + this.odeljenje;
	}

}
