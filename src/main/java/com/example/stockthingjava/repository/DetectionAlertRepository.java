package com.example.stockthingjava.repository;

import com.example.stockthingjava.entities.DetectionAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetectionAlertRepository extends JpaRepository<DetectionAlert, Long> {
    List<DetectionAlert> findByAlertType(String alertType);
    List<DetectionAlert> findByRuleId(Long ruleId);
}
