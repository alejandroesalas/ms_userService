package com.example.userService.repository;

import com.example.userService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link User} entity.
 * <p>
 * Provides basic CRUD operations and custom queries using Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user if found, or empty if not
     */
    Optional<User> findByEmail(String email);
}
