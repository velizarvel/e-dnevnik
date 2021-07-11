package com.ednevnik.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private String secret;

	public JWTAuthorizationFilter(String secret) {
		this.secret = secret;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// proverimo da li postoji jwt
		if (checkIfExists(request)) {
			// ako je poslat proverimo da li je validan
			Claims claims = validateToken(request);
			if (claims.get("authorities") != null) {
				// ako je validan onda postavimo security context na osnovu toga sto smo
				// iscitali iz tokena (roles)
				setUpSpringAuthentication(claims);
			} else {
				// ako nije validan izbrisi sve iz konteksta
				SecurityContextHolder.clearContext();
			}
		} else {
			// ako ne postoji jwt izbrisi sve iz konteksta
			SecurityContextHolder.clearContext();
		}

		// dodamo nas filter na ostale filtere

		filterChain.doFilter(request, response);
	}

	private boolean checkIfExists(HttpServletRequest request) {

		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX)) {
			return false;
		}
		return true;
	}

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		// iz klejmova izvuce authorities i dobijemo listu njegovih rola
		List<String> authorities = (List<String>) claims.get("authorities");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
