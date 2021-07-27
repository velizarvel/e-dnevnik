package com.ednevnik.entities.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

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

}
