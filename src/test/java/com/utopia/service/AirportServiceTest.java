package com.utopia.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.utopia.models.Airport;
import com.utopia.services.AirportService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AirportServiceTest {

//   Airport airport;

//   @Autowired
//   AirportService airportService;
	
//   @Test
//   public void contextLoads() {
//     assertNotNull(airportService);
//   }

// 	@Test
// 	public void testFindAll() {
//     List<Airport> expected = new ArrayList<Airport>();
//     expected.add(new Airport("DCA", "Washington DC"));
//     expected.add(new Airport("IAH", "Huston"));
//     expected.add(new Airport("JFK", "New York"));
//     expected.add(new Airport("LAX", "Los Angeles"));
//     expected.add(new Airport("MCO", "Orlando"));
//     expected.add(new Airport("MIA", "Miami"));
//     expected.add(new Airport("ORD", "Chicago"));
//     expected.add(new Airport("SAN", "San Diego"));
//     expected.add(new Airport("SFO", "San Francisco"));
//     expected.add(new Airport("SJU", "Puerto Rico"));

//     List<Airport> actual = airportService.findAll();
//     assertEquals(expected.size(), actual.size());
//     for(int i = 0; i < expected.size(); i++) {
//       assertEquals(expected.get(i).getIataId(), actual.get(i).getIataId());
//       assertEquals(expected.get(i).getCity(), actual.get(i).getCity());
//     }
// 	}

// 	@Test
// 	public void testFindByIataId() {
//     List<Airport> expected = new ArrayList<Airport>();
//     expected.add(new Airport("DCA", "Washington DC"));
//     expected.add(new Airport("IAH", "Huston"));
//     expected.add(new Airport("JFK", "New York"));
//     expected.add(new Airport("LAX", "Los Angeles"));
//     expected.add(new Airport("MCO", "Orlando"));
//     expected.add(new Airport("MIA", "Miami"));
//     expected.add(new Airport("ORD", "Chicago"));
//     expected.add(new Airport("SAN", "San Diego"));
//     expected.add(new Airport("SFO", "San Francisco"));
//     expected.add(new Airport("SJU", "Puerto Rico"));

//     for(int i = 0; i < expected.size(); i++) {
//       Airport actual = airportService.findByIataId(expected.get(i).getIataId());
//       assertEquals(expected.get(i).getIataId(), actual.getIataId());
//       assertEquals(expected.get(i).getCity(), actual.getCity());
//     }
// 	}
	
// 	@Test
// 	public void testInsertAndDelete() {
//     List<Airport> expected1 = new ArrayList<Airport>();
//     expected1.add(new Airport("DCA", "Washington DC"));
//     expected1.add(new Airport("IAH", "Huston"));
//     expected1.add(new Airport("JFK", "New York"));
//     expected1.add(new Airport("LAX", "Los Angeles"));
//     expected1.add(new Airport("MCO", "Orlando"));
//     expected1.add(new Airport("MIA", "Miami"));
//     expected1.add(new Airport("ORD", "Chicago"));
//     expected1.add(new Airport("SAN", "San Diego"));
//     expected1.add(new Airport("SFO", "San Francisco"));
//     expected1.add(new Airport("SJU", "Puerto Rico"));
//     expected1.add(new Airport("TES", "TEST"));

//     assertDoesNotThrow(() -> airportService.insert(new Airport("TES", "TEST")));
//     List<Airport> actual1 = airportService.findAll();

//     assertEquals(expected1.size(), actual1.size());
//     for(int i = 0; i < expected1.size(); i++) {
//       assertEquals(expected1.get(i).getIataId(), actual1.get(i).getIataId());
//       assertEquals(expected1.get(i).getCity(), actual1.get(i).getCity());
//     }

//     List<Airport> expected2 = new ArrayList<Airport>();
//     expected2.add(new Airport("DCA", "Washington DC"));
//     expected2.add(new Airport("IAH", "Huston"));
//     expected2.add(new Airport("JFK", "New York"));
//     expected2.add(new Airport("LAX", "Los Angeles"));
//     expected2.add(new Airport("MCO", "Orlando"));
//     expected2.add(new Airport("MIA", "Miami"));
//     expected2.add(new Airport("ORD", "Chicago"));
//     expected2.add(new Airport("SAN", "San Diego"));
//     expected2.add(new Airport("SFO", "San Francisco"));
//     expected2.add(new Airport("SJU", "Puerto Rico"));

//     assertDoesNotThrow(() -> airportService.delete("TES"));
//     List<Airport> actual2 = airportService.findAll();

//     assertEquals(expected2.size(), actual2.size());
//     for(int i = 0; i < expected2.size(); i++) {
//       assertEquals(expected2.get(i).getIataId(), actual2.get(i).getIataId());
//       assertEquals(expected2.get(i).getCity(), actual2.get(i).getCity());
//     }
// 	}
}
