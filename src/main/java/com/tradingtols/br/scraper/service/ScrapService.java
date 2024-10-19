package com.tradingtols.br.scraper.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradingtols.br.scraper.model.repository.BuscaPadraoRepository;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.service.scrappers.ScraperDentalExpress;
import com.tradingtols.br.scraper.service.scrappers.ScraperDentalix;

@Service
public class ScrapService {

//	@Autowired
//	private ScraperDentaleader dentaleader = new ScraperDentaleader();
	
	@Autowired
	private BuscaPadraoRepository buscaRepo;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public void scrap() {
	
		List<String> buscas = buscaRepo.findAll()
				.stream()
				.map(busca->busca.getTexto())
				.toList();
		
		if (buscas==null || buscas.size()==0) return;
		
		var splits = splitBuscas(buscas, 8, true);
		
		try (var executor = Executors.newVirtualThreadPerTaskExecutor();){
			for (List<String> list : splits) {
				executor.execute(()-> (new ScraperDentalExpress(produtoRepository)).scrap(list));
				executor.execute(()-> (new ScraperDentalix(produtoRepository)).scrap(list));
			}
			
			executor.shutdown();
			
			boolean waitNoMore = false;
			do {
				try {
					waitNoMore = executor.awaitTermination(60, TimeUnit.SECONDS);
					System.out.println("46-[ScrapSerice] - Tarefas não concluídas...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!waitNoMore);
			System.out.println("51-[ScrapSerice] - Tarefas concluídas");
			
		}
	}
	
	// Divide a lista de buscas em várias outras mediante o tamanho n
	private List<List<String>> splitBuscas (List<String> buscas, int n, boolean shuffle) {
		
		var lista = new ArrayList<>(buscas);		
		if(shuffle) Collections.shuffle(lista);
		
		int listSize = lista.size(); //368
		int chunckSize = listSize / n; //368/10 = 36
		int modList = listSize % chunckSize; //8
		int complement = modList == 0 ? 0 : 1;
		int totalSplits = n + complement;
		
		List<List<String>> splits = new ArrayList<>();
		for (int i = 0; i < totalSplits; i++) {
			splits.add(new ArrayList<String>());
		}
		
		for (int i = 0; i < lista.size(); i++) {
			String texto = lista.get(i).replace(' ', '+');
			splits.get(i/chunckSize).add(texto);
		}
		return splits;
	}

}
