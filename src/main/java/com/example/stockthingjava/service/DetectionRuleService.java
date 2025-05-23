package com.example.stockthingjava.service;

import com.example.stockthingjava.dto.DetectionRulePatchDto;
import com.example.stockthingjava.entities.DetectionRule;
import com.example.stockthingjava.repository.DetectionRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetectionRuleService {

    private final DetectionRuleRepository detectionRuleRepository;

    public DetectionRuleService(DetectionRuleRepository detectionRuleRepository) {
        this.detectionRuleRepository = detectionRuleRepository;
    }

    public List<DetectionRule> getAllRules() {
        return detectionRuleRepository.findAll();
    }

    public Optional<DetectionRule> getRuleById(Long id) {
        return detectionRuleRepository.findById(id);
    }

    public DetectionRule createRule(DetectionRule rule) {
        return detectionRuleRepository.save(rule);
    }

    public DetectionRule updateRule(Long id, DetectionRule updatedRule) {
        return detectionRuleRepository.findById(id)
                .map(rule -> {
                    rule.setName(updatedRule.getName());
                    rule.setDescription(updatedRule.getDescription());
                    rule.setThreshold(updatedRule.getThreshold());
                    rule.setEnabled(updatedRule.isEnabled());
                    return detectionRuleRepository.save(rule);
                })
                .orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public DetectionRule patchRule(Long id, DetectionRulePatchDto patch) {
        return detectionRuleRepository.findById(id)
                .map(rule -> {
                    if (patch.getName() != null) {
                        rule.setName(patch.getName());
                    }
                    if (patch.getDescription() != null) {
                        rule.setDescription(patch.getDescription());
                    }
                    if (patch.getThreshold() != null) {
                        rule.setThreshold(patch.getThreshold());
                    }
                    if (patch.getEnabled() != null) {
                        rule.setEnabled(patch.getEnabled());
                    }
                    return detectionRuleRepository.save(rule);
                })
                .orElseThrow(() -> new RuntimeException("Rule not found"));
    }


    public void deleteRule(Long id) {
        detectionRuleRepository.deleteById(id);
    }
}

