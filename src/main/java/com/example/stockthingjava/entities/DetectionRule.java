package com.example.stockthingjava.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class DetectionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;             // "Rapid Port Scan"
    private String description;      // "10+ unique ports in 1 second"
    private String ruleLogic;        // Optional: expression or config string
    private boolean enabled;

    private int threshold;           // Optional: e.g., 10 connections
    private int timeWindowSeconds;   // e.g., 1 second window

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<DetectionAlert> alerts;


    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getRuleLogic() { return ruleLogic; }
    public boolean isEnabled() { return enabled; }
    public int getThreshold() { return threshold; }
    public int getTimeWindowSeconds() { return timeWindowSeconds; }
    public List<DetectionAlert> getAlerts() { return alerts; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setRuleLogic(String ruleLogic) { this.ruleLogic = ruleLogic; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setThreshold(int threshold) { this.threshold = threshold; }
}

