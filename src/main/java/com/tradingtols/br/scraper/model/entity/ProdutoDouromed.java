package com.tradingtols.br.scraper.model.entity;

import java.util.Date;

public class ProdutoDouromed extends Produto{

	public ProdutoDouromed() {
		super();
	}

	public ProdutoDouromed(Long id, String compania, String description, String externalId, String link, String price,
			String srcImage, String brand, Date data) {
		super(id, compania, description, externalId, link, price, srcImage, brand, data);
	}

}
