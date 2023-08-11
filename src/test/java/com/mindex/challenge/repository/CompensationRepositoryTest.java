package com.mindex.challenge.repository;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dao.CompensationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.time.LocalDate;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CompensationRepositoryTest {

    @Autowired
    private CompensationRepository compensationRepository;

    @Test
    public void testCompensationRepository_happy_path() {
    	String testID = "Test ID";
    	Compensation compensation = new Compensation(testID);
    	compensation.setSalary(42);
    	compensationRepository.insert(compensation);
    	List<Compensation> results = compensationRepository.findByEmployeeId(testID);
    	boolean found = false;
    	for (Compensation comp : results) {
    		if (comp.equals(compensation)) {
    			found = true;
    		}
    	}
    	assertTrue(found);
    }
    
    @Test
    public void testCompensationRepository_multiple_salaries() {
    	String testID = "Test ID - multiple";
    	Compensation compensation1 = new Compensation(testID, 42, LocalDate.parse("2002-02-02"));
    	compensationRepository.insert(compensation1);
    	Compensation compensation2 = new Compensation(testID, 73, LocalDate.parse("2020-02-02"));
    	compensationRepository.insert(compensation2);
    	List<Compensation> results = compensationRepository.findByEmployeeId(testID);
    	assertEquals("Expected 2 results", 2, results.size());
    	for (Compensation comp : results) {
    		assertTrue("Unexpected compensation", comp.equals(compensation1) || comp.equals(compensation2));
    	}
    }
    
    @Test
    public void testCompensationRepository_bad_id() {
    	String testID = "Bad ID";
    	List<Compensation> results = compensationRepository.findByEmployeeId(testID);
    	assertEquals("Expected 0 results", 0, results.size());
    }
}