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
}

