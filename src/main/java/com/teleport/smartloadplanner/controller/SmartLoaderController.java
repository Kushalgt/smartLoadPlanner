package com.teleport.smartloadplanner.controller;

import com.teleport.smartloadplanner.dto.LoadOptimizationRequest;
import com.teleport.smartloadplanner.dto.LoadOptimizationResponse;
import com.teleport.smartloadplanner.service.SmartLoaderService;
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
    public ResponseEntity<LoadOptimizationResponse> optimizeLoader(@RequestBody LoadOptimizationRequest request) {
        try {
            return ResponseEntity.ok().body(smartLoaderService.optimizeLoad(request.getTruck(), request.getOrders()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
