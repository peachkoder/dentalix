package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.Produto;
import com.tradingtols.br.scraper.model.entity.ProdutoDentalix;
import com.tradingtols.br.scraper.model.entity.responses.DentalixSearchResponse.ProductData;
import com.tradingtols.br.scraper.service.Companias;

@Component
public class DentaltixEntityAdapter implements ResponseToEntityAdpater<ProdutoDentalix, ProductData>{

	@Override
	public ProdutoDentalix toEntity(ProductData resp) {
		// TODO rever o campo price - criar uma classe que manipula os preços e moeda
		return new ProdutoDentalix(
			0L,
			Companias.DENTALTIX.getNome(),
			resp.name,
			resp.sku,
			resp.url,
			String.valueOf(resp.price.recommended) + resp.price.currency,
			resp.image.variants.getFirst().url,
			resp.brand.name,
			new Date()
		);
	}

}
