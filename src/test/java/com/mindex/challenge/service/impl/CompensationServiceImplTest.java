package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
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
import java.util.List;

import static org.junit.Assert.*;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead_happy_path() {
    	String testID = "Service Test ID";
        Compensation testCompensation = new Compensation(testID, 42, LocalDate.now());

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertTrue("Created Compensation does not match test comp", testCompensation.equals(createdCompensation));

        // Read checks
        Compensation[] readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation[].class, testID).getBody();
        assertEquals("Expected one result from read test", 1, readCompensation.length);
        assertTrue("Read Compensation does not match created comp", readCompensation[0].equals(createdCompensation));
    }
    
    @Test
    public void testRead_no_results() {
    	Compensation[] readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation[].class, "Bad ID").getBody();
        assertEquals("Results should be empty", 0, readCompensation.length);
    }
}
