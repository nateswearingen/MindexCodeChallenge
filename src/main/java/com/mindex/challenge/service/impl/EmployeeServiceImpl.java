package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.*;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    
    @Override
    public ReportingStructure getReportsCount(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        ReportingStructure reports = new ReportingStructure(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        else {
        	List<String> reportsIDs = new ArrayList<String>();  //reportsIDs is needed to prevent infinite loops
        	reports.setNumberOfReports(countAllReports(employee, reportsIDs));
        }

        return reports;
    }
    
    private int countAllReports(Employee employee, List<String> excludingIDs) {
    	/* Depth-first search of reporting structure to count all reports, both direct and indirect.
    	 * Requires a list of visited nodes to prevent infinite loops, 
    	 * since cycles are not being checked for and prevented on insertion
    	 * */
    	int out = 0;
    	List<Employee> subordinates = employee.getDirectReports();
    	if (subordinates == null) { return out; }
    	for (Employee subordinate : subordinates) {
    		String subID = subordinate.getEmployeeId();
    		if (!excludingIDs.contains(subID)) {
    			excludingIDs.add(subID);
    			out++;
    			out += countAllReports(subordinate, excludingIDs);
    		}
    	}
    	return out;
    }
}
