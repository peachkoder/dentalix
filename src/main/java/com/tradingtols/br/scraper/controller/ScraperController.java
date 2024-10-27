package com.tradingtols.br.scraper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;
import com.tradingtols.br.scraper.model.tools.CategoriasExtractor;
import com.tradingtols.br.scraper.service.ScrapService;
import com.tradingtols.br.scraper.service.scrappers.curl.DentaltixCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.Scraper;

@RestController
@RequestMapping("/v1/scrap")
public class ScraperController {
	
	@Autowired
	private ScrapService scrapService;
	@Autowired
	private DentaltixCurlScraper raw;

	@GetMapping("/{search}")
	public void scrap(@PathVariable String search) {
		//scrapService.scrap();
		raw.scrap(search);
	}
}
