package com.ednevnik.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.SQLDelete;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roditelj")
@Getter
@Setter
@SQLDelete(sql = "UPDATE korisnik SET obrisano = true WHERE id=?")
public class RoditeljEntity extends KorisnikEntity {

	@JsonView(Views.RoditeljView.class)
	@Column(unique = true)
	@NotBlank(message = "Email je zahtevano polje")
	private String email;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinTable(name = "ucenik_roditelj", joinColumns = { @JoinColumn(name = "roditelj_id") }, inverseJoinColumns = {
			@JoinColumn(name = "ucenik_id") })
	@JsonManagedReference
	@JsonView(Views.RoditeljView.class)
	private Set<UcenikEntity> ucenici = new HashSet<UcenikEntity>();

	@Override
	public String toString() {
		return this.ime + " " + this.prezime + ", email: " + this.email;
	}

}
