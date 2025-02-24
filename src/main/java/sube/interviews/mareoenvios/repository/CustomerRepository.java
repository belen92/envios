package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByAddress(String address);
}