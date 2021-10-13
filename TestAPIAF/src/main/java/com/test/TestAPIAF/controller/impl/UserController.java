package com.test.TestAPIAF.controller.impl;

import java.net.URI;

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

import com.test.TestAPIAF.controller.IUserController;
import com.test.TestAPIAF.dto.UserDTO;
import com.test.TestAPIAF.model.User;
import com.test.TestAPIAF.service.IUserService;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class UserController implements IUserController {
	
	@Autowired IUserService registeredUserService;
	
	/**
	 * Get a user using its user name
	 * @param userName
	 * @return the user found with this username
	 * @throws ResponseStatusException : Bad Request for parameters with invalid formats, Internal Server Error other other issues, not found if no user found
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=400 message="User name invalid")
	 * ApiResponses(code=404 message="Not found")
	 * ApiResponses(code=500 message="Internal error")
	 */
	@Override
	@GetMapping("/users/{username}")
	@ApiResponses(value={
		@ApiResponse(code=200, message="OK", response = UserDTO.class),
		@ApiResponse(code=400, message="User name invalid"),
		@ApiResponse(code=404, message="Not found"),
		@ApiResponse(code=500, message="Internal error")
	})
	public ResponseEntity<UserDTO> getUser(@PathVariable String username){
		User returnUser = null;
		ModelMapper modelMapper = new ModelMapper();
		try {
			returnUser = registeredUserService.getRegisteredUser(username);
		}catch(IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal user name");
		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
		}
		if(returnUser != null) {
			return ResponseEntity.ok().body(modelMapper.map(returnUser, UserDTO.class));
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
	@ApiResponses(value={
		@ApiResponse(code=201, message="Created", response = UserDTO.class),
		@ApiResponse(code=400, message="Invalid user name, phone number, country"),
		@ApiResponse(code=403, message="Country or age does not fit the registration rights settings"),
		@ApiResponse(code=409, message="User already exists"),
		@ApiResponse(code=500, message="Internal error")
	})
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){
		try {
			ModelMapper modelMapper = new ModelMapper();
			User modelUser = modelMapper.map(user, User.class);
			User returnUser = registeredUserService.addRegisteredUser(modelUser);
			/* I let the result unchecked because I had to throw an exception if it's null (save shall not return null). 
			the exception will be caught by the last catch sending the correct status */
			return ResponseEntity.created(URI.create("/users/" + returnUser.getUserName())).body(modelMapper.map(returnUser, UserDTO.class));
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
