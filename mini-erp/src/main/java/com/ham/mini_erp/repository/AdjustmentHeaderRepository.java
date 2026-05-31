package com.ham.mini_erp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ham.mini_erp.entity.AdjustmentHeader;

public interface AdjustmentHeaderRepository extends JpaRepository<AdjustmentHeader, Long> {
    Optional<AdjustmentHeader> findTopByOrderByCodeDesc();
}