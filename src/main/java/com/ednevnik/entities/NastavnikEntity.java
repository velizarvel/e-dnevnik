package com.ednevnik.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nastavnik")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE korisnik SET obrisano = true WHERE id=?")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class NastavnikEntity extends KorisnikEntity {

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "nastavnici")
	@JsonView(Views.NastavnikView.class)
	@JsonBackReference
	private Set<PredmetEntity> predmeti = new HashSet<PredmetEntity>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinTable(name = "odeljenje_nastavnik", joinColumns = {
			@JoinColumn(name = "nastavnik_id") }, inverseJoinColumns = { @JoinColumn(name = "odeljenje_id") })
	@JsonView(Views.NastavnikView.class)
	@JsonIgnore
	private Set<OdeljenjeEntity> odeljenja = new HashSet<OdeljenjeEntity>();

}
