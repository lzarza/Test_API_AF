package com.test.TestAPIAF.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.test.TestAPIAF.model.SettingsCountry;

public interface SettingsCountryRepository extends CrudRepository<SettingsCountry,Long> {
	public Optional<SettingsCountry> findByCountryName(String countryName);
}
