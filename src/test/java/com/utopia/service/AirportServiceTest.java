package com.utopia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.utopia.AirportController;
import com.utopia.exeptions.AirportAlreadyExistsException;
import com.utopia.models.Airport;
import com.utopia.services.AirportService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Profile("test")
@SpringBootTest
public class AirportServiceTest {

  final String SERVICE_PATH_AIRPORTS = "/airports";

  @Mock
  AirportService service;

  @InjectMocks
  AirportController controller;

  MockMvc mvc;
  Airport testAirport;
  List<Airport> testAirportList;

	@BeforeEach
	void setup() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
    Mockito.reset(service);

    testAirport = new Airport("TES", "Testsville");

    testAirportList = new ArrayList<Airport>();
    testAirportList.add(new Airport("DCA", "Washington DC"));
    testAirportList.add(new Airport("IAH", "Huston"));
    testAirportList.add(new Airport("JFK", "New York"));
    testAirportList.add(new Airport("LGA", "New York"));
    testAirportList.add(new Airport("LAX", "Los Angeles"));
    testAirportList.add(new Airport("MCO", "Orlando"));
    testAirportList.add(new Airport("MIA", "Miami"));
    testAirportList.add(new Airport("ORD", "Chicago"));
    testAirportList.add(new Airport("SAN", "San Diego"));
    testAirportList.add(new Airport("SFO", "San Francisco"));
    testAirportList.add(new Airport("SJU", "Puerto Rico"));
	}

  @Test
  void test_validAirportModel_getIataId() {
    assertEquals("TES", testAirport.getIataId());
  }

  @Test
  void test_validAirportModel_getCity() {
    assertEquals("Testsville", testAirport.getCity());
  }

  @Test
  void test_findAllAirports_withValidAirports_thenStatus200() {    
    try {
      when(service.findAll()).thenReturn(testAirportList);

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      List<Airport> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Airport[].class)).collect(Collectors.toList());
  
      assertEquals(testAirportList.size(), actual.size());
      for(int i = 0; i < testAirportList.size(); i++) {
        assertEquals(testAirportList.get(i).getIataId(), actual.get(i).getIataId());
        assertEquals(testAirportList.get(i).getCity(), actual.get(i).getCity());
      }
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findAllAirports_withNoValidAirports_thenStatus204() {    
    try {
      when(service.findAll()).thenReturn(Collections.emptyList());

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
  
      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByIataId_withValidAirport_thenStatus200() {    
    try {
      when(service.findByIataId(testAirport.getIataId())).thenReturn(testAirport);

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS + "/" + testAirport.getIataId())
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      Airport actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Airport.class);
  
      assertEquals(testAirport.getIataId(), actual.getIataId());
      assertEquals(testAirport.getCity(), actual.getCity());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByIataId_withInValidAirport_thenStatus204() {    
    try {
      when(service.findByIataId("123")).thenReturn(null);

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS + "/" + testAirport.getIataId())
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
  
      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByCity_withValidAirports_singleResult_thenStatus200() {    
    try {
      String testCitySearch = "wash"; // Should only return DCA
      when(service.findByCityName(testCitySearch)).thenReturn(testAirportList.stream()
      .filter(airport -> airport.getCity().toLowerCase().contains(testCitySearch))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS + "/city=" + testCitySearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<Airport> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Airport[].class)).collect(Collectors.toList());

      assertEquals(1, actual.size()); 
      assertEquals("DCA", actual.get(0).getIataId());
      assertEquals("Washington DC", actual.get(0).getCity());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByCity_withValidAirports_multiResult_thenStatus200() {    
    try {
      String testCitySearch = "york"; // Should return both JFK & LGA
      when(service.findByCityName(testCitySearch)).thenReturn(testAirportList.stream()
      .filter(airport -> airport.getCity().toLowerCase().contains(testCitySearch))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS + "/city=" + testCitySearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<Airport> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Airport[].class)).collect(Collectors.toList());

      assertEquals(2, actual.size()); 
      assertTrue(actual.get(0).getIataId().equals("JFK") || actual.get(1).getIataId().equals("LGA"));
      assertTrue(actual.get(0).getIataId().equals("JFK") || actual.get(1).getIataId().equals("LGA"));
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByCity_withNoValidAirports_thenStatus204() {    
    try {
      String testCitySearch = "dal"; // Should return no content as Dallas is not in the list
      when(service.findByCityName(testCitySearch)).thenReturn(testAirportList.stream()
      .filter(airport -> airport.getCity().toLowerCase().contains(testCitySearch))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_AIRPORTS + "/city=" + testCitySearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();

      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insert_withValidAirport_thenStatus201() {    
    try {
      when(service.insert(testAirport.getIataId(), testAirport.getCity()))
      .thenReturn(testAirport);

      MvcResult response = mvc.perform(post(SERVICE_PATH_AIRPORTS + 
      "/new/iataId=" + testAirport.getIataId() + ", city=" + testAirport.getCity())
      .header("Accept", "application/json"))
      .andExpect(status().is(201))
      .andReturn();

      Airport actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Airport.class);

      assertEquals(testAirport.getIataId(), actual.getIataId());
      assertEquals(testAirport.getCity(), actual.getCity());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insert_withDuplicateAirport_thenStatus403() {    
    try {
      when(service.insert(testAirport.getIataId(), testAirport.getCity()))
      .thenThrow(new AirportAlreadyExistsException("duplicate"));

      MvcResult response = mvc.perform(post(SERVICE_PATH_AIRPORTS + 
      "/new/iataId=" + testAirport.getIataId() + ", city=" + testAirport.getCity())
      .header("Accept", "application/json"))
      .andExpect(status().is(403))
      .andReturn();

      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insert_withInvalidAirport_thenStatus400() {    
    try {
      String invalidIataId = "ThisIsTooLong";
      when(service.insert(invalidIataId, testAirport.getCity()))
      .thenThrow(new IllegalArgumentException());

      MvcResult response = mvc.perform(post(SERVICE_PATH_AIRPORTS + 
      "/new/iataId=" + invalidIataId + ", city=" + testAirport.getCity())
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();

      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_delete_withValidAirport_thenStatus204() {    
    try {
      MvcResult response = mvc.perform(delete(SERVICE_PATH_AIRPORTS + "/" + testAirport.getIataId())
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
  
      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }
}
