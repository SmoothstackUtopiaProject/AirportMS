package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ss.utopia.repositories.AirportRepository;
import com.ss.utopia.services.AirportService;
import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AirportServiceTest {

  List<Airport> testAirportList;

  @MockBean
  private AirportRepository repository;

  @Autowired
  private AirportService service;

  @Test
  void test_getAllAirports_returnsListOfAirports() {
    when(repository.findAll()).thenReturn(List.of(new Airport()));
    
    assertEquals(1, service.findAll().size());
  }

  @Test
  void test_findByIataId_returnAirport() throws Exception {
    Airport expected = new Airport("TTT","TestName","TestCity");
    when(repository.findById(expected.getAirportIataId())).thenReturn(Optional.of(expected));

    assertEquals(expected, service.findByIataId(expected.getAirportIataId()));
  }

  @Test
  void test_findByIataId_throwsAirportNotFoundException(){
    when(repository.findById(any())).thenReturn(Optional.empty());
    
    assertThrows(AirportNotFoundException.class, () -> service.findByIataId("XXX"));
  }

  @Test
  void test_insertAirport_success() throws Exception {
    Airport expected = new Airport("TTT","TestName","TestCity");
    service.insert(expected.getAirportIataId(),expected.getAirportName(),expected.getAirportCityName());
    
    Mockito.verify(repository).save(any());
  }

  @Test
  void test_insertAirport_throwsIllegalArgumentException() {
    Airport airport = new Airport("x","TestName", "TestCity");
    
    assertThrows(IllegalArgumentException.class,
     () -> service.insert(airport.getAirportIataId(),airport.getAirportName(),airport.getAirportCityName()));
  }

  @Test
  void test_insertAirport_throwsAirportAlreadyExistsException(){
    Airport airport = new Airport("ABC","TestName", "TestCity");
    when(repository.findById(any())).thenReturn(Optional.of(airport));

    assertThrows(AirportAlreadyExistsException.class,
     () -> service.insert(airport.getAirportIataId(),airport.getAirportName(),airport.getAirportCityName()));
  }

  @Test
  void test_updateAirport_success() throws Exception {
    Airport expected = new Airport("TTT","TestName","TestCity");
    String editCityName = "EditCity";
    when(repository.findById(any())).thenReturn(Optional.of(expected));
    service.update(expected.getAirportIataId(),expected.getAirportName(),editCityName);
    
    Mockito.verify(repository).save(any());
  }

  @Test
  void test_updateAirport_throwsIllegalArgumentException() {
    Airport airport = new Airport("x","TestName", "TestCity");
    
    assertThrows(IllegalArgumentException.class,
     () -> service.update(airport.getAirportIataId(),airport.getAirportName(),airport.getAirportCityName()));
  }

  @Test
  void test_updateAirport_throwsAirportNotFoundException() {
    Airport expected = new Airport("TTT","TestName","TestCity");
    when(repository.findById(expected.getAirportIataId())).thenReturn(Optional.empty());
    
    assertThrows(AirportNotFoundException.class, () -> service.findByIataId(expected.getAirportIataId()));
  }

  @Test
  void test_deleteAirport_success() throws Exception{
    Airport expected = new Airport("TTT","TestName","TestCity");
    when(repository.findById(expected.getAirportIataId())).thenReturn(Optional.of(expected));
    service.delete(expected.getAirportIataId());
    
    Mockito.verify(repository).deleteById(expected.getAirportIataId());
  }

}
