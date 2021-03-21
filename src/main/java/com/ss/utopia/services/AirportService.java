package com.ss.utopia.services;

import com.ss.utopia.exceptions.AirportAlreadyExistsException;
import com.ss.utopia.exceptions.AirportNotFoundException;
import com.ss.utopia.models.Airport;
import com.ss.utopia.repositories.AirportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {

  @Autowired
  private AirportRepository airportRepository;

  private static final String REGEX_AIRPORT_IATA_ID = "[A-Z]";

  public List<Airport> findAll() {
    return airportRepository.findAll();
  }

  public Airport findByIataId(String airportIataId)
    throws AirportNotFoundException {
    String formattedAirportIataId = formatAirportIataId(airportIataId);
    Optional<Airport> optionalAirpot = airportRepository.findById(formattedAirportIataId);
    if (!optionalAirpot.isPresent()) {
      throw new AirportNotFoundException(
        "No airport with IATA code: " + formattedAirportIataId + " exists."
      );
    }
    return optionalAirpot.get();
  }

  public List<Airport> findBySearchAndFilter(Map<String, String> filterMap) {
    List<Airport> airports = findAll();
    if (!filterMap.keySet().isEmpty()) {
      airports = applyFilters(airports, filterMap);
    }
    return airports;
  }

  public List<Airport> applyFilters(
    List<Airport> airports,
    Map<String, String> filterMap
  ) {
    // IATA ID
    String airportIataId = "airportIataId";
    if (filterMap.keySet().contains(airportIataId)) {
      String parsedAirportIataId = filterMap.get(airportIataId);
      airports =
        airports
          .stream()
          .filter(i -> i.getAirportIataId().equals(parsedAirportIataId))
          .collect(Collectors.toList());
    }

    // City Name
    String airportCityName = "airportCityName";
    if (filterMap.keySet().contains(airportCityName)) {
      String parsedAirportCityName = filterMap.get(airportCityName);
      airports =
        airports
          .stream()
          .filter(i -> i.getAirportCityName().equals(parsedAirportCityName))
          .collect(Collectors.toList());
    }

    // Search - (applied last due to save CPU usage
    return applySearch(airports, filterMap);
  }

  public List<Airport> applySearch(
    List<Airport> airports,
    Map<String, String> filterMap
  ) {

    String searchTerms = "searchTerms";
    if (!filterMap.keySet().contains(searchTerms)) {
      return airports;
    }

    String formattedSearch = filterMap
      .get(searchTerms)
      .toLowerCase(Locale.getDefault())
      .replace(", ", ",");

    String[] splitTerms = formattedSearch.split(",");
    List<Airport> airportsWithSearchTerms = new ArrayList<>();
    for (Airport airport : airports) {
      boolean containsSearchTerms = true;

      String airportAsString =
        (airport.getAirportIataId() +
        airport.getAirportCityName())
				.toLowerCase(Locale.getDefault());

      for (String term : splitTerms) {
        if (!airportAsString.contains(term)) {
          containsSearchTerms = false;
          break;
        }
      }

      if (containsSearchTerms) {
        airportsWithSearchTerms.add(airport);
      }
    }
    return airportsWithSearchTerms;
  }

  public Airport insert(String airportIataId, String airportName, String airportCityName)
    throws AirportAlreadyExistsException, IllegalArgumentException {
    String formattedAirportIataId = formatAirportIataId(airportIataId);
    boolean isValidIataId = validateAirportIataId(airportIataId);
    if (!isValidIataId) {
      throw new IllegalArgumentException(
        "The IATA Code: " +
        formattedAirportIataId +
        " is invalid (only uppercase English letter are allowed)"
      );
    }

    Optional<Airport> optionalAirpot = airportRepository.findById(
      formattedAirportIataId
    );
    if (optionalAirpot.isPresent()) {
      throw new AirportAlreadyExistsException(
        "An airport with IATA code: " +
        formattedAirportIataId +
        " already exists."
      );
    }
    return airportRepository.save(
      new Airport(formattedAirportIataId, airportName, airportCityName)
    );
  }

  public Airport update(String airportIataId, String airportName, String airportCityName)
    throws AirportNotFoundException, IllegalArgumentException {
    String formattedAirportIataId = formatAirportIataId(airportIataId);
    boolean isValidIataId = validateAirportIataId(airportIataId);
    if (!isValidIataId) {
      throw new IllegalArgumentException(
        "The IATA Code: " +
        formattedAirportIataId +
        " is invalid (only uppercase English letter are allowed)"
      );
    }

    Optional<Airport> optionalAirpot = airportRepository.findById(
      formattedAirportIataId
    );
    if (!optionalAirpot.isPresent()) {
      throw new AirportNotFoundException(
        "No airport with IATA code: " + formattedAirportIataId + " exists."
      );
    }
    return airportRepository.save(
      new Airport(formattedAirportIataId, airportName, airportCityName)
    );
  }

  public void delete(String airportIataId) throws AirportNotFoundException {
    String formattedAirportIataId = formatAirportIataId(airportIataId);
    findByIataId(airportIataId);
    airportRepository.deleteById(formattedAirportIataId);
  }

  private static String formatAirportIataId(String airportIataId) {
    return airportIataId.toUpperCase(Locale.getDefault());
  }

  private static Boolean validateAirportIataId(String airportIataId) {
    return airportIataId.replaceAll("[^A-Z]", "").length() == airportIataId.length();
  }
}
