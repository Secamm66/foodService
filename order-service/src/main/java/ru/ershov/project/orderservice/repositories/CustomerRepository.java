package ru.ershov.project.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ershov.project.common.models.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c.phone FROM Customer c WHERE c.phone=:phone")
    Optional<String> findCustomerByPhone(@Param("phone") String phone);

    @Query("SELECT c.email FROM Customer c WHERE c.email=:email")
    Optional<String> findCustomerByEmail(@Param("email") String email);
}