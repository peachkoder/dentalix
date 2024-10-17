package com.tradingtols.br.scraper.service.scrappers;

import java.util.Collection;
import java.util.HashSet;

import com.microsoft.playwright.Page;
import com.tradingtols.br.scraper.model.entity.Produto;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

public abstract class ScraperClazz implements ScraperEngine{
	
//	private Collection<Object> list = Collections.synchronizedCollection(new ArrayList<>());
	private Collection<Produto> list = new HashSet<>();
	
	protected ProdutoRepository repo;
	
	public ScraperClazz(ProdutoRepository repo) {
		this.repo = repo;
	}

	@Transactional
	protected void saveToDb() {
		System.out.println("22-[ScraperClazz] - Saving Produtos..." + list.size());
		try {
			
			repo.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		list.clear();
	}
	
	public boolean addToList(Produto item) {
		System.out.println("28-[ScraperClazz] - Produtos List size = " + list.size());
		return list.add(item);
	}
	
	public String splitTextoByLastChar(String texto, char separador) {
		var idx = texto.lastIndexOf(separador);
		if (idx < 0) return texto;
		
		return texto.substring(idx+1);
	}
	
	@Override
	public void handleProfisionalWarning(Page page) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void handleCookieConsent(Page page) {
		// TODO Auto-generated method stub
		
	}
}
