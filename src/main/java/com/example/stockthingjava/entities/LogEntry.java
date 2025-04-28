package com.example.stockthingjava.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer sourcePort;
    private Integer destinationPort;
    private String protocol; // e.g., TCP/UDP/ICMP
    private LocalDateTime timestamp;
    private int packetSize;
    private Integer icmpType;
    private Integer icmpCode;

    @JsonIgnore
    @OneToMany(mappedBy = "logEntry", cascade = CascadeType.ALL)
    private List<DetectionAlert> alerts;

    // could use Lombok
    public LogEntry() {}
    public Long getId() { return id; }
    public String getSourceIp() { return sourceIp; }
    public Integer getSourcePort() { return sourcePort; }
    public String getDestinationIp() { return destinationIp; }
    public Integer getDestinationPort() { return destinationPort; }
    public String getProtocol() { return protocol; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getPacketSize() { return packetSize; }
    public Integer getIcmpType() { return icmpType; }
    public Integer getIcmpCode() { return icmpCode; }
    public List<DetectionAlert> getAlerts() { return alerts; }
    public void setId(Long id) { this.id = id; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public void setSourcePort(Integer sourcePort) { this.sourcePort = sourcePort; }
    public void setDestinationIp(String destinationIp) { this.destinationIp = destinationIp; }
    public void setDestinationPort(Integer destinationPort) { this.destinationPort = destinationPort; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setAlerts(List<DetectionAlert> alerts) { this.alerts = alerts; }
    public void setPacketSize(int packetSize) { this.packetSize = packetSize; }
    public void setIcmpCode(Integer icmpCode) { this.icmpCode = icmpCode; }
    public void setIcmpType(Integer icmpType) { this.icmpType = icmpType; }
}

