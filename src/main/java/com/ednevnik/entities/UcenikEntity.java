package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ucenik")
@Getter
@Setter
@SQLDelete(sql = "UPDATE korisnik SET obrisano = true WHERE id=?")
public class UcenikEntity extends KorisnikEntity {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje_id")
	@JsonView(Views.UcenikView.class)
	OdeljenjeEntity odeljenje;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "ucenici")
	@JsonView(Views.RoditeljView.class)
	List<RoditeljEntity> roditelji = new ArrayList<RoditeljEntity>();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "ucenik")
	@JsonView(Views.UcenikView.class)
	List<OcenaEntity> ocene = new ArrayList<OcenaEntity>();

}
