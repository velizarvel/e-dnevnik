package com.ednevnik.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OdeljenjeEntity;
import com.ednevnik.entities.PredmetEntity;
import com.ednevnik.exceptions.EntityNotFoundException;
import com.ednevnik.exceptions.GlobalExceptionHandler;
import com.ednevnik.repositories.NastavnikRepository;
import com.ednevnik.repositories.OdeljenjeRepository;
import com.ednevnik.utils.CustomValidation;

@Service
public class OdeljenjeServiceImpl implements OdeljenjeService {

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Autowired
	NastavnikRepository nastavnikRepository;

	@Override
	public OdeljenjeEntity updateOdeljenje(Integer razred, Integer brojOdeljenja, Integer generacija, Integer id) {
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(GlobalExceptionHandler.getMessage("Odeljenje", id)));

		odeljenje.setGeneracija(CustomValidation.setIfNotNull(odeljenje.getGeneracija(), generacija));
		odeljenje.setOdeljenje(CustomValidation.setIfNotNull(odeljenje.getOdeljenje(), brojOdeljenja));
		odeljenje.setRazred(CustomValidation.setIfNotNull(odeljenje.getRazred(), razred));

		odeljenjeRepository.save(odeljenje);

		return odeljenje;
	}

	@Override
	public Map<PredmetEntity, NastavnikEntity> dodajPredmetINastavnika(OdeljenjeEntity odeljenje,
			NastavnikEntity nastavnik, PredmetEntity predmet) {
		if (nastavnik.getPredmeti().contains(predmet)) {
			odeljenje.getPredmetNastavnikMapa().put(predmet, nastavnik);
			odeljenjeRepository.save(odeljenje);
			nastavnik.getOdeljenja().add(odeljenje);
			nastavnikRepository.save(nastavnik);
			return odeljenje.getPredmetNastavnikMapa();
		}
		return null;
	}

	@Override
	public boolean daLiNastavnikPredajePredmetOdeljenju(OdeljenjeEntity odeljenje, NastavnikEntity nastavnik,
			PredmetEntity predmet) {
		NastavnikEntity nastavnikKojiPredajePredmet = odeljenje.getPredmetNastavnikMapa().get(predmet);
		return nastavnik.equals(nastavnikKojiPredajePredmet) ? true : false;
	}

	@Override
	public NastavnikEntity getNastavnikKojiPredajePredmetOdeljenju(OdeljenjeEntity odeljenje, PredmetEntity predmet) {
		return odeljenje.getPredmetNastavnikMapa().get(predmet);
	}

}
