package com.tradingtols.br.scraper.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "busca_padrao")
public class BuscaPadrao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String texto;
	
	public BuscaPadrao() {}
	
	public BuscaPadrao(Long id, String texto) {
		super();
		this.id = id;
		this.texto = texto;
	}
	public Long getId() {
		return id;
	}
	public String getTexto() {
		return texto;
	}
}
