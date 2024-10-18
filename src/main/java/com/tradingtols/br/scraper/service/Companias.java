package com.tradingtols.br.scraper.service;

public enum Companias {
	DENTALEADER ("DentaLeader"),
	DENTALIX ("Dentalix"),
	DENTALEXPRESS ("Dentalexpress");

	String nome = "";
	Companias(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
}
