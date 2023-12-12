package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.ReasonCode;
import com.hackathon.Lops.repository.ReasonCodeRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reasonCode")
@CrossOrigin("http://localhost:4200")
public class ReasonCodesController {
    @Autowired
    ReasonCodeRepo reasonCodeRepository;


    @GetMapping
    public List<ReasonCode> getAllReasonCodes() {
        return reasonCodeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ReasonCode> getReasonCodeById(@PathVariable String id) {
        return reasonCodeRepository.findById(id);
    }

    @PostMapping
    public ReasonCode createReasonCode(@RequestBody ReasonCode reasonCode) {
        return reasonCodeRepository.save(reasonCode);
    }

    @PutMapping("/{id}")
    public ReasonCode updateReasonCode(@PathVariable String id, @RequestBody ReasonCode updatedReasonCode) {
        updatedReasonCode.setId(id);
        return reasonCodeRepository.save(updatedReasonCode);
    }

    @DeleteMapping("/{id}")
    public void deleteReasonCode(@PathVariable String id) {
        reasonCodeRepository.deleteById(id);
    }
}
