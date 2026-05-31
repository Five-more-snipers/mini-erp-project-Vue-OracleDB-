package com.ham.mini_erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ham.mini_erp.entity.Item;

public interface ItemRepository
                extends JpaRepository<Item, Long> {

        Optional<Item> findTopByOrderByCodeDesc();

        List<Item> findByActiveTrue();

}