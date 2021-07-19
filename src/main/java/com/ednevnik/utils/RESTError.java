package com.ednevnik.utils;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RESTError {

	@JsonView(Views.PublicView.class)
	private int code;
	@JsonView(Views.PublicView.class)
	private String message;

}