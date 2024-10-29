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
import com.tradingtols.br.scraper.service.scrappers.curl.CliniclicCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.DentalibericaCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.DentaltixCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.DotamedCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.DouromedCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.MontellanoCurlScraper;
import com.tradingtols.br.scraper.service.scrappers.curl.Scraper;

@RestController
@RequestMapping("/v1/scrap")
public class ScraperController {
	
	@Autowired
	private ScrapService scrapService;
	@Autowired
	private DentaltixCurlScraper dentaltixScraper;
	@Autowired
	private MontellanoCurlScraper montellanoCurlScraper;
	@Autowired
	private DotamedCurlScraper  dotamedCurlScraper;
	@Autowired
	private CliniclicCurlScraper cliniclicCurlScraper; 
	@Autowired
	private DouromedCurlScraper douromedCurlScraper; 
	@Autowired
	private DentalibericaCurlScraper dentalibericaCurlScraper;

	@GetMapping("/{search}")
	public void scrap(@PathVariable String search) {
		//scrapService.scrap();
//		montellanoCurlScraper.scrap(search);
//		dotamedCurlScraper.scrap(search);
//		cliniclicCurlScraper.scrap(search);
//		douromedCurlScraper.scrap(search);
		dentalibericaCurlScraper.scrap(search);
	}
}
