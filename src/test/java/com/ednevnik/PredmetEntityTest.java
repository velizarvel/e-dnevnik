package com.ednevnik;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.repositories.PredmetRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class PredmetEntityTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PredmetRepository predmetRepository;
	
	@Test
	public void testirajVezuPredmetaINastavnika() {
		NastavnikEntity nastavnik = entityManager.find(NastavnikEntity.class, 21);
		PredmetEntity predmet = entityManager.find(PredmetEntity.class, 45);
		
		predmet.getNastavnici().add(nastavnik);
		
		predmetRepository.save(predmet);
		
		assertThat(predmet.getNastavnici().contains(nastavnik));
		
	}
	
}
