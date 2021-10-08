package com.test.TestAPIAF.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.test.TestAPIAF.model.SettingsCountry;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SettingsCountryRepositoryTest {

	@Autowired private SettingsCountryRepository testCountryRepository;
	
	SettingsCountry testEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		//empty all settings
		testCountryRepository.deleteAll();
		
		//setup test setting
		testEntity = new SettingsCountry();
		testEntity.setCountryName("France");
		testEntity.setMinimumAge(18);
	}

	@AfterEach
	void tearDown() throws Exception {
		//empty all settings
	}

	/**
	 * Test inserting
	 */
	@Test
	void testSave() {
		SettingsCountry result = testCountryRepository.save(testEntity);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertEquals(testEntity.getMinimumAge(),result.getMinimumAge());
	}
	
	/**
	 * Test find by name
	 */
	@Test
	void testFindByCountryName() {
		testCountryRepository.save(testEntity);
		Optional<SettingsCountry> resultOp = testCountryRepository.findByCountryName(testEntity.getCountryName());
		assertTrue(resultOp.isPresent());
		SettingsCountry result = resultOp.get();
		assertNotNull(result.getId());
		assertEquals(testEntity.getCountryName(),result.getCountryName());
		assertEquals(testEntity.getMinimumAge(),result.getMinimumAge());
	}

}
