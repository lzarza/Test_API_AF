package com.test.TestAPIAF.services;

import javax.management.InstanceAlreadyExistsException;

import com.test.TestAPIAF.model.RegisteredUser;

import javassist.NotFoundException;

public interface IRegisteredUserService {
	/**
	 * Get a registered name by his user name
	 * @param userName
	 * @return
	 */
	public RegisteredUser getRegisteredUser(String userName) throws IllegalArgumentException, NotFoundException;
	public RegisteredUser addRegisteredUser(RegisteredUser userToAdd) throws IllegalArgumentException, InstanceAlreadyExistsException;
}
