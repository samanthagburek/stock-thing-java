package com.example.stockthingjava.controller;

import com.example.stockthingjava.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {

    @Autowired
    private LogEntryService logEntryService;

    @PostMapping
    public ResponseEntity<String> triggerSimulation() {
        logEntryService.simulateAttack();
        return ResponseEntity.ok("Simulation triggered.");
    }
}

