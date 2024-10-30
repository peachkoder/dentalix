package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoDotamed;
import com.tradingtols.br.scraper.model.entity.responses.DotamedSeacrhResponse.Product;
import com.tradingtols.br.scraper.service.Companias;;

@Component
public class DotamedEntityAdpater implements ResponseToEntityAdpater<ProdutoDotamed, Product> {

	@Override
	public ProdutoDotamed toEntity(Product resp) {
		// TODO: rever se há como extrair a marca do produto
		return new ProdutoDotamed(
			0L,
			Companias.DOTAMED.getNome(),
			resp.title,
			String.valueOf(resp.id),
			resp.link,
			resp.price,
			resp.image,
			resp.excerpt, // rever se há como extrair a marca do produto
			new Date()
		);
	}

}
