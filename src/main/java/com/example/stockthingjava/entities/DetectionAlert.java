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

    public String getAlertType(){return alertType;}
    public String getMessage(){return message;}
    public LocalDateTime getTimestamp(){return timestamp;}
    public LogEntry getLogEntry(){return logEntry;}
    public DetectionRule getRule(){return rule;}
    public void setId(Long id){ this.id = id; }
    public void setAlertType(String alertType){ this.alertType = alertType; }
    public void setMessage(String message){ this.message = message; }
    public void setTimestamp(LocalDateTime timestamp){ this.timestamp = timestamp; }
    public void setLogEntry(LogEntry logEntry){ this.logEntry = logEntry; }
    public void setRule(DetectionRule rule){ this.rule = rule; }
}

