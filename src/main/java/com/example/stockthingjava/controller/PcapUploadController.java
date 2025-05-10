package com.example.stockthingjava.controller;

import com.example.stockthingjava.service.PcapProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/pcap")
public class PcapUploadController {

    @Autowired
    private PcapProcessorService pcapProcessorService;

    // Now accept multipart/form-data file upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPcap(@RequestParam("file") MultipartFile multipartFile) {
        try {
            System.out.println(">>> Received multipart file: " + multipartFile.getOriginalFilename());

            // Save uploaded content to a temp file
            Path tempFile = Files.createTempFile("upload_", ".pcap");
            multipartFile.transferTo(tempFile);

            // Process the file
            System.out.println("\n\t"+tempFile.toString()+ " is the file we are trying to process right now\n\n");
            pcapProcessorService.processPcap(tempFile.toFile());

            return ResponseEntity.ok("PCAP processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("There Is An Error!!! " + e.getMessage());
        }
    }
}
