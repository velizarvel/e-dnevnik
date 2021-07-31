package com.ednevnik.exceptions;

import java.util.Collection;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.ednevnik.entities.enums.EUlogaEntity;
import com.ednevnik.security.Views;

@ControllerAdvice
public class JsonViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

	@Override
	protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
			MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {

		Class<?> viewClass = Views.PublicView.class;

		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
			Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
					.getAuthorities();

			if (authorities.stream().anyMatch(o -> o.getAuthority().equals(EUlogaEntity.ROLE_UCENIK.name()))) {
				viewClass = Views.UcenikView.class;
			} else if (authorities.stream().anyMatch(o -> o.getAuthority().equals(EUlogaEntity.ROLE_RODITELJ.name()))) {
				viewClass = Views.RoditeljView.class;
			} else if (authorities.stream()
					.anyMatch(o -> o.getAuthority().equals(EUlogaEntity.ROLE_NASTAVNIK.name()))) {
				viewClass = Views.NastavnikView.class;
			} else if (authorities.stream()
					.anyMatch(o -> o.getAuthority().equals(EUlogaEntity.ROLE_ADMINISTRATOR.name()))) {
				viewClass = Views.AdminView.class;
			}
		}
		bodyContainer.setSerializationView(viewClass);
	}
}
