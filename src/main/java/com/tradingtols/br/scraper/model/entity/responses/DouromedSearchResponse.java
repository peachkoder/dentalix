package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

public class DouromedSearchResponse {

	private List<Product> gtag = new ArrayList<>();
	private int minimo; // 12,
	private int maximo; // 56,
	private int minimo_search; // 12,
	private int maximo_search; // 56,
	//private String html;
	/*
	 * A ser usado no futuro
	 */
//	   @JsonIgnore private List<Idioma> idioma;
//	   @JsonIgnore private String html;

	public DouromedSearchResponse() {}

	public List<Product> getGtag() {
		return gtag;
	}

	public int getMinimo() {
		return minimo;
	}

	public int getMaximo() {
		return maximo;
	}

	public int getMinimo_search() {
		return minimo_search;
	}

	public int getMaximo_search() {
		return maximo_search;
	}
	
//	public String getHtml() {
//		return html;
//	}

	@Override
	public String toString() {
		return "DouromedSearchResponse [gtag=" + gtag + ", minimo=" + minimo + ", maximo=" + maximo + ", minimo_search="
				+ minimo_search + ", maximo_search=" + maximo_search + "]";
	}

	public static class Product {
        public int id; // 7291,
        public String name; // public String Cureta Gracey 1807-14public String ,
        public String brand; // public String Asa Dentalpublic String ,
        public String category; // public String Produtos\/Instrumental\/Periodontia\/public String
		public Product() {}
		@Override
		public String toString() {
			return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", category=" + category + "]";
		}
        
	}
}
