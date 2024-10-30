package com.tradingtols.br.scraper.service;

public enum Companias {
	DENTALEADER ("DentaLeader"),
	DENTALTIX ("Dentaltix"),
	DENTALEXPRESS ("Dentalexpress"), 
	HENRY_SCHEIN("Henry_Schein"), 
	MINHO_MEDICA("Minho_Medica"),
	MONTELLANO("Montellando"), 
	DENTALIBERICA("DentalIberica"), 
	CLINICLIC ("Cliniclic"), 
	DOTAMED("Dotamed"), 
	DOUROMED("Douromed"), 
	FOQUIMDENTAL("FoquimDental");

	String nome = "";
	Companias(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
}
