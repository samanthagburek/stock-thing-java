package com.example.stockthingjava.controller;

import com.example.stockthingjava.entities.DetectionAlert;
import com.example.stockthingjava.service.DetectionAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class DetectionAlertController {

    private final DetectionAlertService alertService;

    public DetectionAlertController(DetectionAlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<DetectionAlert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public DetectionAlert getAlert(@PathVariable Long id) {
        return alertService.getAlertById(id).orElseThrow();
    }

    @PostMapping
    public DetectionAlert createAlert(@RequestBody DetectionAlert alert) {
        return alertService.saveAlert(alert);
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }

    @GetMapping("/rule/{ruleId}")
    public List<DetectionAlert> getAlertsByRule(@PathVariable Long ruleId) {
        return alertService.getAlertsByRuleId(ruleId);
    }
}
