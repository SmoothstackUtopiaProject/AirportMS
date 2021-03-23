package com.ss.utopia;

import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.models.ErrorMessage;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
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
  public ResponseEntity<Object> findByIataId(@PathVariable String airportIataId) 
  throws AirportNotFoundException {
    Airport airport = airportService.findByIataId(airportIataId);
    return new ResponseEntity<>(airport, HttpStatus.OK);
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
    @RequestBody Airport airport
  ) throws IllegalArgumentException, AirportAlreadyExistsException {
    Airport newAirport = airportService.insert(
      airport.getAirportIataId(),
      airport.getAirportName(),
      airport.getAirportCityName()
    );
    return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<Object> update(
    @RequestBody Airport airport
  ) throws IllegalArgumentException, AirportNotFoundException {
    Airport newAirport = airportService.update(
      airport.getAirportIataId(),
      airport.getAirportName(),
      airport.getAirportCityName()
    );
    return new ResponseEntity<>(newAirport, HttpStatus.ACCEPTED);
  }

  @DeleteMapping("{airportIataId}")
  public ResponseEntity<Object> delete(@PathVariable String airportIataId)
  throws AirportNotFoundException {
    airportService.delete(airportIataId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(AirportAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> airportAlreadyExistsException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

  @ExceptionHandler(AirportNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> airportNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> connectException() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(
			new ErrorMessage("Invalid HTTP message content."), 
			HttpStatus.BAD_REQUEST
		);
	}

  @ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> illegalArgumentException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> sqlException() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}
}
