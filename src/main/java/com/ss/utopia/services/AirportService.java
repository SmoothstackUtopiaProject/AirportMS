package com.ss.utopia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exeptions.AirportAlreadyExistsException;
import com.ss.utopia.exeptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.repositories.AirportRepository;

@Service
public class AirportService {

	@Autowired
	AirportRepository airportRepository;

	public List<Airport> findAll() {
		return airportRepository.findAll();
	}

	public Airport findByIataId(String iataId) throws AirportNotFoundException {
		// Validate IataId
		String formattedIataId = formatIataId(iataId);
		if(!validateIataId(formattedIataId)) throw new IllegalArgumentException("Not a valid IATA code: " + formattedIataId + "!");

		// Perform GET query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedIataId);
		if(!optionalAirpot.isPresent()) throw new AirportNotFoundException("No airport with IATA code: " + formattedIataId + " exist!");
		return optionalAirpot.get();
	}

	public List<Airport> findByCityName(String cityName) {
		return airportRepository.findByCityName(cityName);
	}

	public Airport insert(String iataId, String cityName) throws AirportAlreadyExistsException, IllegalArgumentException {
		// Validate IataId & cityName
		String formattedIataId = formatIataId(iataId);
		String formattedCityName = formatCityName(cityName);
		if(!validateIataId(formattedIataId)) throw new IllegalArgumentException("The IATA Code: " + formattedIataId + "is not valid!");
		if(!validateCityName(formattedCityName)) throw new IllegalArgumentException("The city name: " + cityName + " is invalid!\n(Only letters are allowed - cannot be empty or have extra whitespace)");
		
		// Perform the POST query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedIataId);
		if(optionalAirpot.isPresent()) throw new AirportAlreadyExistsException("An airport with IATA code: " + formattedIataId + " already exist!");
		return airportRepository.save(new Airport(formattedIataId, formattedCityName));
	}

	public Airport update(String iataId, String cityName) throws AirportNotFoundException, IllegalArgumentException {		
		// Validate cityName
		String formattedIataId = formatIataId(iataId);
		String formattedCityName = formatCityName(cityName);
		if(!validateCityName(formattedCityName)) {
			throw new IllegalArgumentException("The city name: " + cityName + " is invalid!\n(Only letters are allowed - cannot be empty or have extra whitespace)");
		}

		// Perform the PUT query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedIataId);
		if(!optionalAirpot.isPresent()) throw new AirportNotFoundException("No airport with IATA code: " + formattedIataId + " exist!");
		return airportRepository.save(new Airport(formattedIataId, formattedCityName));
	}

	public void delete(String iataId) throws AirportNotFoundException {
		String formattedIataId = iataId.toUpperCase();
		try {
			airportRepository.deleteById(formattedIataId);
		} catch(Exception err) {
			throw new AirportNotFoundException("IATA Code not found!");
		}
	}

	private String formatIataId(String iataId) {
		return iataId != null
		? iataId.toUpperCase().replaceAll("[^A-Z]", "")
		: null;
	}

	private Boolean validateIataId(String iataId) {
		return iataId != null && 
			iataId.replaceAll("[^A-Z]", "").length() == 3;
	}

	private String formatCityName(String cityName) {
		return cityName != null
		? cityName.trim()
		: null;
	}

	private Boolean validateCityName(String cityName) {
		return 
		cityName != null && 
		!cityName.isEmpty() && 
		cityName.length() < 256 && 
		cityName.replaceAll("[^a-zA-Z\\s]", "").length() == cityName.length();
	}
}