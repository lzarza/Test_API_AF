package com.test.TestAPIAF.controller.impl;

import javax.management.InstanceAlreadyExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.test.TestAPIAF.controller.IRegisteredUserController;
import com.test.TestAPIAF.dto.RegisteredUserDTO;
import com.test.TestAPIAF.model.RegisteredUser;
import com.test.TestAPIAF.service.IRegisteredUserService;

@RestController
@RequestMapping("/api/v1")
public class RegisteredUserController implements IRegisteredUserController {
	
	@Autowired IRegisteredUserService registeredUserService;
	
	/**
	 * Get a user using its user name
	 * @param userName
	 * @return the user found with this username
	 * @throws ResponseStatusException : Bad Request for parameters with invalid formats, Internal Server Error other other issues, not found if no user found
	 */
	@Override
	@GetMapping("/users/{username}")
	public ResponseEntity<RegisteredUserDTO> getUser(@PathVariable String username){
		RegisteredUser returnUser = null;
		ModelMapper modelMapper = new ModelMapper();
		try {
			returnUser = registeredUserService.getRegisteredUser(username);
		}catch(IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal user name");
		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
		}
		if(returnUser != null) {
			return ResponseEntity.ok().body(modelMapper.map(returnUser, RegisteredUserDTO.class));
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
	}
	
	/**
	 * Save a user into database if all values are set and correct
	 * @param user : user data
	 * @return the user saved in database
	 * @throws ResponseStatusException : Bad Request for parameters with invalid formats, Forbidden for invalid parameter, Conflict for already existing member, Internal Error for other issues
	 */
	@Override
	@PostMapping("/users")
	public RegisteredUserDTO createUser(@RequestBody RegisteredUserDTO user){
		try {
			ModelMapper modelMapper = new ModelMapper();
			RegisteredUser modelUser = modelMapper.map(user, RegisteredUser.class);
			RegisteredUser returnUser = registeredUserService.addRegisteredUser(modelUser);
			/* I let the result unchecked because I had to throw an exception if it's null (save shall not return null). 
			the exception will be caught by the last catch sending the correct status */
			return modelMapper.map(returnUser, RegisteredUserDTO.class);
		}catch(IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getLocalizedMessage());
		}catch(IllegalStateException ise) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getLocalizedMessage());
		}catch(InstanceAlreadyExistsException iaee) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
		}
	}
}
