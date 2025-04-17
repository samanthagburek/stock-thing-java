package com.example.stockthingjava.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DetectionAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alertType; // e.g., "Port Scan", "DDoS", etc.
    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "log_entry_id")
    private LogEntry logEntry;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private DetectionRule rule;
}

