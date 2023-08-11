package com.mindex.challenge.data;

import java.time.*;

public class Compensation {
    private String employeeId;
    private int salary;
	private LocalDate effectiveDate;
    
    public Compensation() {
    	effectiveDate = LocalDate.now();
    }
    
    public Compensation(String employeeId) {
    	this.employeeId = employeeId;
    	this.effectiveDate = LocalDate.now();
    }
    
    public Compensation(String employeeId, int salary, LocalDate effectiveDate) {
    	this.employeeId = employeeId;
    	this.salary = salary;
    	this.effectiveDate = effectiveDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
    @Override
    public String toString() {
    	return getClass().getSimpleName() + "[empID=" + employeeId + ", salary: " + salary + ", effective: " + effectiveDate + "]";
    }
}
