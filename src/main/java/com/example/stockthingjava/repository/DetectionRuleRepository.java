package com.example.stockthingjava.repository;

import com.example.stockthingjava.entities.DetectionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionRuleRepository extends JpaRepository<DetectionRule, Long> {
}
