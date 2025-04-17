package com.example.stockthingjava.repository;

import com.example.stockthingjava.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    // You can define custom queries here if needed
    List<LogEntry> findBySourceIp(String sourceIp);
}

