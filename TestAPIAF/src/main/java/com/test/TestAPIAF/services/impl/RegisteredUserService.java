package com.test.TestAPIAF.services.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.TestAPIAF.model.RegisteredUser;
import com.test.TestAPIAF.model.SettingsCountry;
import com.test.TestAPIAF.repositories.RegisteredUserRepository;
import com.test.TestAPIAF.repositories.SettingsCountryRepository;
import com.test.TestAPIAF.services.IRegisteredUserService;

@Service
public class RegisteredUserService implements IRegisteredUserService {
	 
	@Autowired
	private RegisteredUserRepository registerUserRepository;
	
	@Autowired
	private SettingsCountryRepository settingsCountryRepository;
	
	@Override
	public RegisteredUser getRegisteredUser(String userName) throws IllegalArgumentException {
		if(userName==null || userName.trim().isEmpty()) {
			throw new IllegalArgumentException("Username not sent");
		}
		
		Optional<RegisteredUser> result = registerUserRepository.findByUserName(userName.trim());
		if(result.isEmpty()) {
			return null;
		}
		return result.get();
	}

	@Override
	public RegisteredUser addRegisteredUser(RegisteredUser userToAdd) throws IllegalArgumentException,IllegalStateException, InstanceAlreadyExistsException {
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
			throw new IllegalStateException("Invalid registration country");
		}
		
		if(userToAdd.getBirthDate().plusYears(settingCountry.get().getMinimumAge()).compareTo(LocalDate.now()) <= 0) {
			throw new IllegalStateException("Invalid age");
		}
		
		//verify the phone number
		if(userToAdd.getPhoneNumber() != null && userToAdd.getPhoneNumber().isEmpty() && !Pattern.matches("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", userToAdd.getPhoneNumber())) {
			throw new IllegalArgumentException("Invalid phone number");
		}
		
		//does the user already exists ?
		Optional<RegisteredUser> result = registerUserRepository.findByUserName(userToAdd.getUserName());
		if(result.isPresent()) {
			throw new InstanceAlreadyExistsException("User alredy exist");
		}
		
		return registerUserRepository.save(userToAdd);
	}

}
