package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

import com.ednevnik.entities.enums.EUspehEntity;
import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private OdeljenjeEntity odeljenje;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "ucenici")
	@JsonView(Views.RoditeljView.class)
	@JsonBackReference
	private Set<RoditeljEntity> roditelji = new HashSet<RoditeljEntity>();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "ucenik")
	@JsonView(Views.UcenikView.class)
	@JsonIgnore
	private List<OcenaEntity> ocene = new ArrayList<OcenaEntity>();

	@JsonView(Views.UcenikView.class)
	private Double prosekPolugodiste;

	@JsonView(Views.UcenikView.class)
	private Double prosekKrajGodine;

	@JsonView(Views.UcenikView.class)
	private EUspehEntity uspehPolugodiste = EUspehEntity.NIJE_OCENJEN;

	@JsonView(Views.UcenikView.class)
	private EUspehEntity uspehKrajGodine = EUspehEntity.NIJE_OCENJEN;

	public EUspehEntity getEUspehPolugodiste() {
		return izracunajUspeh(prosekPolugodiste);
	}

	public EUspehEntity getEUspehKrajGodine() {
		return izracunajUspeh(prosekKrajGodine);
	}

	private EUspehEntity izracunajUspeh(double prosek) {
		Integer ocena = (int) Math.round(prosek);

		switch (ocena) {
		case 5:
			return EUspehEntity.ODLICAN;
		case 4:
			return EUspehEntity.VRLO_DOBAR;
		case 3:
			return EUspehEntity.DOBAR;
		case 2:
			return EUspehEntity.DOVOLJAN;
		case 1:
			return EUspehEntity.NEDOVOLJAN;
		default:
			return EUspehEntity.NIJE_OCENJEN;
		}
	};

}
