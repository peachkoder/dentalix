package com.tradingtols.br.scraper.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Marca {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String brand;
	
	public Marca(Integer id, String brand) {
		super();
		this.id = id;
		this.brand = brand;
	}
	
	
}
