package com.test.TestAPIAF.service;

import javax.management.InstanceAlreadyExistsException;

import com.test.TestAPIAF.model.RegisteredUser;

public interface IRegisteredUserService {
	/**
	 * Get a registered name by his user name. Return null if no user found
	 * @param userName
	 * @return the user found
	 * @throws IllegalArgumentException if user name is null or empty
	 */
	public RegisteredUser getRegisteredUser(String userName) throws IllegalArgumentException;
	
	/**
	 * Register a user into database and return the registered user
	 * @param userToAdd
	 * @return the user saved in database
	 * @throws IllegalArgumentException if user name, country, birth date are null or empty and if phone number is malformed
	 * @throws IllegalStateException if the new user do not meet the registration acceptance (age and country)
	 * @throws InstanceAlreadyExistsException if the user name already exist
	 */
	public RegisteredUser addRegisteredUser(RegisteredUser userToAdd) throws IllegalArgumentException,IllegalStateException, InstanceAlreadyExistsException;
}
