package com.utopia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utopia.exeptions.AirportAlreadyExistsException;
import com.utopia.exeptions.AirportNotFoundException;
import com.utopia.models.Airport;
import com.utopia.repositories.AirportRepository;

@Service
public class AirportService {

	@Autowired
	AirportRepository airportRepository;

	public List<Airport> findAll() {
		return airportRepository.findAll();
	}

	public Airport findByIataId(String iataId) {
		Optional<Airport> optionalAirpot = airportRepository.findById(iataId);
		return optionalAirpot.isPresent()
		? optionalAirpot.get()
		: null;
	}

	public List<Airport> findByCityName(String cityName) {
		return airportRepository.findByCityName(cityName);
	}

	public Airport insert(String iataId, String cityName) throws AirportAlreadyExistsException, IllegalArgumentException {
		Optional<Airport> optionalAirpot = airportRepository.findById(iataId);
		if(optionalAirpot.isPresent()) {
			throw new AirportAlreadyExistsException("An airport with IATA code: " +iataId + " already exist!");
		}
		return airportRepository.save(new Airport(iataId, cityName));
	}

	public void delete(String iataId) throws AirportNotFoundException {
		try {
			airportRepository.deleteById(iataId);
		} catch(Exception err) {
			throw new AirportNotFoundException("IATA Code not found!");
		}
	}
}