package com.tradingtols.br.scraper.model.entity.adapters;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.ProdutoDentaleader;
import com.tradingtols.br.scraper.model.entity.responses.DentaLeaderSearchResponse.Result;
import com.tradingtols.br.scraper.service.Companias;

@Component
public class DentaLearderEntityAdapter implements ResponseToEntityAdpater<ProdutoDentaleader, Result>{

	@Override
	public ProdutoDentaleader toEntity(Result resp) {
		// TODO rever a description pq pode ser outro campo da response
		return new ProdutoDentaleader(
				0L, 
				Companias.DENTALEADER.getNome(), 
				resp.title,
				resp.id, 
				resp.link, 
				resp.price, 
				resp.image_link, 
				"",
				new Date());
	}
}
