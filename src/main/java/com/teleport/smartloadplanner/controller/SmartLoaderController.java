package com.teleport.smartloadplanner.controller;

import com.teleport.smartloadplanner.dto.LoadOptimizationRequest;
import com.teleport.smartloadplanner.service.SmartLoaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load-optimizer")
public class SmartLoaderController {
    @Autowired
    SmartLoaderService smartLoaderService;

    @PostMapping("/optimize")
    public ResponseEntity<?> optimizeLoader(@Valid @RequestBody LoadOptimizationRequest request) {
        try {
            return ResponseEntity.ok().body(smartLoaderService.optimizeLoad(request.getTruck(), request.getOrders()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
