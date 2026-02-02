package com.mycompany.service.customer.repository;

import com.mycompany.service.customer.entity.Customer;
import com.mycompany.service.customer.entity.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository< Customer,UUID>{

    boolean existsByEmail(String email);

    List<Customer> findAllByCustomerStatus(CustomerStatus status);

}
