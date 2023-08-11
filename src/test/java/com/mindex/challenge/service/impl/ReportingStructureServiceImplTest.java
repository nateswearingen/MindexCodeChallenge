package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.*;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportsURL;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportsURL = "http://localhost:" + port + "/reports_count/{id}";
    }
    
    @Test
    public void testReportsCount_happy_path() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("Alpha");
        testEmployee1.setLastName("Happy_Path");
        testEmployee1.setDepartment("dpt");
        testEmployee1.setPosition("pos");

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Beta");
        testEmployee2.setLastName("Happy_Path");
        testEmployee2.setDepartment("dpt");
        testEmployee2.setPosition("pos");
        
        Employee testEmployee3 = new Employee();
        testEmployee3.setFirstName("Gamma");
        testEmployee3.setLastName("Happy_Path");
        testEmployee3.setDepartment("dpt");
        testEmployee3.setPosition("pos");
        

        Employee testEmployee4 = new Employee();
        testEmployee3.setFirstName("Delta");
        testEmployee3.setLastName("Happy_Path");
        testEmployee3.setDepartment("dpt");
        testEmployee3.setPosition("pos");
        
        Employee testEmployee5 = new Employee();
        testEmployee3.setFirstName("Epsilon");
        testEmployee3.setLastName("Happy_Path");
        testEmployee3.setDepartment("dpt");
        testEmployee3.setPosition("pos");

        
        /**test structure is going to be:
         * 
         * 1
         * 2
         * 3, 4
         *  , 5
         */
        
        /*Insert the bottom of the totem pole, get back the ID*/
        testEmployee5 = restTemplate.postForEntity(employeeUrl, testEmployee5, Employee.class).getBody();
        
        /*now that we have the id, assign to the next level up*/
        assertTrue("addDirectReport function failed for testEmployee4", testEmployee4.addDirectReport(testEmployee5));
        assertNotNull("testEmployee4 has null direct reports", testEmployee4.getDirectReports());
        assertEquals("testEmployee4 has incorrect number of direct reports", testEmployee4.getDirectReports().size(), 1);
        
        testEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();
        
        testEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        
        //3 and 4 both report to 2
        assertTrue("addDirectReport function failed for testEmployee2 (emp3)", testEmployee2.addDirectReport(testEmployee3));
        assertTrue("addDirectReport function failed for testEmployee2 (emp4)", testEmployee2.addDirectReport(testEmployee4));
        assertEquals("testEmployee2 has incorrect number of direct reports", testEmployee2.getDirectReports().size(), 2);
        
        testEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        
        //and emp2 reports to employee 1
        assertTrue("addDirectReport function failed for testEmployee1", testEmployee1.addDirectReport(testEmployee2));
        testEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        
        //Now to check out the reports function
        ReportingStructure reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee1.getEmployeeId()).getBody();
        assertNotNull(reports);
        assertNotNull("reports.getEmployeeId() returned a null value for testEmployee1", reports.getEmployeeId());
        assertEquals("reports employeeID does not match expected testEmployee1's ID", reports.getEmployeeId(), testEmployee1.getEmployeeId());
        assertNotNull("reports.getNumberOfReports() returned a null value for testEmployee1", reports.getNumberOfReports());
        assertEquals("reports.getNumberOfReports() returned the wrong number of reports for testEmployee1", reports.getNumberOfReports(), 4);
        
        reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee2.getEmployeeId()).getBody();
        assertEquals("reports employeeID does not match expected testEmployee2's ID", reports.getEmployeeId(), testEmployee2.getEmployeeId());
        assertEquals("reports.getNumberOfReports() returned the wrong number of reports for testEmployee2", reports.getNumberOfReports(), 3);
        
        reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee3.getEmployeeId()).getBody();
        assertNotNull(reports);
        assertNotNull("reports.getEmployeeId() returned a null value for testEmployee3", reports.getEmployeeId());
        assertEquals("reports employeeID does not match expected testEmployee3's ID", reports.getEmployeeId(), testEmployee3.getEmployeeId());
        assertEquals("reports.getNumberOfReports() returned the wrong number of reports for testEmployee3", reports.getNumberOfReports(), 0);
        
        reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee4.getEmployeeId()).getBody();
        assertEquals("reports employeeID does not match expected testEmployee4's ID", reports.getEmployeeId(), testEmployee4.getEmployeeId());
        assertEquals("reports.getNumberOfReports() returned the wrong number of reports for testEmployee4", reports.getNumberOfReports(), 1);
        
        reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee5.getEmployeeId()).getBody();
        assertEquals("reports employeeID does not match expected testEmployee5's ID", reports.getEmployeeId(), testEmployee5.getEmployeeId());
        assertEquals("reports.getNumberOfReports() returned the wrong number of reports for testEmployee5", reports.getNumberOfReports(), 0);
        
    }

    @Test
    public void testReportsCount_cycle() {
    	/*Defined behavior: In the case of a cycle, return all employees who could be considered to report to the queried employee without any repitions */
	    Employee testEmployee1 = new Employee();
	    testEmployee1.setFirstName("Alpha");
	    testEmployee1.setLastName("Cycle");
	    testEmployee1.setDepartment("dpt");
	    testEmployee1.setPosition("pos");
	
	    Employee testEmployee2 = new Employee();
	    testEmployee2.setFirstName("Bravo");
	    testEmployee2.setLastName("Cycle");
	    testEmployee2.setDepartment("dpt");
	    testEmployee2.setPosition("pos");
	    
	    Employee testEmployee3 = new Employee();
	    testEmployee3.setFirstName("Charlie");
	    testEmployee3.setLastName("Cycle");
	    testEmployee3.setDepartment("dpt");
	    testEmployee3.setPosition("pos");
	
	    /*Insert the bottom of the totem pole, get back the ID*/
	    testEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
	    /*now that we have the id, assign to the next level up*/
	    assertTrue("addDirectReport function failed for testEmployee2", testEmployee2.addDirectReport(testEmployee3));
	    assertNotNull("testEmployee2 has null direct reports", testEmployee2.getDirectReports());
	    assertEquals("testEmployee2 has incorrect number of direct reports", testEmployee2.getDirectReports().size(), 1);
	    
	    //repeat for employee 2
	    testEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
	    assertTrue("addDirectReport function failed for testEmployee1", testEmployee1.addDirectReport(testEmployee2));
	    
	    //and employee 1
	    testEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
	    
	    //add cycle intentionally
	    assertTrue("addDirectReport function failed for testEmployee3 - unable to add cycle", testEmployee3.addDirectReport(testEmployee1));
	    
	    //update employee3 to save the cycle	    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        testEmployee3 = restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(testEmployee3, headers),
                        Employee.class,
                        testEmployee3.getEmployeeId()).getBody();

	    //validate the cycle saved
	    assertNotNull("testEmployee3 has null direct reports - cycle didn't take", testEmployee3.getDirectReports());
	    assertEquals("testEmployee3 has incorrect number of direct reports - cycle failed", testEmployee3.getDirectReports().size(), 1);
	    assertEquals("testEmployee31 should report to testEmployee3", testEmployee3.getDirectReports().get(0).getEmployeeId(), testEmployee1.getEmployeeId());
	    
	    //testEmp1 should have 2 reports
	    ReportingStructure reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee1.getEmployeeId()).getBody();
	    assertNotNull(reports);
	    assertNotNull("reports.getEmployeeId() returned a null value", reports.getEmployeeId());
	    assertEquals("reports.getNumberOfReports() returned the wrong number of reports", reports.getNumberOfReports(), 2);

	    //all other employees should now have 2 reports as well
	    reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee2.getEmployeeId()).getBody();
	    assertEquals("reports.getNumberOfReports() returned the wrong number of reports", reports.getNumberOfReports(), 2);
	    reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, testEmployee3.getEmployeeId()).getBody();
	    assertEquals("reports.getNumberOfReports() returned the wrong number of reports", reports.getNumberOfReports(), 2);
    }
    
    @Test
    public void testReportsCount_null_id() {
	    ReportingStructure reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, "").getBody();
	    assertNull("Employee ID should be null for null failure test", reports.getEmployeeId());
	    assertEquals("Expected 0 reports for null failure test", reports.getNumberOfReports(), 0);
    }
    

    @Test
    public void testReportsCount_bad_id() {
	    ReportingStructure reports = restTemplate.getForEntity(reportsURL, ReportingStructure.class, "Bad ID").getBody();
	    assertNull("Employee ID should be null for bad id test", reports.getEmployeeId());
	    assertEquals("Expected 0 reports for bad id failure test", reports.getNumberOfReports(), 0);
    }
    
    
}
