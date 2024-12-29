package ru.ershov.project.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ershov.project.common.models.Courier;
import ru.ershov.project.common.models.statuses.CourierStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    @Query("SELECT c.id FROM Courier c WHERE c.status=:status")
    List<Long> findAllByStatus(@Param("status") CourierStatus status);

    @Query("SELECT c.coordinates FROM Courier c WHERE c.id=:courierId")
    Optional<String> findCourierCoordinatesById(@Param("courierId") Long courierId);

}