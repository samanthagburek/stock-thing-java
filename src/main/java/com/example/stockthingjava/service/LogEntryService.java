package com.example.stockthingjava.service;

import com.example.stockthingjava.entities.DetectionAlert;
import com.example.stockthingjava.entities.DetectionRule;
import com.example.stockthingjava.entities.LogEntry;
import com.example.stockthingjava.repository.DetectionAlertRepository;
import com.example.stockthingjava.repository.DetectionRuleRepository;
import com.example.stockthingjava.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogEntryService {
    private final LogEntryRepository logEntryRepository;
    private final DetectionAlertRepository detectionAlertRepository;

    @Autowired
    public LogEntryService(LogEntryRepository logEntryRepository,
                           DetectionAlertRepository detectionAlertRepository) {
        this.logEntryRepository = logEntryRepository;
        this.detectionAlertRepository = detectionAlertRepository;
    }

    public List<LogEntry> getAllLogs() {
        return logEntryRepository.findAll();
    }

    public LogEntry saveLog(LogEntry entry) {
        return logEntryRepository.save(entry);
    }

    public List<LogEntry> getLogsBySourceIp(String ip) {
        return logEntryRepository.findBySourceIp(ip);
    }

    // method to simulate fake network events
    public void simulateAttack() {
        simulateEvent("10.10.10.5", "192.168.0.8", "TCP", 1337, "Simulated suspicious C2 connection", true);
        simulateEvent("10.10.10.5", "8.8.8.8", "UDP", 53, "Normal DNS request", false);
        simulateEvent("10.10.10.7", "192.168.1.10", "ICMP", null, "Ping Sweep Attempt", true);
    }

    // this will just test adding logEntries and making a fake alert- doesn't test rules
    private void simulateEvent(String srcIp, String dstIp, String protocol, Integer dstPort, String description, boolean malicious) {
        LogEntry log = new LogEntry();
        log.setSourceIp(srcIp);
        log.setDestinationIp(dstIp);
        log.setProtocol(protocol);
        log.setPacketSize(128); // dummy packet size
        log.setTimestamp(LocalDateTime.now());

        if (dstPort != null) {
            log.setDestinationPort(dstPort);
        }

        // Ports are optional in ICMP
        log = logEntryRepository.save(log);

        if (malicious) {
            DetectionAlert alert = new DetectionAlert();
            alert.setLogEntry(log);
            alert.setAlertType("SIMULATED_ATTACK");
            alert.setMessage(description);
            alert.setTimestamp(LocalDateTime.now());

            detectionAlertRepository.save(alert);
        }
    }

    @Autowired
    private DetectionRuleRepository detectionRuleRepository;

    //  this is test code for running attack simulations and testing the rules
    public LogEntry saveAndDetect(LogEntry entry) {
        LogEntry saved = logEntryRepository.save(entry);
        runDetectionRules(saved);
        return saved;
    }

    public void runDetectionRules(LogEntry newLog) {
        List<DetectionRule> rules = detectionRuleRepository.findAll()
                .stream().filter(DetectionRule::isEnabled).toList();

        for (DetectionRule rule : rules) {
            if ("Rapid Port Scan".equals(rule.getName())) {
                detectPortScan(newLog, rule);
            }
            // add more rules later if you want
        }
    }

    private void detectPortScan(LogEntry newLog, DetectionRule rule) {
        LocalDateTime cutoff = newLog.getTimestamp().minusSeconds(rule.getTimeWindowSeconds());

        List<LogEntry> recent = logEntryRepository.findRecentLogsBySourceIp(
                newLog.getSourceIp(), cutoff);

        long uniquePorts = recent.stream()
                .map(LogEntry::getDestinationPort)
                .distinct()
                .count();

        if (uniquePorts >= rule.getThreshold()) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("PORT_SCAN");
            alert.setMessage("Triggered: " + rule.getName());
            alert.setTimestamp(LocalDateTime.now());

            detectionAlertRepository.save(alert);
        }
    }

    private void detectRestrictedPorts(LogEntry newLog, DetectionRule rule) {

    }

}

