package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import com.tradingtols.br.scraper.model.entity.ProdutoDouromed;
import com.tradingtols.br.scraper.model.entity.responses.DouromedSearchResponse.Product;
import com.tradingtols.br.scraper.service.Companias;

public class DouromedEntityAdapter implements ResponseToEntityAdpater<ProdutoDouromed, Product>{

	@Override
	public ProdutoDouromed toEntity(Product resp) {
		// TODO Muito o que fazer nesta classe. A resposta do servidor no momento não oferece informação suficiente para completar esta classe.
		return new ProdutoDouromed(
			0L,
			Companias.DOUROMED.getNome(),
			resp.name,
			String.valueOf(resp.id),
			"","","","", 
			new Date()
		);
	}

}
