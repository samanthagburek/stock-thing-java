package com.example.stockthingjava.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DetectionAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    private String alertType; // e.g., "Port Scan", "DDoS", etc.
    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "log_entry_id") // foreign key in log entry class-maps to id
    private LogEntry logEntry;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "rule_id") // foreign key in Detection rule class-maps to id
    private DetectionRule rule;
}

