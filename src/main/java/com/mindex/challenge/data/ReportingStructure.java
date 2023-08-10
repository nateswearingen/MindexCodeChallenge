package com.mindex.challenge.data;

public class ReportingStructure {
    private String employeeId;
    private int numberOfReports;
    
    public ReportingStructure() {
    }
    
    public ReportingStructure(String employeeId) {
    	this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName() + "[empID=" + employeeId + ", numReports: " + numberOfReports + "]";
    }
}
