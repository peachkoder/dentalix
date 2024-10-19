package com.tradingtols.br.scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tradingtols.br.scraper.config.ConfigDatabase;

@SpringBootApplication
public class ScraperApplication {

	@Autowired
	ConfigDatabase configDatabase;
	
	public static void main(String[] args) {
		SpringApplication.run(ScraperApplication.class, args);
		
	}

}
