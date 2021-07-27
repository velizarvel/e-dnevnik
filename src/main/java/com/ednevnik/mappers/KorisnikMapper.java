package com.ednevnik.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.dto.KorisnikInfoDTO;

@Mapper
public interface KorisnikMapper {

	KorisnikMapper INSTANCE = Mappers.getMapper(KorisnikMapper.class);

	@Mappings({ @Mapping(target = "id", source = "entity.id"),
			@Mapping(target = "korisnickoIme", source = "entity.korisnickoIme"),
			@Mapping(target = "uloga", source = "entity.uloga"),
			@Mapping(target = "imeIPrezime", expression = "java(entity.getIme() + \" \" + entity.getPrezime())") })
	KorisnikInfoDTO korisnikEntityToKorisnikInfoDTO(KorisnikEntity entity);

}
