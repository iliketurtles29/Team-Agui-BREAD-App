package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.User;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//User findByEmail(String email);
}
