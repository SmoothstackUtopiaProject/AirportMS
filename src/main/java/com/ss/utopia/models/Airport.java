package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airport")
public class Airport {
	@Id
	@Column(name = "iata_id")
	private String iataId;

	@Column(name = "city")
	private String city;

	public Airport() {}

	public Airport(String iataId, String city) {
		super();
		this.iataId = iataId;
		this.city = city;
	}

	public String getIataId() {
		return iataId;
	}

	public void setIataId(String iataId) {
		this.iataId = iataId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}