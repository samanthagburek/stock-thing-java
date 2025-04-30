package com.example.stockthingjava.repository;

import com.example.stockthingjava.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    // You can define custom queries here if needed
    List<LogEntry> findBySourceIp(String sourceIp);

    @Query("SELECT l FROM LogEntry l WHERE l.sourceIp = :ip AND l.timestamp >= :cutoff")
    List<LogEntry> findRecentLogsBySourceIp(@Param("ip") String ip, @Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT l FROM LogEntry l WHERE l.sourceIp = :sourceIp AND l.protocol = :protocol AND l.timestamp >= :cutoff")
    List<LogEntry> findRecentLogsBySourceIpAndProtocol(@Param("sourceIp") String sourceIp, @Param("protocol") String protocol, @Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT l FROM LogEntry l WHERE l.sourceIp = :sourceIp AND l.destinationPort = :port AND l.timestamp >= :cutoff")
    List<LogEntry> findRecentLogsBySourceIpAndPort(@Param("sourceIp") String sourceIp, @Param("port") Integer port, @Param("cutoff") LocalDateTime cutoff);


}

