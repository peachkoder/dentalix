package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoDentaliberica;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse.Product;
import com.tradingtols.br.scraper.service.Companias;

@Component
public class DentalibericaEntityAdapter implements ResponseToEntityAdpater<ProdutoDentaliberica, Product> {

	@Override
	public ProdutoDentaliberica toEntity(Product resp) {

		//TODO: rever os campos link e imgSrc
		return new ProdutoDentaliberica(
				0L, 
				Companias.DENTALIBERICA.getNome(), 
				resp.name.isBlank()?resp.description_short:resp.name, 
				resp.id_product, 
				resp.link, 
				resp.price.isBlank()?resp.price_amount:resp.price, 
				resp.url, 
				resp.manufacturer_name, 
				new Date()
		);
		
	}
}
