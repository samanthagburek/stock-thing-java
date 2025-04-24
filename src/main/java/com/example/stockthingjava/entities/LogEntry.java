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
    public int getSourcePort() { return sourcePort; }
    public String getDestinationIp() { return destinationIp; }
    public int getDestinationPort() { return destinationPort; }
    public String getProtocol() { return protocol; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getPacketSize() { return packetSize; }
    public List<DetectionAlert> getAlerts() { return alerts; }
    public void setId(Long id) { this.id = id; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public void setSourcePort(int sourcePort) { this.sourcePort = sourcePort; }
    public void setDestinationIp(String destinationIp) { this.destinationIp = destinationIp; }
    public void setDestinationPort(int destinationPort) { this.destinationPort = destinationPort; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setAlerts(List<DetectionAlert> alerts) { this.alerts = alerts; }
    public void setPacketSize(int packetSize) { this.packetSize = packetSize; }
}

