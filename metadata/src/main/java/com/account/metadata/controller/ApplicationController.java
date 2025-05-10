package com.account.metadata.controller;

import com.account.metadata.dto.ApplicationRequest;
import com.account.metadata.dto.ApplicationResponse;
import com.account.metadata.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @Valid @RequestBody ApplicationRequest request) {
        return new ResponseEntity<>(applicationService.createApplication(request), HttpStatus.CREATED);
    }

    @GetMapping("/{appId}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable String appId) {
        return ResponseEntity.ok(applicationService.getApplication(appId));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PutMapping("/{appId}")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @PathVariable String appId,
            @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.updateApplication(appId, request));
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String appId) {
        applicationService.deleteApplication(appId);
        return ResponseEntity.noContent().build();
    }
} 