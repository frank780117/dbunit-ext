package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findByLastName(String lastName);
  
}
