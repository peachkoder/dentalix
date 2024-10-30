package com.tradingtols.br.scraper.model.entity;

import java.util.Date;

public class ProdutoFoquimDental extends Produto {

	public ProdutoFoquimDental() {
		super();
	}

	public ProdutoFoquimDental(Long id, String compania, String description, String externalId, String link,
			String price, String srcImage, String brand, Date data) {
		super(id, compania, description, externalId, link, price, srcImage, brand, data);
	}

}
