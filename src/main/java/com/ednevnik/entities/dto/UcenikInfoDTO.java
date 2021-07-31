package com.ednevnik.entities.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ednevnik.entities.enums.EUspehEntity;
import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UcenikInfoDTO extends KorisnikInfoDTO {

	@JsonView(Views.UcenikView.class)
	private String razredIOdeljenje;

	@JsonView(Views.RoditeljView.class)
	private List<String> podaciORoditeljima = new ArrayList<>();

	@JsonView(Views.UcenikView.class)
	private Map<String, List<String>> podaciOOcenama = new HashMap<String, List<String>>();

	@JsonView(Views.UcenikView.class)
	private Double prosekPolugodiste;

	@JsonView(Views.UcenikView.class)
	private Double prosekKrajGodine;

	@Getter(AccessLevel.NONE)
	@JsonView(Views.UcenikView.class)
	private EUspehEntity uspehPolugodiste;

	@Getter(AccessLevel.NONE)
	@JsonView(Views.UcenikView.class)
	private EUspehEntity uspehKrajGodine;

}
