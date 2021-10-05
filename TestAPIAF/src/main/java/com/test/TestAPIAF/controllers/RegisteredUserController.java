package com.test.TestAPIAF.controllers;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletResponse;

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

import com.test.TestAPIAF.services.IRegisteredUserService;

import javassist.NotFoundException;

import com.test.TestAPIAF.model.RegisteredUser;

@RestController
@RequestMapping("/api/v1")
public class RegisteredUserController {
	
	@Autowired IRegisteredUserService registeredUserService;
	
	@GetMapping("/users/{username}")
	public ResponseEntity<RegisteredUser> getUsersById(@PathVariable(value = "username") String userName, HttpServletResponse response){
		try {
			return ResponseEntity.ok().body(registeredUserService.getRegisteredUser(userName));
		}catch(IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal user name", iae);
		}catch(NotFoundException nfe) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", nfe);
		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex);
		}
		
	}
	
	@PostMapping("/users")
	public RegisteredUser createUser(@RequestBody RegisteredUser user, HttpServletResponse response){
		try {
			return registeredUserService.addRegisteredUser(user);
		}catch(IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getLocalizedMessage(), iae);
		}catch(InstanceAlreadyExistsException iaee) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists", iaee);
		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex);
		}
	}
}
