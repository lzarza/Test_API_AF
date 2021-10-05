package com.test.TestAPIAF.services.impl;

import java.time.LocalDate;
import java.util.Optional;

import javax.management.InstanceAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.TestAPIAF.model.RegisteredUser;
import com.test.TestAPIAF.model.SettingsCountry;
import com.test.TestAPIAF.repositories.RegisteredUserRepository;
import com.test.TestAPIAF.repositories.SettingsCountryRepository;
import com.test.TestAPIAF.services.IRegisteredUserService;

import javassist.NotFoundException;

public class RegisteredUserService implements IRegisteredUserService {
	 
	@Autowired
	private RegisteredUserRepository registerUserRepository;
	
	@Autowired
	private SettingsCountryRepository settingsCountryRepository;
	
	@Override
	public RegisteredUser getRegisteredUser(String userName) throws NotFoundException {
		if(userName==null || userName.trim().isEmpty()) {
			throw new IllegalArgumentException("Username not sent");
		}
		
		Optional<RegisteredUser> result = registerUserRepository.findByUserName(userName.trim());
		if(result.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		return result.get();
	}

	@Override
	public RegisteredUser addRegisteredUser(RegisteredUser userToAdd) throws IllegalArgumentException, InstanceAlreadyExistsException {
		//Verify mandatory fields are filled
		if(userToAdd.getUserName() == null || userToAdd.getUserName().trim().isEmpty()) {
			throw new IllegalArgumentException("User name is mandatory");
		}
		if(userToAdd.getCountryName() == null || userToAdd.getCountryName().trim().isEmpty()) {
			throw new IllegalArgumentException("Country name is mandatory");
		}
		if(userToAdd.getBirthDate() == null){
			throw new IllegalArgumentException("Birth date is mandatory");
		}
		
		//Verify the age, get the country accepted settings
		Optional<SettingsCountry> settingCountry = settingsCountryRepository.findByCountryName(userToAdd.getCountryName());
		if(settingCountry.isEmpty()) {
			throw new IllegalArgumentException("Invalid registration country");
		}
		
		if(userToAdd.getBirthDate().plusYears(settingCountry.get().getMinimumAge()).compareTo(LocalDate.now()) <= 0) {
			throw new IllegalArgumentException("Invalid age");
		}
		
		//does the user already exists ?
		Optional<RegisteredUser> result = registerUserRepository.findByUserName(userToAdd.getUserName());
		if(result.isPresent()) {
			throw new InstanceAlreadyExistsException("User alredy exist");
		}
		
		return registerUserRepository.save(userToAdd);
	}

}
