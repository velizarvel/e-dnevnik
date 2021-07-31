package com.ednevnik.mappers;

import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.UcenikInfoDTO;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UcenikMapper {

	public static UcenikMapper INSTANCE = Mappers.getMapper(UcenikMapper.class);

	@Mappings({ @Mapping(target = "id", source = "entity.id"),
			@Mapping(target = "korisnickoIme", source = "entity.korisnickoIme"),
			@Mapping(target = "uloga", source = "entity.uloga"),
			@Mapping(target = "prosekPolugodiste", source = "entity.prosekPolugodiste"),
			@Mapping(target = "prosekKrajGodine", source = "entity.prosekKrajGodine"),
			@Mapping(target = "uspehPolugodiste", source = "entity.uspehPolugodiste"),
			@Mapping(target = "uspehKrajGodine", source = "entity.uspehKrajGodine"),
			@Mapping(target = "podaciORoditeljima", ignore = true), @Mapping(target = "podaciOOcenama", ignore = true),
			@Mapping(target = "razredIOdeljenje", ignore = true),
			@Mapping(target = "imeIPrezime", expression = "java(entity.getIme() + \" \" + entity.getPrezime())") })
	public abstract UcenikInfoDTO ucenikEntityToUcenikInfoDTO(UcenikEntity entity);

	@AfterMapping
	public void ucitajPodatke(@MappingTarget UcenikInfoDTO ucenikInfo, UcenikEntity ucenikEntity) {
		ucenikInfo.setPodaciORoditeljima(
				ucenikEntity.getRoditelji().stream().map(r -> r.toString()).collect(Collectors.toList()));

		if (ucenikEntity.getOdeljenje() != null) {
			ucenikInfo.setRazredIOdeljenje(ucenikEntity.getOdeljenje().getRazredIOdeljenje());
		}

	}

}
