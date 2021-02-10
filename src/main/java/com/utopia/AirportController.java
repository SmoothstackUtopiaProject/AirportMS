package com.utopia;

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
import com.utopia.exeptions.AirportAlreadyExistsException;
import com.utopia.exeptions.AirportNotFoundException;
import com.utopia.models.Airport;
import com.utopia.services.AirportService;

@RestController
@RequestMapping(value = "/airports")
public class AirportController {

	@Autowired
	AirportService airportService;
	
	
	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Airport> airports = airportService.findAll();
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{iataId}")
	public ResponseEntity<Object> findByIataId(@PathVariable String iataId) {
		try {
			Airport airport = airportService.findByIataId(iataId);
			return new ResponseEntity<>(airport, HttpStatus.OK);
		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByCityName(@RequestParam String city) {
		List<Airport> airports = airportService.findByCityName(city);
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody String body) {
		try {
			Airport airport = new ObjectMapper().readValue(body, Airport.class);
			Airport newAirport = airportService.insert(airport.getIataId(), airport.getCity());
			return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
		} catch(AirportAlreadyExistsException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);
		} catch(IllegalArgumentException | JsonProcessingException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("{iataId}")
	public ResponseEntity<Object> update(@PathVariable String iataId, @RequestBody String body) {
		try {
			Airport newAirport = airportService.update(iataId, body);
			return new ResponseEntity<>(newAirport, HttpStatus.ACCEPTED);
		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("{iataId}")
	public ResponseEntity<Object> delete(@PathVariable String iataId) {
		try {
			airportService.delete(iataId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidRequestContent() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}
}