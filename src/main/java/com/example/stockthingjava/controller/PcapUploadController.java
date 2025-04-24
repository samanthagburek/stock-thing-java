package com.example.stockthingjava.controller;

import com.example.stockthingjava.service.PcapProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/pcap")
public class PcapUploadController {

    @Autowired
    private PcapProcessorService pcapProcessorService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPcap(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("upload", ".pcap");
            file.transferTo(tempFile.toFile());

            pcapProcessorService.processPcap(tempFile.toFile());

            return ResponseEntity.ok("PCAP processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

