package com.example.stockthingjava.service;

import com.example.stockthingjava.entities.DetectionAlert;
import com.example.stockthingjava.entities.DetectionRule;
import com.example.stockthingjava.entities.LogEntry;
import com.example.stockthingjava.repository.DetectionAlertRepository;
import com.example.stockthingjava.repository.DetectionRuleRepository;
import com.example.stockthingjava.repository.LogEntryRepository;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PcapProcessorService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private DetectionRuleRepository detectionRuleRepository;

    @Autowired
    private DetectionAlertRepository detectionAlertRepository;

    public void processPcap(File file) throws PcapNativeException, NotOpenException {
        PcapHandle handle = Pcaps.openOffline(file.getAbsolutePath());

        Packet packet;
        while ((packet = handle.getNextPacket()) != null) {
            // Parse basic IP + TCP info
            IpV4Packet ip = packet.get(IpV4Packet.class);
            TcpPacket tcp = packet.get(TcpPacket.class);

            if (ip != null && tcp != null) {
                LogEntry log = new LogEntry();
                log.setSourceIp(ip.getHeader().getSrcAddr().getHostAddress());
                log.setDestinationIp(ip.getHeader().getDstAddr().getHostAddress());
                log.setSourcePort(tcp.getHeader().getSrcPort().valueAsInt());
                log.setDestinationPort(tcp.getHeader().getDstPort().valueAsInt());
                log.setProtocol("TCP");
                log.setPacketSize(packet.length());
                log.setTimestamp(LocalDateTime.now());

                log = logEntryRepository.save(log); // save & get ID

                runDetectionRules(log);
            }
        }

        handle.close();
    }

    private void runDetectionRules(LogEntry newLog) {
        List<DetectionRule> rules = detectionRuleRepository.findAll()
                .stream().filter(DetectionRule::isEnabled).toList();

        for (DetectionRule rule : rules) {
            if ("Rapid Port Scan".equals(rule.getName())) {
                detectPortScan(newLog, rule);
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
}

