package com.example.stockthingjava.service;

import com.example.stockthingjava.entities.LogEntry;
import com.example.stockthingjava.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogEntryService {
    private final LogEntryRepository logEntryRepository;

    @Autowired
    public LogEntryService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
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
}

