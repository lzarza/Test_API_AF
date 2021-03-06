package com.test.TestAPIAF.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class UserDTO {
	private Long id;
	private String userName; 
	
	@JsonFormat(pattern="dd-MM-yyyy")
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate birthDate;
	private String countryName;
	private String phoneNumber; //set as string to preserve format and grant use of + sign for international numbers
	private String gender;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
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
	/**
	 * Override to string to have a prettier display on logging.
	 */
	@Override public String toString() {
		StringBuilder descBld = new StringBuilder();
		descBld.append("Id : ").append(this.id).append("; ");
		descBld.append("Username : ").append(this.userName).append("; ");
		descBld.append("Country : ").append(this.countryName).append("; ");
		descBld.append("BirthDate : ").append(this.birthDate.toString()).append("; ");
		if(phoneNumber != null && !phoneNumber.trim().isEmpty()) {
			descBld.append("PhoneNumber : ").append(this.phoneNumber).append("; ");
		}
		if(gender != null && !gender.trim().isEmpty()) {
			descBld.append("Gender : ").append(this.gender).append("; ");
		}
		return descBld.toString();
	}
}
