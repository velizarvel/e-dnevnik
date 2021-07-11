package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ucenik")
@Getter
@Setter
public class UcenikEntity extends KorisnikEntity {

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "ucenici")
	@JsonView(Views.NastavnikView.class)
	List<RoditeljEntity> roditelji = new ArrayList<RoditeljEntity>();

}
