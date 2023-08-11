package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/Compensation")
    public Compensation create(@RequestBody Compensation comp) {
        LOG.debug("Received Compensation create request for [{}]", comp);

        return compensationService.create(comp);
    }

    @GetMapping("/Compensation/{id}")
    public List<Compensation> read(@PathVariable String id) {
        LOG.debug("Received Compensation read request for id [{}]", id);

        return compensationService.read(id);
    }
}
