package com.ednevnik.services;

import java.util.Map;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.PredmetEntity;

public interface OdeljenjeService {

	public OdeljenjeEntity updateOdeljenje(Integer razred, Integer brojOdeljenja, Integer generacija, Integer id);

	public Map<PredmetEntity, NastavnikEntity> dodajPredmetINastavnika(OdeljenjeEntity odeljenje,
			NastavnikEntity nastavnik, PredmetEntity predmet);

	public boolean daLiNastavnikPredajePredmetOdeljenju(OdeljenjeEntity odeljenje, NastavnikEntity nastavnik,
			PredmetEntity predmet);

	public NastavnikEntity getNastavnikKojiPredajePredmetOdeljenju(OdeljenjeEntity odeljenje, PredmetEntity predmet);
}
