/**
 * 
 */
package com.test.TestAPIAF.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.test.TestAPIAF.model.RegisteredUser;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class RegisteredUserRepositoryTest {

	RegisteredUser testEntity;
	
	@Autowired private RegisteredUserRepository testUserRepository;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		//empty database
		testUserRepository.deleteAll();
		
		//set up an user
		testEntity = new RegisteredUser();
		testEntity.setBirthDate(LocalDate.of(1985, 4, 24));
		testEntity.setUserName("Tester");
		testEntity.setCountryName("France");
		testEntity.setGender("M");
		testEntity.setPhoneNumber("+33489764253");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		testUserRepository.deleteAll();
	}

	/**
	 * Test inserting
	 */
	@Test
	void testSave() {
		RegisteredUser result = testUserRepository.save(testEntity);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(testEntity.getBirthDate(),result.getBirthDate());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertEquals(testEntity.getGender(),result.getGender());
		assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
		assertEquals(testEntity.getUserName(),result.getUserName());
	}
	
	/**
	 * Test inserting without gender
	 */
	@Test
	void testSave_GenderNull() {
		testEntity.setGender(null);
		RegisteredUser result = testUserRepository.save(testEntity);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(testEntity.getBirthDate(),result.getBirthDate());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertNull(result.getGender());
		assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
		assertEquals(testEntity.getUserName(),result.getUserName());
	}
	
	/**
	 * Test inserting without phone number
	 */
	@Test
	void testSave_PhoneNumberNull() {
		testEntity.setPhoneNumber(null);
		RegisteredUser result = testUserRepository.save(testEntity);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(testEntity.getBirthDate(),result.getBirthDate());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertEquals(testEntity.getGender(),result.getGender());
		assertNull(result.getPhoneNumber());
		assertEquals(testEntity.getUserName(),result.getUserName());
	}
	
	/**
	 * Test find by name
	 */
	@Test
	void testFindByUserName() {
		testUserRepository.save(testEntity);
		Optional<RegisteredUser> resultOp = testUserRepository.findByUserName(testEntity.getUserName());
		assertTrue(resultOp.isPresent());
		RegisteredUser result = resultOp.get();
		assertNotNull(result.getId());
		assertEquals(testEntity.getBirthDate(),result.getBirthDate());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertEquals(testEntity.getGender(),result.getGender());
		assertEquals(testEntity.getPhoneNumber(),result.getPhoneNumber());
		assertEquals(testEntity.getUserName(),result.getUserName());
	}

}
