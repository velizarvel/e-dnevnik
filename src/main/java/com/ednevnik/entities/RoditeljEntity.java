package com.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roditelj")
@Getter
@Setter
public class RoditeljEntity extends KorisnikEntity {

	@NotBlank(message = "Email is requested field")
	private String email;

	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.REFRESH,
                CascadeType.MERGE
            })
    @JoinTable(name = "ucenik_roditelj",
            joinColumns = { @JoinColumn(name = "roditelj_id") },
            inverseJoinColumns = { @JoinColumn(name = "ucenik_id") })
	@JsonView(Views.RoditeljView.class)
	List<UcenikEntity> ucenici = new ArrayList<UcenikEntity>();

}
