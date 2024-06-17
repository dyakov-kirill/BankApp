package ru.dyakov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dyakov.entities.Customer;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer> {
}
