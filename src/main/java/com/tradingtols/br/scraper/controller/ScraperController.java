package com.tradingtols.br.scraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingtols.br.scraper.service.ScrapService;

@RestController
@RequestMapping("/v1/scrap")
public class ScraperController {
	
	@Autowired
	private ScrapService scrapService;

	@GetMapping("/{search}")
	public void scrap(@PathVariable String search) {
		
		scrapService.scrap();
	}
}
