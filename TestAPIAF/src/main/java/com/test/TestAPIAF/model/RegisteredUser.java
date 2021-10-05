package com.test.TestAPIAF.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegisteredUser {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String userName; 
	
	@Column(nullable=false)
	private LocalDate birthDate;
	
	@Column(nullable=false)
	private String countryName;
	
	@Column
	private String phoneNumber; //set as string to preserve format and grant use of + sign for international numbers

	@Column
	private String gender;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		//check null or class
		if (obj == null || getClass() != obj.getClass() ) {
			return false;
		}
		
		//check both ids aren't null
		RegisteredUser other = (RegisteredUser) obj;
		if(this.id == null || other.id == null) {
			return false;
		}
		return Objects.equals(id, other.id);
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
}
