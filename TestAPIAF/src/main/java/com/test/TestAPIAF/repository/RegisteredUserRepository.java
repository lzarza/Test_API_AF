package com.test.TestAPIAF.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.test.TestAPIAF.model.RegisteredUser;

public interface RegisteredUserRepository extends CrudRepository<RegisteredUser,Long> {
	public Optional<RegisteredUser> findByUserName(String userName);
}
