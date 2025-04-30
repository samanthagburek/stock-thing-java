package com.example.stockthingjava.controller;

import com.example.stockthingjava.dto.DetectionRulePatchDto;
import com.example.stockthingjava.entities.DetectionRule;
import com.example.stockthingjava.service.DetectionRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class DetectionRuleController {

    private final DetectionRuleService detectionRuleService;

    public DetectionRuleController(DetectionRuleService detectionRuleService) {
        this.detectionRuleService = detectionRuleService;
    }

    @GetMapping
    public List<DetectionRule> getAllRules() {
        return detectionRuleService.getAllRules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetectionRule> getRuleById(@PathVariable Long id) {
        return detectionRuleService.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetectionRule createRule(@RequestBody DetectionRule rule) {
        return detectionRuleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetectionRule> updateRule(@PathVariable Long id, @RequestBody DetectionRule rule) {
        try {
            DetectionRule updated = detectionRuleService.updateRule(id, rule);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        detectionRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DetectionRule> patchRule(@PathVariable Long id, @RequestBody DetectionRulePatchDto patchDto) {
        try {
            DetectionRule updated = detectionRuleService.patchRule(id, patchDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}