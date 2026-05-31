package com.ham.mini_erp.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ham.mini_erp.entity.AdjustmentDetail;

public interface AdjustmentDetailRepository
        extends JpaRepository<AdjustmentDetail, Long> {

    List<AdjustmentDetail> findByHeaderCode(String headerCode);

}