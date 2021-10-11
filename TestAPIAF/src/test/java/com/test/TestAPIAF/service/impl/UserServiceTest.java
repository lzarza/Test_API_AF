/**
 * 
 */
package com.test.TestAPIAF.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.management.InstanceAlreadyExistsException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.test.TestAPIAF.model.User;
import com.test.TestAPIAF.model.SettingsCountry;
import com.test.TestAPIAF.repository.UserRepository;
import com.test.TestAPIAF.repository.SettingsCountryRepository;

/**
 * @author Laurent
 *
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserServiceTest {

	User testEntity;
	
	@Autowired private UserRepository testUserRepository;
	
	@Autowired private SettingsCountryRepository testCountryRepository;
	
	@Autowired private UserService testRegisteredUserService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Empty everything
		testUserRepository.deleteAll();
		testCountryRepository.deleteAll();
		
		//Create a default DTO for saving cases 
		testEntity = new User();
		testEntity.setBirthDate(LocalDate.of(1985, 4, 24));
		testEntity.setUserName("Tester");
		testEntity.setCountryName("France");
		testEntity.setGender("M");
		testEntity.setPhoneNumber("+33489764253");
		
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
	}

	/**
	 * Basic test, insert and verify the fields
	 * Expected :
	 * - User insertion with all data set are ok
	 * - User insertion without gender is ok
	 * - User insertion without phone number is ok
	 */
	@Test
	void testCreateUser() {
		try {
			User result = null;
			//Step 1 : all data
			result = testRegisteredUserService.addRegisteredUser(testEntity);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testEntity.getBirthDate(),result.getBirthDate());
			assertEquals(testEntity.getCountryName(),result.getCountryName());
			assertEquals(testEntity.getGender(),result.getGender());
			assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testEntity.getUserName(),result.getUserName());
			
			//Step 2 gender null
			testUserRepository.deleteAll();
			result = null;
			testEntity.setGender(null);
			result = testRegisteredUserService.addRegisteredUser(testEntity);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testEntity.getBirthDate(),result.getBirthDate());
			assertEquals(testEntity.getCountryName(),result.getCountryName());
			assertNull(result.getGender());
			assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testEntity.getUserName(),result.getUserName());
			
			//Step 3 phone number null
			testUserRepository.deleteAll();
			result = null;
			testEntity.setPhoneNumber(null);
			result = testRegisteredUserService.addRegisteredUser(testEntity);
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testEntity.getBirthDate(),result.getBirthDate());
			assertEquals(testEntity.getCountryName(),result.getCountryName());
			assertNull(result.getGender());
			assertNull(result.getPhoneNumber());
			assertEquals(testEntity.getUserName(),result.getUserName());
			
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
		try {
			//Step 1 : Test null
			testEntity.setUserName(null);
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalArgumentException iae) {
			//do nothing, we expected it
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			//Step 2 : Test empty
			testEntity.setUserName("");
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalArgumentException iae) {
			//do nothing, we expected it
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
		try {
			//Step 1 : Test null
			testEntity.setCountryName(null);
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalArgumentException iae) {
			//do nothing, we expected it
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			//Step 2 : Test empty
			testEntity.setCountryName("");
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalArgumentException iae) {
			//do nothing, we expected it
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
		try {
			testEntity.setCountryName("Zimbabwe");
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalStateException ise) {
			//do nothing, we expected it
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
		try {
			testEntity.setBirthDate(LocalDate.now().plusYears(1));
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalStateException ise) {
			//do nothing, we expected it
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
		try {
			testEntity.setPhoneNumber("++2456378+");
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (IllegalArgumentException iae) {
			//do nothing, we expected it
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
		try {
			testUserRepository.save(testEntity);
			testRegisteredUserService.addRegisteredUser(testEntity);
		} catch (InstanceAlreadyExistsException iae) {
			//do nothing, we expected it
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Basic test case, get an existing user inserted on test beginning
	 */
	@Test
	void getUserById() {
		try {
			testEntity = testUserRepository.save(testEntity);
			User result = testRegisteredUserService.getRegisteredUser(testEntity.getUserName());
			assertNotNull(result);
			assertNotNull(result.getId());
			assertEquals(testEntity.getId(),result.getId());
			assertEquals(testEntity.getBirthDate(),result.getBirthDate());
			assertEquals(testEntity.getCountryName(),result.getCountryName());
			assertEquals(testEntity.getGender(),result.getGender());
			assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
			assertEquals(testEntity.getUserName(),result.getUserName());
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
			User result = testRegisteredUserService.getRegisteredUser(testEntity.getUserName());
			assertNull(result);
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
		try {
			testRegisteredUserService.getRegisteredUser("");
			fail("Shall have thrown an exception");
		} catch (IllegalArgumentException iae) {
			return; //expected result
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			testRegisteredUserService.getRegisteredUser(null);
			fail("Shall have thrown an exception");
		} catch (IllegalArgumentException iae) {
			return; //expected result
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	


}
