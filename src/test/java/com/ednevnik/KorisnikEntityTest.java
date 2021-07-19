package com.ednevnik;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ednevnik.entities.EUlogaEntity;
import com.ednevnik.entities.KorisnikEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.entities.dto.KorisnikDTO;
import com.ednevnik.entities.factories.KorisnikFactory;
import com.ednevnik.repositories.KorisnikRepository;
import com.ednevnik.repositories.RoditeljRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class KorisnikEntityTest {

	@Autowired
	private KorisnikRepository korisnikRepository;

	@Autowired
	private RoditeljRepository roditeljRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testKreirajKorisnika() {
		KorisnikDTO korisnikDTO = new KorisnikDTO();
		korisnikDTO.setEmail("korisnik@email.com");
		korisnikDTO.setIme("korisnik");
		korisnikDTO.setKorisnickoIme("korisnik");
		korisnikDTO.setPrezime("korisnik");
		korisnikDTO.setSifra("korisnik");
		korisnikDTO.setUloga(EUlogaEntity.ROLE_RODITELJ);

		KorisnikEntity korisnik = KorisnikFactory.createKorisnik(korisnikDTO);

		KorisnikEntity sacuvaniKorisnik = korisnikRepository.save(korisnik);

		RoditeljEntity roditelj = entityManager.find(RoditeljEntity.class, sacuvaniKorisnik.getId());

		assertThat(korisnikDTO.getEmail().equals(roditelj.getEmail()));

	}

	@Test
	public void izbrisiKorisnikaPoKorisnickomImenu() {
		KorisnikEntity korisnik = korisnikRepository.findByKorisnickoIme("korisnik");

		assertThat(korisnik.getPrezime()
				.equals(roditeljRepository.findByEmail(((RoditeljEntity) korisnik).getEmail()).getPrezime()));

		korisnikRepository.deleteByKorisnickoIme(korisnik.getKorisnickoIme());

		assertThat(korisnik.equals(null));

	}
}