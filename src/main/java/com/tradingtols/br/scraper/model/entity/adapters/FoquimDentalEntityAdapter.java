package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoFoquimDental;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse.Product;
import com.tradingtols.br.scraper.service.Companias;

@Component
public class FoquimDentalEntityAdapter implements ResponseToEntityAdpater<ProdutoFoquimDental, Product> {

	@Override
	public ProdutoFoquimDental toEntity(Product resp) {
		// TODO rever o mapeamento
		return new ProdutoFoquimDental(
			0L,
			Companias.FOQUIMDENTAL.getNome(),
			resp.name,
			resp.reference,
			resp.link,
			resp.price,
			resp.cover.large.url,
			resp.manufacturer_name,
			new Date()
		);
	}

}
