package com.test.TestAPIAF.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.test.TestAPIAF.model.User;

public interface UserRepository extends CrudRepository<User,Long> {
	public Optional<User> findByUserName(String userName);
}
