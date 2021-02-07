package com.utopia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utopia.exeptions.AirportAlreadyExistsException;
import com.utopia.exeptions.AirportNotFoundException;
import com.utopia.models.Airport;
import com.utopia.services.AirportService;

@RestController
@RequestMapping(value = "/airports")
public class AirportController implements ErrorController {

	@Autowired
	AirportService airportService;
	
	
	@GetMapping
	public ResponseEntity<List<Airport>> findAll() {
		List<Airport> list = airportService.findAll();
		return !list.isEmpty()
		? new ResponseEntity<>(list, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{iataId}")
	public ResponseEntity<Airport> findByCode(@PathVariable String iataId) {
		Airport airport = airportService.findByIataId(iataId);
		return airport != null
		? new ResponseEntity<>(airport, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{iataId}")
	public <T> ResponseEntity<T> delete(@PathVariable String iataId) {
		try {
			airportService.delete(iataId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch(AirportNotFoundException err) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/new")
	public ResponseEntity<Airport> insert(@RequestBody Airport airport) {
		System.out.println("new called");
		try {
			Airport newAirport = airportService.insert(airport);
			return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
		} catch(AirportAlreadyExistsException err) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
	}

	// @RequestMapping(value = "/error")
	// public <T> ResponseEntity<T> allFallback() {
	// 	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	// }

	@Override
	public String getErrorPath() {
		return "Invalid request for airports.";
	}
}