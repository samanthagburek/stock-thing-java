package com.example.stockthingjava.repository;

import com.example.stockthingjava.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    // You can define custom queries here if needed
    List<LogEntry> findBySourceIp(String sourceIp);

    @Query("SELECT l FROM LogEntry l WHERE l.sourceIp = :ip AND l.timestamp >= :cutoff")
    List<LogEntry> findRecentLogsBySourceIp(@Param("ip") String ip, @Param("cutoff") LocalDateTime cutoff);

}

