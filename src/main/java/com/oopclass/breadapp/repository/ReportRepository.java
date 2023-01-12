package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Report;
/**
 *
 * @author alvin
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long>  {
    //User findByEmail(String email);
}
