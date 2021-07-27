package com.ednevnik.entities.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NastavnikInfoDTO extends KorisnikInfoDTO {

	@JsonView(Views.NastavnikView.class)
	private Set<String> naziviPredmetaKojePredaje;
	
	@JsonView(Views.NastavnikView.class)
	private List<String> predajePredmeteOdeljenjima = new ArrayList<>();

}
