package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation for employeeID [{}]", compensation);
        
    	compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public List<Compensation> read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        List<Compensation> compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null || compensation.size() < 1) {
            LOG.warn("Unable to find compensation for employeeId: " + id);
        }

        return compensation;
    }
}
