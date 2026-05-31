package com.ham.mini_erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ham.mini_erp.entity.Item;

public interface ItemRepository
        extends JpaRepository<Item, Long> {
}