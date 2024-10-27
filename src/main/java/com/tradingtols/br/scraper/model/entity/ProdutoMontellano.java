package com.tradingtols.br.scraper.model.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="produtomontellano")
public class ProdutoMontellano extends Produto{
	
	public ProdutoMontellano() {super();}

	public ProdutoMontellano(Long id, String compania, String description, String externalId, String link,
			String price, String srcImage, String brand, Date data) {
		super(id, compania, description, externalId, link, price, srcImage, brand, data);
	}
}