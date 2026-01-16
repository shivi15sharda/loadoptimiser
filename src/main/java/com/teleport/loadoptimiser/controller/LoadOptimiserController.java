package com.teleport.loadoptimiser.controller;

import com.teleport.loadoptimiser.request.LoadOptimiseRequest;
import com.teleport.loadoptimiser.response.LoadOptimiseResponse;
import com.teleport.loadoptimiser.service.ILoadOptimiserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/load-optimiser")
public class LoadOptimiserController {

    private final ILoadOptimiserService loadOptimiserService;

    public LoadOptimiserController(ILoadOptimiserService service) {
        this.loadOptimiserService = service;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/optimise")
    public LoadOptimiseResponse optimize(@RequestBody LoadOptimiseRequest request) {
        return loadOptimiserService.optimiseLoad(request.truck, request.orders);
    }
}
