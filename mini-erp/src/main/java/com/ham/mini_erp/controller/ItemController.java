package com.ham.mini_erp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.ham.mini_erp.entity.Item;
import com.ham.mini_erp.repository.ItemRepository;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    // CRUD - READ
    @GetMapping
    public List<Item> getAll() {
        return repository.findAll();
    }

    // CRUD - CREATE
    @PostMapping
    public Item create(@RequestBody Item item) {

        item.setCreatedDate(
                LocalDateTime.now());

        return repository.save(item);
    }

    // CRUD - UPDATE
    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) {
        item.setId(id);
        return repository.save(item);
    }

    // CRUD - DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}