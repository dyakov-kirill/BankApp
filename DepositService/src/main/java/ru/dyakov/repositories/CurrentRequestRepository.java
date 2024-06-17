package ru.dyakov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.dyakov.entities.CurrentRequestStatus;
import ru.dyakov.entities.Request;

import java.util.Date;
import java.util.List;

public interface CurrentRequestRepository extends JpaRepository<CurrentRequestStatus, Integer> {
    List<CurrentRequestStatus> findByRequestStatusId(Integer requestStatus);

    @Query(value = "INSERT INTO current_request_status (request_id, request_status_id, change_datetime) VALUES (:requestId, :requestStatusId, :changeDatetime)", nativeQuery = true)
    void insertRequest(@Param("requestId") Integer requestId,
                       @Param("requestStatusId") Integer requestStatusId,
                       @Param("changeDatetime") Date changeDatetime);
}
