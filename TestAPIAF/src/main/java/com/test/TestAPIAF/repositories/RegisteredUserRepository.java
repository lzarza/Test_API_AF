package com.test.TestAPIAF.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.test.TestAPIAF.model.RegisteredUser;

public interface RegisteredUserRepository extends CrudRepository<RegisteredUser,Long> {
	public Optional<RegisteredUser> findByUserName(String userName);
}
