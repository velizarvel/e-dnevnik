package com.ednevnik.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.dto.KorisnikTokenDTO;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.utils.Encryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {

	@Value("${spring.security.secret-key}")
	private String secretKey;
	@Value("${spring.security.token-duration}")
	private Integer tokenDuration;

	@Autowired
	KorisnikRepository korisnikRepository;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam("korisnickoIme") String korisnickoIme,
			@RequestParam("sifra") String sifra) {
		KorisnikEntity korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme);
		if (korisnik != null && Encryption.validatePassword(sifra, korisnik.getSifra())) {
			String token = getJWTToken(korisnik);
			KorisnikTokenDTO korisnikDTO = new KorisnikTokenDTO();
			korisnikDTO.setKorisnickoIme(korisnickoIme);
			korisnikDTO.setToken(token);
			return new ResponseEntity<>(korisnikDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>("Pogresno korisnicko ime ili lozinka", HttpStatus.UNAUTHORIZED);
	}

	private String getJWTToken(KorisnikEntity korisnik) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(korisnik.getUloga().name());
		String token = Jwts.builder().setId("softtekJWT").setSubject(korisnik.getKorisnickoIme())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.tokenDuration))
				.signWith(SignatureAlgorithm.HS512, this.secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

}
