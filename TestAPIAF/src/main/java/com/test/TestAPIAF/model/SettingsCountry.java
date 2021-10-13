package com.test.TestAPIAF.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Laurent
 *
 * Represent a setting for accept registration on the country
 * if the country exist on database, we accept users from this country to register if the user has the minimum age
 */
@Entity
@Table(name = "SETTINGS_COUNTRY")
public class SettingsCountry {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "COUNTRY_NAME",unique=true, nullable=false)
	private String countryName; 
	
	@Column(name = "MINIMUM_AGE",nullable=false)
	private Integer minimumAge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(Integer minimumAge) {
		this.minimumAge = minimumAge;
	}
}
