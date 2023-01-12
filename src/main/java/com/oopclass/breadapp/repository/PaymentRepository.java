package com.oopclass.breadapp.repository;
import com.oopclass.breadapp.models.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Payment;
/**
 *
 * @author alvin
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>  {

    //User findByEmail(String email);
}
    
