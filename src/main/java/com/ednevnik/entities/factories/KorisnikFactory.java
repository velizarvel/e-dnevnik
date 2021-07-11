package com.ednevnik.entities.factories;

import com.ednevnik.entities.EUlogaEntity;
import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.UcenikEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.utils.Encryption;

public class KorisnikFactory {

	public static KorisnikEntity createKorisnik(EUlogaEntity uloga) {
		KorisnikEntity korisnik;

		switch (uloga) {
		case ROLE_ADMINISTRATOR:
			korisnik = new KorisnikEntity();
			break;
		case ROLE_NASTAVNIK:
			korisnik = new NastavnikEntity();
			break;
		case ROLE_RODITELJ:
			korisnik = new RoditeljEntity();
			break;
		case ROLE_UCENIK:
			korisnik = new UcenikEntity();
			break;
		default:
			throw new IllegalArgumentException("Pogresno unesena uloga.");
		}

		korisnik.setUloga(uloga);

		return korisnik;
	}

	public static KorisnikEntity createKorisnik(KorisnikDTO korisnik) {
		KorisnikEntity noviKorisnik;

		switch (korisnik.getUloga()) {
		case ROLE_ADMINISTRATOR:
			noviKorisnik = new KorisnikEntity();
			break;
		case ROLE_NASTAVNIK:
			noviKorisnik = new NastavnikEntity();
			break;
		case ROLE_RODITELJ:
			noviKorisnik = new RoditeljEntity();
			((RoditeljEntity) noviKorisnik).setEmail(korisnik.getEmail());
			
			break;
		case ROLE_UCENIK:
			noviKorisnik = new UcenikEntity();
			break;
		default:
			throw new IllegalArgumentException("Pogresno unesena uloga.");
		}

		noviKorisnik.setUloga(korisnik.getUloga());
		noviKorisnik.setIme(korisnik.getIme());
		noviKorisnik.setPrezime(korisnik.getPrezime());
		noviKorisnik.setKorisnickoIme(korisnik.getKorisnickoIme());
		noviKorisnik.setSifra(Encryption.getPassEncoded(korisnik.getSifra()));

		return noviKorisnik;
	}

}
