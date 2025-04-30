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
import java.util.Set;

@Service
public class DetectionEngineService {

    @Autowired
    private DetectionRuleRepository detectionRuleRepository;

    @Autowired
    private DetectionAlertRepository detectionAlertRepository;

    @Autowired
    private LogEntryRepository logEntryRepository;

    public void runDetectionRules(LogEntry newLog) {
        List<DetectionRule> rules = detectionRuleRepository.findAll()
                .stream().filter(DetectionRule::isEnabled).toList();

        for (DetectionRule rule : rules) {
            switch (rule.getName()) {
                case "Rapid Port Scan" -> detectPortScan(newLog, rule);
                case "ICMP Flood" -> detectIcmpFlood(newLog, rule);
                case "IP Sweep" -> detectIpSweep(newLog, rule);
                case "Frequent Connections to Single Port" -> detectFrequentPortHits(newLog, rule);
                case "Large Data Transfer" -> detectLargeDataTransfer(newLog, rule);
                case "Restricted Port Access" -> detectRestrictedPortAccess(newLog, rule);
            }
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

    private void detectIcmpFlood(LogEntry newLog, DetectionRule rule) {
        if (!"ICMP".equals(newLog.getProtocol())) return;

        LocalDateTime cutoff = newLog.getTimestamp().minusSeconds(rule.getTimeWindowSeconds());
        List<LogEntry> recent = logEntryRepository.findRecentLogsBySourceIpAndProtocol(
                newLog.getSourceIp(), "ICMP", cutoff);

        if (recent.size() >= rule.getThreshold()) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("ICMP_FLOOD");
            alert.setMessage("Triggered: " + rule.getName());
            alert.setTimestamp(LocalDateTime.now());
            detectionAlertRepository.save(alert);
        }
    }

    private void detectIpSweep(LogEntry newLog, DetectionRule rule) {
        LocalDateTime cutoff = newLog.getTimestamp().minusSeconds(rule.getTimeWindowSeconds());
        List<LogEntry> recent = logEntryRepository.findRecentLogsBySourceIp(
                newLog.getSourceIp(), cutoff);

        long uniqueDestIps = recent.stream()
                .map(LogEntry::getDestinationIp)
                .distinct()
                .count();

        if (uniqueDestIps >= rule.getThreshold()) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("IP_SWEEP");
            alert.setMessage("Triggered: " + rule.getName());
            alert.setTimestamp(LocalDateTime.now());
            detectionAlertRepository.save(alert);
        }
    }

    private void detectFrequentPortHits(LogEntry newLog, DetectionRule rule) {
        if (newLog.getDestinationPort() == null) return;

        LocalDateTime cutoff = newLog.getTimestamp().minusSeconds(rule.getTimeWindowSeconds());
        List<LogEntry> recent = logEntryRepository.findRecentLogsBySourceIpAndPort(
                newLog.getSourceIp(), newLog.getDestinationPort(), cutoff);

        if (recent.size() >= rule.getThreshold()) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("FREQ_PORT_CONN");
            alert.setMessage("Triggered: " + rule.getName());
            alert.setTimestamp(LocalDateTime.now());
            detectionAlertRepository.save(alert);
        }
    }
    private void detectLargeDataTransfer(LogEntry newLog, DetectionRule rule) {
        if (newLog.getPacketSize() >= rule.getThreshold()) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("LARGE_PACKET");
            alert.setMessage("Large packet detected from " + newLog.getSourceIp() + " to " + newLog.getDestinationIp());
            alert.setTimestamp(LocalDateTime.now());

            detectionAlertRepository.save(alert);
        }
    }

    private static final Set<Integer> RESTRICTED_PORTS = Set.of(23, 3389, 22, 445, 21); // Telnet, RDP, SSH, SMB, FTP
    private void detectRestrictedPortAccess(LogEntry newLog, DetectionRule rule) {
        Integer destPort = newLog.getDestinationPort();
        if (destPort != null && RESTRICTED_PORTS.contains(destPort)) {
            DetectionAlert alert = new DetectionAlert();
            alert.setRule(rule);
            alert.setLogEntry(newLog);
            alert.setAlertType("RESTRICTED_PORTS");
            alert.setMessage("Access to restricted port " + destPort + " from " + newLog.getSourceIp());
            alert.setTimestamp(LocalDateTime.now());

            detectionAlertRepository.save(alert);
        }
    }



}
