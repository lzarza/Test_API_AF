package com.test.TestAPIAF.controller;

import org.springframework.http.ResponseEntity;

import com.test.TestAPIAF.dto.UserDTO;

public interface IUserController {

	/**
	 * Save a user into database if all values are set and correct
	 * @param user : user data
	 * @return the user saved in database
	 */
	UserDTO createUser(UserDTO user);

	/**
	 * Get a user using its user name
	 * @param userName
	 * @return the user found with this username
	 * @throws ResponseStatusException : Bad Request for parameters with invalid formats, Internal Server Error other other issues, not found if no user found
	 */
	ResponseEntity<UserDTO> getUser(String username);

}