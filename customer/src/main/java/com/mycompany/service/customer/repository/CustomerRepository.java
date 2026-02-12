package com.mycompany.service.customer.repository;

import com.mycompany.service.customer.entity.Customer;
import com.mycompany.service.customer.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmail(String email);

    Page<Customer> findAllByCustomerStatus(CustomerStatus status, Pageable pageable);
    
    String email(String email);
}
