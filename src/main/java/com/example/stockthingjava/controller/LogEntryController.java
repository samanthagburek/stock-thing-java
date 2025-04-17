package com.example.stockthingjava.controller;

import com.example.stockthingjava.entities.LogEntry;
import com.example.stockthingjava.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogEntryController {
    private final LogEntryService logEntryService;

    @Autowired
    public LogEntryController(LogEntryService logEntryService) {
        this.logEntryService = logEntryService;
    }

    @GetMapping
    public List<LogEntry> getAllLogs() {
        return logEntryService.getAllLogs();
    }

    @PostMapping
    public LogEntry addLog(@RequestBody LogEntry logEntry) {
        return logEntryService.saveLog(logEntry);
    }

    @GetMapping("/source/{ip}")
    public List<LogEntry> getLogsBySourceIp(@PathVariable String ip) {
        return logEntryService.getLogsBySourceIp(ip);
    }
}
