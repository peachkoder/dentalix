package com.tradingtols.br.scraper.service;

public enum Companias {
	DENTALEADER ("DentaLeader"),
	DENTALIX ("Dentalix"),
	DENTALEXPRESS ("Dentalexpress"), 
	HENRY_SCHEIN("Henry_Schein"), 
	MINHO_MEDICA("Minho_Medica"),
	MONTELLANO("Montellando");

	String nome = "";
	Companias(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
}
