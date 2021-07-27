package com.ednevnik.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.dto.NastavnikInfoDTO;

@Mapper
public interface NastavnikMapper {

	NastavnikMapper INSTANCE = Mappers.getMapper(NastavnikMapper.class);

	@Mappings({ @Mapping(target = "id", source = "entity.id"),
			@Mapping(target = "korisnickoIme", source = "entity.korisnickoIme"),
			@Mapping(target = "uloga", source = "entity.uloga"),
			@Mapping(target = "naziviPredmetaKojePredaje", ignore = true),
			@Mapping(target = "predajePredmeteOdeljenjima", ignore = true),
			@Mapping(target = "imeIPrezime", expression = "java(entity.getIme() + \" \" + entity.getPrezime())") })
	NastavnikInfoDTO nastavnikEntityToNastavnikInfoDTO(NastavnikEntity entity);

	@AfterMapping
	default void addPredmeti(@MappingTarget NastavnikInfoDTO nastavnikInfo, NastavnikEntity entity) {
		Set<String> predmeti = entity.getPredmeti().stream().map(x -> x.getNazivPredmeta()).collect(Collectors.toSet());
		nastavnikInfo.setNaziviPredmetaKojePredaje(predmeti);

	}
}
