package com.ss.utopia.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.repositories.AirportRepository;

@Service
public class AirportService {

	@Autowired
	AirportRepository airportRepository;

	public List<Airport> findAll() {
		return airportRepository.findAll();
	}

	public Airport findByIataId(String airportIataId) throws AirportNotFoundException {
		// Validate IataId
		String formattedAirportIataId = formatAirportIataId(airportIataId);
		if(!validateAirportIataId(formattedAirportIataId)) throw new IllegalArgumentException("Not a valid IATA code: " + formattedAirportIataId + ".");

		// Perform GET query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedAirportIataId);
		if(!optionalAirpot.isPresent()) throw new AirportNotFoundException("No airport with IATA code: " + formattedAirportIataId + " exist!");
		return optionalAirpot.get();
	}

	public List<Airport> findBySearchAndFilter(HashMap<String, String> filterMap) {
		List<Airport> airports = findAll();
		if(!filterMap.keySet().isEmpty()) airports = applyFilters(airports, filterMap);
		return airports;
	}

	public List<Airport> applyFilters(List<Airport> airports, HashMap<String, String> filterMap) {
		// IATA ID
		String airportIataId = "airportIataId";
		if(filterMap.keySet().contains(airportIataId)) {
			try {
				String parsedAirportIataId = filterMap.get(airportIataId);
				airports = airports.stream()
				.filter(i -> i.getAirportIataId().equals(parsedAirportIataId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// City Name
		String airportCityName = "airportCityName";
		if(filterMap.keySet().contains(airportCityName)) {
			try {
				String parsedAirportCityName = filterMap.get(airportCityName);
				airports = airports.stream()
				.filter(i -> i.getAirportCityName().equals(parsedAirportCityName))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Search - (applied last due to save CPU usage
		return applySearch(airports, filterMap);
	}

	public List<Airport> applySearch(List<Airport> airports, HashMap<String, String> filterMap) {
		List<Airport> airportsWithSearchTerms = new ArrayList<Airport>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			String[] splitTerms = formattedSearch.split(",");
			ObjectMapper mapper = new ObjectMapper();
			
			for(Airport airport : airports) {
				boolean containsSearchTerms = true;
				
				try {
					String airportAsString = mapper.writeValueAsString(airport)
					.toLowerCase()
					.replace("airportiataid", "")
					.replace("airportcityname", "");
					
					for(String term : splitTerms) {
						if(!airportAsString.contains(term)) {
							containsSearchTerms = false;
							break;
						}
					}
				} catch(JsonProcessingException err){
					containsSearchTerms = false;
				}

				if(containsSearchTerms) {
					airportsWithSearchTerms.add(airport);
				}
			}
		}
		return airportsWithSearchTerms;
	}

	public Airport insert(String airportIataId, String airportCityName) throws AirportAlreadyExistsException, IllegalArgumentException {
		// Validate airportIataId & airportCityName
		String formattedAirportIataId = formatAirportIataId(airportIataId);
		String formattedAirportCityName = formatAirportCityName(airportCityName);
		if(!validateAirportIataId(formattedAirportIataId)) throw new IllegalArgumentException("The IATA Code: " + formattedAirportIataId + "is not valid!");
		if(!validateAirportCityName(formattedAirportCityName)) throw new IllegalArgumentException("The city name: " + formattedAirportCityName + " is invalid (Only letters are allowed - cannot be empty or have extra whitespace).");
		
		// Perform the POST query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedAirportIataId);
		if(optionalAirpot.isPresent()) throw new AirportAlreadyExistsException("An airport with IATA code: " + formattedAirportIataId + " already exist!");
		return airportRepository.save(new Airport(formattedAirportIataId, formattedAirportCityName));
	}

	public Airport update(String airportIataId, String airportCityName) throws AirportNotFoundException, IllegalArgumentException {		
		// Validate airportCityName
		String formattedAirportIataId = formatAirportIataId(airportIataId);
		String formattedAirportCityName = formatAirportCityName(airportCityName);
		if(!validateAirportCityName(formattedAirportCityName)) {
			throw new IllegalArgumentException("The city name: " + formattedAirportCityName + " is invalid (Only letters are allowed - cannot be empty or have extra whitespace).");
		}

		// Perform the PUT query
		Optional<Airport> optionalAirpot = airportRepository.findById(formattedAirportIataId);
		if(!optionalAirpot.isPresent()) throw new AirportNotFoundException("No airport with IATA code: " + formattedAirportIataId + " exist!");
		return airportRepository.save(new Airport(formattedAirportIataId, formattedAirportCityName));
	}

	public void delete(String airportIataId) throws AirportNotFoundException {
		String formattedAirportIataId = airportIataId.toUpperCase();
		try {
			airportRepository.deleteById(formattedAirportIataId);
		} catch(Exception err) {
			throw new AirportNotFoundException("IATA Code not found!");
		}
	}

	private String formatAirportIataId(String airportIataId) {
		return airportIataId != null
			? airportIataId.toUpperCase().replaceAll("[^A-Z]", "")
			: null;
	}

	private Boolean validateAirportIataId(String airportIataId) {
		return airportIataId != null && 
			airportIataId.replaceAll("[^A-Z]", "").length() == 3;
	}

	private String formatAirportCityName(String airportCityName) {
		return airportCityName != null
			? airportCityName.replaceAll("[\"]", "")
			: null;
	}

	private Boolean validateAirportCityName(String airportCityName) {
		if(airportCityName == null) return false;

		String nameCheck = airportCityName.replaceAll("[^a-zA-Z\\s]", "");
		return airportCityName != null && 
		!airportCityName.isEmpty() && 
		airportCityName.length() < 256 &&
		airportCityName.charAt(0) != ' ' &&
		airportCityName.charAt(airportCityName.length()-1) != ' ' &&
		nameCheck.length() == airportCityName.length();
	}
}