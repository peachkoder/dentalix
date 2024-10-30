package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoMontellano;
import com.tradingtols.br.scraper.model.entity.responses.MontellanoSearchResponse.Product2;
import com.tradingtols.br.scraper.service.Companias;;

@Component
public class MontellanoEntityAdapter implements ResponseToEntityAdpater<ProdutoMontellano, Product2> {

	@Override
	public ProdutoMontellano toEntity(Product2 resp) {
		// TODO Rever o preço
		return new ProdutoMontellano(
			0L,
			Companias.MONTELLANO.getNome(),
			resp.description,
			resp.sku,
			resp.url,
			resp.regularPrice, 
			resp.image,
			resp.brand,
			new Date()
		);
	}

}
