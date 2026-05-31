package com.ham.mini_erp.entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}