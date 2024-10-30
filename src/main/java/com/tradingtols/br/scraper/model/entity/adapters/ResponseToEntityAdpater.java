package com.tradingtols.br.scraper.model.entity.adapters;

public interface ResponseToEntityAdpater<T, K> {
	
	T toEntity(K resp);

}
