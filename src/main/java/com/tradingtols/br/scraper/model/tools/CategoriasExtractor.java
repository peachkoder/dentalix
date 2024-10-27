package com.tradingtols.br.scraper.model.tools;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.tradingtols.br.scraper.model.entity.categorias.MapCategorias;
import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;


public class CategoriasExtractor {
	
	private static AtomicReference<String> normalizado = new AtomicReference<>(); 
			

	public static List<ProdutoCategoria> extract(String texto) {

		if (texto == null || texto.isBlank())
			return null;
		
		String temp = Normalizer.normalize(texto, Normalizer.Form.NFD).toLowerCase();
		// Usa regex para remover os caracteres diacríticos (acentos)
	     temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");		
	    normalizado.set(temp);
	    
		List<ProdutoCategoria> result = new ArrayList<>();
		Map<ProdutoCategoria, List<String>> map = MapCategorias.getMap();
		
		for (Map.Entry<ProdutoCategoria, List<String>> entry : map.entrySet()) {
			ProdutoCategoria key = entry.getKey();
			List<String> valores = entry.getValue();
			if (valores.stream().filter(s -> normalizado.get().contains(s)).count()>0) {
				result.add(key);
			};
		}
		return result;
	}
}
