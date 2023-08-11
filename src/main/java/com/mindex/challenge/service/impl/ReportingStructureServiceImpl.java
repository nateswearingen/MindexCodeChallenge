package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.*;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Override
    public ReportingStructure getReportsCount(String id) {
        LOG.debug("Collecting count of all reports on employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        ReportingStructure reports = new ReportingStructure();

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        else {
            reports.setEmployeeId(id);
        	List<String> reportsIDs = new ArrayList<String>();  //reportsIDs is needed to prevent infinite loops
        	reportsIDs.add(employee.getEmployeeId()); //under no circumstance should an employee be considered to report to themselves
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
            LOG.debug(subordinate.getFirstName() + " " + subordinate.getLastName() + "(" + subID + ") reports to " 
            		+ employee.getFirstName() + " " + employee.getLastName() + "(" + employee.getEmployeeId() + ")");
    		if (!excludingIDs.contains(subID)) {
    			excludingIDs.add(subID);
    			out++;
    			out += countAllReports(employeeRepository.findByEmployeeId(subID), excludingIDs);
    		}
    	}
    	return out;
    }
}
