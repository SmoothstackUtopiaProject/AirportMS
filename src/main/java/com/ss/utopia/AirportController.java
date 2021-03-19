package com.ss.utopia;

import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.services.AirportService;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airports")
public class AirportController {

  @Autowired
  private AirportService airportService;

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

  @GetMapping
  public ResponseEntity<Object> findAll() {
    List<Airport> airports = airportService.findAll();
    return !airports.isEmpty()
      ? new ResponseEntity<>(airports, HttpStatus.OK)
      : new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{airportIataId}")
  public ResponseEntity<Object> findByIataId(
    @PathVariable String airportIataId
  ) {
    try {
      Airport airport = airportService.findByIataId(airportIataId);
      return new ResponseEntity<>(airport, HttpStatus.OK);
    } catch (AirportNotFoundException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    } catch (IllegalArgumentException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @PostMapping("/search")
  public ResponseEntity<Object> findBySearchAndFilter(
    @RequestBody Map<String, String> filterMap
  ) {
    List<Airport> airports = airportService.findBySearchAndFilter(filterMap);
    return !airports.isEmpty()
      ? new ResponseEntity<>(airports, HttpStatus.OK)
      : new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping
  public ResponseEntity<Object> insert(
    @RequestBody Map<String, String> airportMap
  ) {
    try {
      String airportIataId = airportMap.get("airportIataId");
      String airportCityName = airportMap.get("airportCityName");
      Airport newAirport = airportService.insert(
        airportIataId,
        airportCityName
      );
      return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
    } catch (AirportAlreadyExistsException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.CONFLICT.value()),
        HttpStatus.CONFLICT
      );
    } catch (IllegalArgumentException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @PutMapping
  public ResponseEntity<Object> update(
    @RequestBody Map<String, String> airportMap
  ) {
    try {
      String airportIataId = airportMap.get("airportIataId");
      String airportCityName = airportMap.get("airportCityName");
      Airport newAirport = airportService.update(
        airportIataId,
        airportCityName
      );
      return new ResponseEntity<>(newAirport, HttpStatus.ACCEPTED);
    } catch (AirportNotFoundException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    } catch (IllegalArgumentException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @DeleteMapping("{airportIataId}")
  public ResponseEntity<Object> delete(@PathVariable String airportIataId) {
    try {
      airportService.delete(airportIataId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (AirportNotFoundException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND
      );
    } catch (IllegalArgumentException err) {
      return new ResponseEntity<>(
        new HttpError(err.getMessage(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @ExceptionHandler(ConnectException.class)
  public ResponseEntity<Object> invalidConnection() {
    return new ResponseEntity<>(
      new HttpError(
        "Service temporarily unavailabe.",
        HttpStatus.SERVICE_UNAVAILABLE.value()
      ),
      HttpStatus.SERVICE_UNAVAILABLE
    );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> invalidMessage() {
    return new ResponseEntity<>(
      new HttpError(
        "Invalid HTTP message content.",
        HttpStatus.BAD_REQUEST.value()
      ),
      HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<Object> invalidSQL() {
    return new ResponseEntity<>(
      new HttpError(
        "Service temporarily unavailabe.",
        HttpStatus.SERVICE_UNAVAILABLE.value()
      ),
      HttpStatus.SERVICE_UNAVAILABLE
    );
  }
}
