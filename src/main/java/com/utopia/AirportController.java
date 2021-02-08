package com.utopia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<List<Airport>> findAll() {
		List<Airport> airports = airportService.findAll();
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{iataId}")
	public ResponseEntity<Airport> findByIataId(@PathVariable String iataId) {
		Airport airport = airportService.findByIataId(iataId);
		return airport != null
		? new ResponseEntity<>(airport, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/city={cityName}")
	public ResponseEntity<List<Airport>> findByCityName(@PathVariable String cityName) {
		List<Airport> airports = airportService.findByCityName(cityName);
		return !airports.isEmpty()
		? new ResponseEntity<>(airports, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
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

	@PostMapping("/new/iataId={iataId}, city={cityName}")
	public ResponseEntity<Airport> insert(@PathVariable String iataId, @PathVariable String cityName) {
		try {
			Airport newAirport = airportService.insert(iataId, cityName);
			return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
		} catch(AirportAlreadyExistsException err) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}