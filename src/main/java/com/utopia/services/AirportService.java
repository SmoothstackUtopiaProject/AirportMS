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

	public Airport insert(Airport airport) throws AirportAlreadyExistsException {
		try {
			return airportRepository.save(airport);
		} catch(IllegalArgumentException err) {
			throw new AirportAlreadyExistsException("IATA Code already exist!");
		}
	}

	public void delete(String iataId) throws AirportNotFoundException {
		try {
			airportRepository.deleteById(iataId);
		} catch(Exception err) {
			throw new AirportNotFoundException("IATA Code not found!");
		}
	}
}