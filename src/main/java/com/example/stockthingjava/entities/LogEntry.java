package com.example.stockthingjava.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceIp;
    private String destinationIp;
    private Integer sourcePort;
    private Integer destinationPort;
    private String protocol; // e.g., TCP/UDP/ICMP
    private LocalDateTime timestamp;
    private int packetSize;
    private Integer icmpType;
    private Integer icmpCode;

    @JsonManagedReference
    @OneToMany(mappedBy = "logEntry", cascade = CascadeType.ALL)
    private List<DetectionAlert> alerts;
}

