package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Instructor;
/**
 *
 * @author alvin
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>  {
    //User findByEmail(String email);
}
