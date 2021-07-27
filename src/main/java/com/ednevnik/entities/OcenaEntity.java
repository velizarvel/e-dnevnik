package com.ednevnik.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.ednevnik.security.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ocena")
@Getter
@Setter
@NoArgsConstructor
public class OcenaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Min(value = 1, message = "Najmanja ocena moze biti {value}")
	@Max(value = 5, message = "Najveca ocena moze biti {value}")
	@Column(name = "vrednost_ocene")
	@NotNull(message = "Ocena mora biti uneta.")
	@JsonView(Views.UcenikView.class)
	private Integer vrednostOcene;

	@PastOrPresent(message = "Datum ocene ne moze biti u buducem vremenu.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.UcenikView.class)
	private LocalDate datum;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ucenik_id")
	@JsonView(Views.UcenikView.class)
	private UcenikEntity ucenik;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predmet_id")
	@JsonView(Views.UcenikView.class)
	private PredmetEntity predmet;

	@JsonView(Views.UcenikView.class)
	private String aktivnost;

	@Version
	Integer verzija;

	@Override
	public String toString() {
		return "ocena: " + this.vrednostOcene + ", datum: " + this.datum + ", aktivnost: " + this.aktivnost;
	}

}
