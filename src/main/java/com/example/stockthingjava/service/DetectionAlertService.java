package com.example.stockthingjava.service;

import com.example.stockthingjava.entities.DetectionAlert;
import com.example.stockthingjava.repository.DetectionAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetectionAlertService {

    private final DetectionAlertRepository alertRepo;

    public DetectionAlertService(DetectionAlertRepository alertRepo) {
        this.alertRepo = alertRepo;
    }

    public List<DetectionAlert> getAllAlerts() {
        return alertRepo.findAll();
    }

    public Optional<DetectionAlert> getAlertById(Long id) {
        return alertRepo.findById(id);
    }

    public DetectionAlert saveAlert(DetectionAlert alert) {
        return alertRepo.save(alert);
    }

    public void deleteAlert(Long id) {
        alertRepo.deleteById(id);
    }

    public List<DetectionAlert> getAlertsByRuleId(Long ruleId) {
        return alertRepo.findByRuleId(ruleId);
    }
}
