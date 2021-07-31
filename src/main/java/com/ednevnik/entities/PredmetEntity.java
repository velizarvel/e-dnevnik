package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "predmet")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PredmetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.AdminView.class)
	private Integer id;

	@Column(name = "naziv_predmeta", unique=true)
	@NotBlank(message = "Naziv predmeta mora biti unet.")
	@JsonView(Views.UcenikView.class)
	private String nazivPredmeta;

	@Column(name = "fond_casova")
	@NotNull(message = "Fond casova mora biti unet.")
	@JsonView(Views.UcenikView.class)
	private Integer fondCasova;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinTable(name = "predmet_nastavnik", joinColumns = { @JoinColumn(name = "predmet_id") }, inverseJoinColumns = {
			@JoinColumn(name = "nastavnik_id") })
	@JsonView(Views.RoditeljView.class)
	@JsonManagedReference
	private Set<NastavnikEntity> nastavnici = new HashSet<NastavnikEntity>();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "predmet")
	@JsonIgnore
	 List<OcenaEntity> ocene = new ArrayList<OcenaEntity>();

	@Version
	private Integer verzija;

	@Override
	public String toString() {
		return "id: " + id + ", predmet: " + nazivPredmeta;
	}

}
