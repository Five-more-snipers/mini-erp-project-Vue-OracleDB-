package com.ham.mini_erp.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ham.mini_erp.entity.AdjustmentHeader;
import com.ham.mini_erp.repository.AdjustmentHeaderRepository;

@RestController
@RequestMapping("/api/adjustments")
public class AdjustmentHeaderController {

    private final AdjustmentHeaderRepository repository;

    public AdjustmentHeaderController(
            AdjustmentHeaderRepository repository) {

        this.repository = repository;
    }

    @GetMapping
    public List<AdjustmentHeader> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public AdjustmentHeader create(
            @RequestBody AdjustmentHeader header) {

        header.setCode(generateCode());

        header.setStatus("PENDING");

        if (header.getTransactionDate() == null) {
            header.setTransactionDate(
                    LocalDate.now());
        }

        return repository.save(header);
    }

    private String generateCode() {

        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        String prefix = "ADJ-" + datePart + "-";

        long nextNo = 1;

        List<AdjustmentHeader> headers = repository.findAll();

        for (AdjustmentHeader h : headers) {

            if (h.getCode() != null &&
                    h.getCode().startsWith(prefix)) {

                String running = h.getCode()
                        .substring(prefix.length());

                long current = Long.parseLong(running);

                if (current >= nextNo) {
                    nextNo = current + 1;
                }
            }
        }

        return prefix + String.format("%04d", nextNo);
    }
}