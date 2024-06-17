package ru.dyakov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dyakov.entities.Request;

import java.util.Date;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query(value = "INSERT INTO requests (customer_id, request_date) VALUES (:customerId, :requestDate) returning *", nativeQuery = true)
    Request insertRequest(@Param("customerId") Integer customerId,
                       @Param("requestDate") Date requestDate);

}
