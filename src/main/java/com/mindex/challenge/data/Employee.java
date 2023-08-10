package com.mindex.challenge.data;

import java.util.List;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;

public class Employee {
	@Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }
    
    public boolean addDirectReport(Employee subordinate) {
    	if (this.directReports == null) { this.directReports = new ArrayList<Employee>(); }
    	if (this.directReports.contains(subordinate)) { return false; } 
    	return this.directReports.add(subordinate);
    	
    }
    
    public boolean removeDirectReport(Employee subordinate) {
    	if (!this.directReports.contains(subordinate)) { return false; }
    	return this.directReports.remove(subordinate);
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName() + "[ID: " + this.employeeId 
    	   + ", First: " + this.firstName
    	   + ", Last: " + this.lastName
    	   + ", Position: " + this.position
    	   + ", Department: " + this.department
    	   + ", Reports: " + this.directReports
    	   + "]";
    }
}
