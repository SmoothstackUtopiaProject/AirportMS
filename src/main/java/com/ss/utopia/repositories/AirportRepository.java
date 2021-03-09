package com.ss.utopia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.ss.utopia.models.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
  
  @Query(value="SELECT * FROM airport WHERE city LIKE %?1%", nativeQuery=true)
	List<Airport> findByAirportCityName(String airportCityName);
  
}