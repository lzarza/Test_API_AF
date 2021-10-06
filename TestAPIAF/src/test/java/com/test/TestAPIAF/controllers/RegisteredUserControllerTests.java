/**
 * 
 */
package com.test.TestAPIAF.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.TestAPIAF.dtos.RegisteredUserDTO;
import com.test.TestAPIAF.model.RegisteredUser;
import com.test.TestAPIAF.model.SettingsCountry;
import com.test.TestAPIAF.repositories.RegisteredUserRepository;
import com.test.TestAPIAF.repositories.SettingsCountryRepository;

/**
 * @author Laurent
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@WebMvcTest(RegisteredUserController.class)
class RegisteredUserControllerTests {

	RegisteredUserDTO testDTO;
	
	@Autowired private MockMvc mvc;
	
	@Autowired private RegisteredUserRepository testUserRepository;
	
	@Autowired private SettingsCountryRepository testCountryRepository;
	
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
	 * Expected :
	 * - User insertion with all data set are ok
	 * - User insertion without gender is ok
	 * - User insertion without phone number is ok
	 */
	@Test
	void testCreateUser() {
		try {
			
			//Step 1 global verification
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", LocalDate.class).value(testDTO.getBirthDate()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userName" ).value(testDTO.getUserName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.countryName").value(testDTO.getCountryName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(testDTO.getGender()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(testDTO.getPhoneNumber()));
			//Step 2 gender null
			testUserRepository.deleteAll();
			testDTO.setGender(null);
			mvc.perform( MockMvcRequestBuilders
					.post("/users")
					.content(asJsonString(testDTO))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
					.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", LocalDate.class).value(testDTO.getBirthDate()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.userName" ).value(testDTO.getUserName()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.countryName").value(testDTO.getCountryName()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.gender").doesNotExist())
					.andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(testDTO.getPhoneNumber()));
			//Step 3 phone number null
			testUserRepository.deleteAll();
			testDTO.setPhoneNumber(null);
			mvc.perform( MockMvcRequestBuilders
					.post("/users")
					.content(asJsonString(testDTO))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
					.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate", LocalDate.class).value(testDTO.getBirthDate()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.userName" ).value(testDTO.getUserName()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.countryName").value(testDTO.getCountryName()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.gender").doesNotExist())
					.andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").doesNotExist());
			
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
			testDTO.setUserName(null);
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
			//Step 2 : Test Empty
			testDTO.setUserName("");
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
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
			testDTO.setCountryName(null);
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
			//Step 2 : Test Empty
			testDTO.setCountryName("");
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
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
			testDTO.setCountryName("Zimbabwe");
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
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
			testDTO.setBirthDate(LocalDate.now().plusYears(1));
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
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
			testDTO.setPhoneNumber("++2456378+");
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
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
			insertTestUser();
			//insert a second time, shall fail with conflict
			mvc.perform( MockMvcRequestBuilders
				.post("/users")
				.content(asJsonString(testDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict());
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
			insertTestUser();
			mvc.perform( MockMvcRequestBuilders
				      .get("/users/{username}", testDTO.getUserName())
				      .accept(MediaType.APPLICATION_JSON))
				      .andDo(MockMvcResultHandlers.print())
				      .andExpect(MockMvcResultMatchers.status().isOk())
				      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testDTO.getId()));
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
			mvc.perform( MockMvcRequestBuilders
				      .get("/users/{username}", testDTO.getUserName())
				      .accept(MediaType.APPLICATION_JSON))
				      .andDo(MockMvcResultHandlers.print())
				      .andExpect(MockMvcResultMatchers.status().isNotFound());
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
			//Verify empty string
			mvc.perform( MockMvcRequestBuilders
				      .get("/users/{username}", "")
				      .accept(MediaType.APPLICATION_JSON))
				      .andDo(MockMvcResultHandlers.print())
				      .andExpect(MockMvcResultMatchers.status().isBadRequest());
			//Verify null string
			mvc.perform( MockMvcRequestBuilders
				      .get("/users/{username}", "")
				      .accept(MediaType.APPLICATION_JSON))
				      .andDo(MockMvcResultHandlers.print())
				      .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
