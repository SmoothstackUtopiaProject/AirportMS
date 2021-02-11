package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exeptions.AirportAlreadyExistsException;
import com.ss.utopia.exeptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.services.AirportService;

@RestController
@RequestMapping(value = "/airports")
public class AirportController {

	@Autowired
	AirportService airportService;
	
	
	@GetMapping
	public ResponseEntity<Object> findAll()
	throws ConnectException, SQLException {
		
		List<Airport> airports = airportService.findAll();
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{iataId}")
	public ResponseEntity<Object> findByIataId(@PathVariable String iataId)
	throws ConnectException, SQLException {

		try {
			Airport airport = airportService.findByIataId(iataId);
			return new ResponseEntity<>(airport, HttpStatus.OK);

		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByCityName(@RequestParam String city)
	throws ConnectException, SQLException {

		List<Airport> airports = airportService.findByCityName(city);
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			Airport airport = new ObjectMapper().readValue(body, Airport.class);
			Airport newAirport = airportService.insert(airport.getIataId(), airport.getCity());
			return new ResponseEntity<>(newAirport, HttpStatus.CREATED);

		} catch(AirportAlreadyExistsException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);

		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);

		} catch(JsonProcessingException | NullPointerException err) {
			return new ResponseEntity<>("Invalid Airport formatting!", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("{iataId}")
	public ResponseEntity<Object> update(@PathVariable String iataId, @RequestBody String body)
	throws ConnectException, SQLException {

		try {
			Airport newAirport = airportService.update(iataId, body);
			return new ResponseEntity<>(newAirport, HttpStatus.ACCEPTED);

		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process Airport " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} 
	}

	@DeleteMapping("{iataId}")
	public ResponseEntity<Object> delete(@PathVariable String iataId) 
	throws ConnectException, SQLException {

		try {
			airportService.delete(iataId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}
}