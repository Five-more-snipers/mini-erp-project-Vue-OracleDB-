package com.ham.mini_erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ham.mini_erp.entity.Item;

// @Repository: Spring stereotype annotation for DAO layer
// Makes this bean eligible for exception translation (converts JPA exceptions to Spring DataAccessException)
// Interview point: "I use @Repository for data access objects to benefit from Spring's exception translation"
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Spring Data JPA method naming convention:
    // findTopByOrderByCodeDesc = "Find the first record when ordered by Code descending"
    // Used to get the latest code for auto-increment generation
    // No @Query needed - Spring Data parses the method name and generates the SQL automatically
    Optional<Item> findTopByOrderByCodeDesc();

    // findByActiveTrue = "Find all records where active = true"
    // Spring Data JPA supports: IsTrue, IsFalse, IsNull, IsNotNull, Like, Between, etc.
    List<Item> findByActiveTrue();
}
