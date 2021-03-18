package com.ss.utopia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.utopia.models.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {}