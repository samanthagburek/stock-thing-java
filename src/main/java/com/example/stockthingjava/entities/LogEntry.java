package com.example.stockthingjava.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceIp;
    private String destinationIp;
    private int sourcePort;
    private int destinationPort;
    private String protocol; // e.g., TCP/UDP/ICMP
    private LocalDateTime timestamp;
    private int packetSize;

    @OneToMany(mappedBy = "logEntry", cascade = CascadeType.ALL)
    private List<DetectionAlert> alerts;

    public Long getId() { return id; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public int getDestinationPort() { return destinationPort; }
    public String getProtocol() { return protocol; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

