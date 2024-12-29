package ru.ershov.project.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ershov.project.common.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT c.username FROM User c WHERE c.username=:username")
    Optional<String> findUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
}