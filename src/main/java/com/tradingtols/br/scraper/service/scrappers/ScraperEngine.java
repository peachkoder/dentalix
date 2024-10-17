package com.tradingtols.br.scraper.service.scrappers;

import java.util.List;

import com.microsoft.playwright.Page;
import com.tradingtols.br.scraper.model.entity.Produto;

public interface ScraperEngine{
	
	List<Produto> scrap(String search);
	List<Produto> scrap(List<String> searchList);
	void handleProfisionalWarning(Page page);
	void handleCookieConsent(Page page);
}
