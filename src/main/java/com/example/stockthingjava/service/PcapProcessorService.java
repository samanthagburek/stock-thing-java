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
import org.pcap4j.packet.*;
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

    // goes through a .pcap file, parses each packet and logs data into the db
    public void processPcap(File file) throws PcapNativeException, NotOpenException {
        PcapHandle handle = Pcaps.openOffline(file.getAbsolutePath());

        Packet packet;
        while ((packet = handle.getNextPacket()) != null) {
            // Parse basic IP, TCP, UDP, ICMP info, no Ipv6 support currently
            IpV4Packet ip = packet.get(IpV4Packet.class);
            if (ip != null) {
                LogEntry log = new LogEntry();
                log.setSourceIp(ip.getHeader().getSrcAddr().getHostAddress());
                log.setDestinationIp(ip.getHeader().getDstAddr().getHostAddress());
                log.setPacketSize(packet.length());
                log.setTimestamp(LocalDateTime.now());

                // Determine protocol and extract ports or types
                if (packet.contains(TcpPacket.class)) {
                    TcpPacket tcp = packet.get(TcpPacket.class);
                    log.setSourcePort(tcp.getHeader().getSrcPort().valueAsInt());
                    log.setDestinationPort(tcp.getHeader().getDstPort().valueAsInt());
                    log.setProtocol("TCP");

                } else if (packet.contains(UdpPacket.class)) {
                    UdpPacket udp = packet.get(UdpPacket.class);
                    log.setSourcePort(udp.getHeader().getSrcPort().valueAsInt());
                    log.setDestinationPort(udp.getHeader().getDstPort().valueAsInt());
                    log.setProtocol("UDP");

                } else if (packet.contains(IcmpV4CommonPacket.class)) {
                    IcmpV4CommonPacket icmp = packet.get(IcmpV4CommonPacket.class);
                    log.setProtocol("ICMP");
                    log.setSourcePort(null); // type Integer allows these to be null
                    log.setDestinationPort(null);
                    log.setIcmpType(Integer.valueOf(icmp.getHeader().getType().value()));
                    log.setIcmpCode(Integer.valueOf(icmp.getHeader().getCode().value()));
                } else {
                    return; // Skip non-TCP/UDP/ICMP packets
                }

                log = logEntryRepository.save(log);
                runDetectionRules(log);
            }
        }

        handle.close();
    }

    private void runDetectionRules(LogEntry newLog) {
        // goes through all detection rules and filters which ones are enabled
        List<DetectionRule> rules = detectionRuleRepository.findAll()
                .stream().filter(DetectionRule::isEnabled).toList();

        // key part going through each rule and checking if it was violated
        for (DetectionRule rule : rules) {
            // need to add more functions that check these rules
            if ("Rapid Port Scan".equals(rule.getName())) {
                detectPortScan(newLog, rule);
            }
        }
    }

    // only rule implemented currently is detecting a port scan
    private void detectPortScan(LogEntry newLog, DetectionRule rule) {
        // sets time window based on what is set in the rule
        LocalDateTime cutoff = newLog.getTimestamp().minusSeconds(rule.getTimeWindowSeconds());

        // queries db for recent traffic in that cutoff time w/ the same ip address
        List<LogEntry> recent = logEntryRepository.findRecentLogsBySourceIp(
                newLog.getSourceIp(), cutoff);

        // counts the distinct ports touched in that window
        long uniquePorts = recent.stream()
                .map(LogEntry::getDestinationPort)
                .distinct()
                .count();

        // if port overrides threshold then send alert
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

