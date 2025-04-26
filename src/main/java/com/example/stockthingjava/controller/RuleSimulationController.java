package com.example.stockthingjava.controller;

import com.example.stockthingjava.entities.DetectionAlert;
import com.example.stockthingjava.entities.LogEntry;
import com.example.stockthingjava.service.DetectionAlertService;
import com.example.stockthingjava.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rulesimulate")
public class RuleSimulationController {

    @Autowired
    private LogEntryService logEntryService;

    @Autowired
    private DetectionAlertService detectionAlertService; // add this

    @PostMapping("/attack")
    public List<DetectionAlert> simulatePortScanAttack() {
        String attackerIp = "10.10.10.5";
        String victimIp = "192.168.0.6";
        // More than 10 ports to exceed the rule's threshold
        int[] portsToScan = {22, 80, 443, 8080, 3306, 21, 53, 1433, 3307, 4444, 25};

        LocalDateTime baseTime = LocalDateTime.now();

        // Generate log entries for all the ports in the array
        for (int i = 0; i < portsToScan.length; i++) {
            int port = portsToScan[i];

            LogEntry scanAttempt = new LogEntry();
            scanAttempt.setSourceIp(attackerIp);
            scanAttempt.setDestinationIp(victimIp);
            scanAttempt.setProtocol("TCP");
            scanAttempt.setSourcePort(5555);
            scanAttempt.setDestinationPort(port);
            scanAttempt.setPacketSize(64);
            scanAttempt.setTimestamp(baseTime); // Use the same timestamp for all logs

            logEntryService.saveLog(scanAttempt);  // Just save, no detect yet
        }

        // After all are saved, trigger detection on the latest log
        List<LogEntry> entries = logEntryService.getLogsBySourceIp(attackerIp);

        if (!entries.isEmpty()) {
            LogEntry finalEntry = entries.get(entries.size() - 1); // Get the latest log entry
            logEntryService.runDetectionRules(finalEntry); // Trigger rule detection
        }

        // Return any triggered alerts
        return detectionAlertService.getAllAlerts();
    }

}

