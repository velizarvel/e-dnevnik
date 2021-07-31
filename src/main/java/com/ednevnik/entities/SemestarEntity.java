package com.ednevnik.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ednevnik.entities.enums.ESemestarEntity;
import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "semestar")
@Getter
@Setter
@NoArgsConstructor
public class SemestarEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@JsonView(Views.AdminView.class)
	private Integer id;

	@JsonView(Views.UcenikView.class)
	private ESemestarEntity eSemestar;
}
