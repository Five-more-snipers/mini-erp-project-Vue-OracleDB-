package com.ham.mini_erp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// @Entity: Marks this class as a JPA entity (maps to a database table)
// @Table: Specifies the table name in the database
// Interview point: "JPA entities are POJOs with annotations that map Java objects to relational database tables"
@Getter
@Setter
@Entity
@Table(name = "ITEM")
public class Item {

    // @Id: Marks this field as the primary key
    // @GeneratedValue: Auto-generates the ID value
    // GenerationType.IDENTITY: Uses database auto-increment (works in Oracle 12c+)
    // For older Oracle: use GenerationType.SEQUENCE with @SequenceGenerator
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column: Maps to a database column
    // nullable = false: NOT NULL constraint at DB level
    // unique = true: UNIQUE constraint - prevents duplicate codes
    @NotBlank(message = "Code is required")
    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Name is required")
    @Column(name = "NAME", nullable = false)
    private String name;

    @Min(value = 0, message = "Price must be positive")
    @Column(name = "PRICE")
    private Long price;

    @Min(value = 0, message = "Quantity must be positive")
    @Column(name = "QUANTITY")
    private Integer quantity;

    // Boolean maps to NUMBER(1) in Oracle (0 = false, 1 = true)
    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
    private String description;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
