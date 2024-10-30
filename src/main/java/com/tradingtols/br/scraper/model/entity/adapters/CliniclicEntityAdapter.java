package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoCliniclic;
import com.tradingtols.br.scraper.model.entity.responses.CliniclicSearchResponse.Hit;
import com.tradingtols.br.scraper.service.Companias;;

@Component
public class CliniclicEntityAdapter implements ResponseToEntityAdpater<ProdutoCliniclic, Hit>{

	@Override
	public ProdutoCliniclic toEntity(Hit resp) {
		// TODO Criar uma classe para manipular os preços e moedas
		return new ProdutoCliniclic(
				0L, 
				Companias.CLINICLIC.getNome(),
				resp.Name_PT, 				
				resp.objectID, 
				resp.Reference, 
				String.valueOf(resp.PriceCC) + " €", 
				resp.ProductImage, 
				resp.ManufacturerName, 
				new Date()
		);
	}
}
