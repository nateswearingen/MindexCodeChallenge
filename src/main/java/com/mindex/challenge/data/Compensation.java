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
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) { return true; }
    	if (!(o instanceof Compensation)) {
    		return false; 
    	}
    	Compensation comp = (Compensation) o;
    	return this.employeeId.equals(comp.getEmployeeId()) && this.salary == comp.getSalary() && this.effectiveDate.equals(comp.getEffectiveDate());
    }
}
