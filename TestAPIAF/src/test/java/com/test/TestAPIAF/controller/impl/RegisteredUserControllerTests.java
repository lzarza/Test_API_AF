/**
 * 
 */
package com.test.TestAPIAF.controller.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.TestAPIAF.dto.RegisteredUserDTO;
import com.test.TestAPIAF.model.RegisteredUser;
import com.test.TestAPIAF.model.SettingsCountry;
import com.test.TestAPIAF.repository.RegisteredUserRepository;
import com.test.TestAPIAF.repository.SettingsCountryRepository;

/**
 * @author Laurent
 *
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class RegisteredUserControllerTests {

	RegisteredUserDTO testDTO;
	
	@Autowired private RegisteredUserRepository testUserRepository;
	
	@Autowired private SettingsCountryRepository testCountryRepository;
	
	@Autowired private RegisteredUserController testRegisteredUserController;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Empty everything
		testUserRepository.deleteAll();
		testCountryRepository.deleteAll();
		
		//Create a default DTO for saving cases 
		testDTO = new RegisteredUserDTO();
		testDTO.setBirthDate(LocalDate.of(1985, 4, 24));
		testDTO.setUserName("Tester");
		testDTO.setCountryName("France");
		testDTO.setGender("M");
		testDTO.setPhoneNumber("+33489764253");
		
		//fill country settings
		SettingsCountry testCountry = new SettingsCountry();
		testCountry.setCountryName("France");
		testCountry.setMinimumAge(18);
		testCountryRepository.save(testCountry);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		testUserRepository.deleteAll();
		testCountryRepository.deleteAll();
	}

	/**
	 * Basic test, insert and verify the fields
	 * Expected : ok
	 */
	@Test
	void testCreateUser() {
		try {
			RegisteredUserDTO result = testRegisteredUserController.createUser(testDTO);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testDTO.getBirthDate(),result.getBirthDate());
			assertEquals(testDTO.getCountryName(),result.getCountryName());
			assertEquals(testDTO.getGender(),result.getGender());
			assertEquals(testDTO.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testDTO.getUserName(),result.getUserName());
			
			//Step 2 gender null
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * insert user without gender
	 * Expected : ok
	 */
	@Test
	void testCreateUser_NoGender() {
		try {
			testDTO.setGender(null);
			RegisteredUserDTO result = testRegisteredUserController.createUser(testDTO);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testDTO.getBirthDate(),result.getBirthDate());
			assertEquals(testDTO.getCountryName(),result.getCountryName());
			assertNull(result.getGender());
			assertEquals(testDTO.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testDTO.getUserName(),result.getUserName());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * insert user without phone number
	 * Expected : ok
	 */
	@Test
	void testCreateUser_NoPhoneNumber() {
		try {
			
			testDTO.setPhoneNumber(null);
			RegisteredUserDTO result = testRegisteredUserController.createUser(testDTO);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testDTO.getBirthDate(),result.getBirthDate());
			assertEquals(testDTO.getCountryName(),result.getCountryName());
			assertEquals(testDTO.getGender(),result.getGender());
			assertNull(result.getPhoneNumber());
			assertEquals(testDTO.getUserName(),result.getUserName());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test error case : name is null or empty
	 * Expected : HTTP Status 400 Bad Request
	 */
	@Test
	void testCreateUser_NameNullOrEmpty() {
		
		testDTO.setUserName(null);
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		testDTO.setUserName("");
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test error case : country is null or empty
	 * Expected : HTTP Status 400 Bad Request
	 */
	@Test
	void testCreateUser_CountryNullOrEmpty() {
		//Step 1 : Test null
		testDTO.setCountryName(null);
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		//Step 2 : Test Empty
		testDTO.setCountryName("");
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test error case : country is denied for registration
	 * Expected : HTTP Status 403 Forbidden
	 */
	@Test
	void testCreateUser_CountryNotAccepted() {
		testDTO.setCountryName("Zimbabwe");
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.FORBIDDEN, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test error case : age id denied for registration
	 * Expected : HTTP Status 403 Forbidden
	 */
	@Test
	void testCreateUser_AgeNotAccepted() {
		testDTO.setBirthDate(LocalDate.now().plusYears(1));
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.FORBIDDEN, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
	
	/**
	 * Test error case : malformed phone number
	 * Expected : HTTP Status 400 Bad Request
	 */
	@Test
	void testCreateUser_MalformedPhoneNumber() {
		testDTO.setPhoneNumber("++2456378+");
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test error case : name is null or empty
	 * Expected : HTTP Status 409 Conflict
	 */
	@Test
	void testCreateUser_AlreadyExists() {

		insertTestUser();
		//insert a second time, shall fail with conflict
		try {
			testRegisteredUserController.createUser(testDTO);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.CONFLICT, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Basic test case, get an existing user inserted on test beginning
	 */
	@Test
	void getUserById() {
		insertTestUser();
		//insert a second time, shall fail with conflict
		try {
			testRegisteredUserController.createUser(testDTO);
			ResponseEntity<RegisteredUserDTO> resultEntity = testRegisteredUserController.getUser(testDTO.getUserName());
			assertNotNull(resultEntity);
			RegisteredUserDTO result = resultEntity.getBody();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testDTO.getBirthDate(),result.getBirthDate());
			assertEquals(testDTO.getCountryName(),result.getCountryName());
			assertEquals(testDTO.getGender(),result.getGender());
			assertEquals(testDTO.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testDTO.getUserName(),result.getUserName());
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.CONFLICT, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test result for a non existing user
	 * excepted : Error 404 not found
	 */
	@Test
	void getUserById_NotExist() {
		try {
			testRegisteredUserController.getUser(testDTO.getUserName());
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.NOT_FOUND, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test result for param null or empty
	 * excepted : Error 400 bad request
	 */
	@Test
	void getUserById_ParamNullorEmpty() {
		//Verify string empty
		try {
			testRegisteredUserController.getUser("");
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		//Verify string null
		try {
			testRegisteredUserController.getUser(null);
		} catch (ResponseStatusException rse) {
			assertEquals(HttpStatus.BAD_REQUEST, rse.getStatus());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	//Utility functions
	/**
	 * Convert an object to json string for Mock Web requests
	 * @param obj
	 * @return
	 */
	public String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	/**
	 * Insert the basic dto user into database
	 */
	public void insertTestUser() {
		ModelMapper modelMapper = new ModelMapper();
		RegisteredUser entity = modelMapper.map(testDTO, RegisteredUser.class);
		RegisteredUser result = testUserRepository.save(entity);
		testDTO.setId(result.getId());
	}

}
