package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DentalibericaSearchResponse {

	private Pagination pagination; // 
	private List<Product> products = new ArrayList<>(); 
	private String current_url; // 

	//@JsonIgnore private List<Object> sort_orders; // 
	//@JsonIgnore private String label; // 
	//@JsonIgnore private String result; // 
	//@JsonIgnore private String rendered_products_top;
	//@JsonIgnore private String rendered_products; // 
	//@JsonIgnore private String rendered_products_bottom; // 
	//@JsonIgnore private String rendered_facets;
	//@JsonIgnore private String rendered_active_filters;
	//@JsonIgnore private boolean js_enabled;
	
	public DentalibericaSearchResponse() {}
	
	public Pagination getPagination() {
		return pagination;
	}

	public List<Product> getProducts() {
		return products;
	}

	public String getCurrent_url() {
		return current_url;
	}

	@Override
	public String toString() {
		return "DentalibericaSearchResponse [pagination=" + pagination + ", current_url="
				+ current_url + ", products=" + products +  "]";
	}



	@JsonIgnoreProperties
	public static class Pagination {
		public int total_items;
		public int current_page;
		public int pages_count;

		/*
		 *  A SER USADO NO FUTURO
		 */
		//@JsonIgnore public String  String items_shown_from; // 13,
		//@JsonIgnore public String  String items_shown_to; // 24,
		//@JsonIgnore public String  String should_be_displayed; // true
		//@JsonIgnore public String  String pages; // 
		
		public Pagination() {}

		@Override
		public String toString() {
			return "Pagination [total_items=" + total_items + ", pages_count=" + pages_count + ", current_page="
					+ current_page + "]";
		}

		
		
	}

	@JsonIgnoreProperties
	public static class Product {

		public String add_to_cart_url; 
		public String url; 
		public String canonical_url; 
		public String id_product; // public String 25147public String ,
		public String reference; // public String VO258public String ,
		public String description_short; // public String public String ,
		public String link_rewrite; // public String liga\u00e7\u00e3o-cer\u00e2mica-5-mlpublic String ,
		public String name; // public String LIGA\u00c7\u00c3O CER\u00c2MICA 5 mlpublic String ,
		public String manufacturer_name; // public String VOCOpublic String ,
		public String position; // public String 1public String ,
		public String link; 
		public String rate; // 10,
		public String tax_name; // public String O IVA \u00c9 10%public String ,
		public String price; // public String 69,65\u00a0\u20acpublic String ,
		public String unit_price; // public String public String ,
		public String price_amount; // 69.65,
		public String regular_price_amount; // 69.65,
		public String regular_price; // public String 69,65\u00a0\u20acpublic String ,
		public Cover cover;
		
		/*
		 * A ser usado no futuro
		 */
		// @JsonIgnore public String String labels;
		// @JsonIgnore public String List<String> main_variants;
		// @JsonIgnore public String String active; ////@JsonIgnore public String String
		// 1public String ,
		// @JsonIgnore public String String has_discount; // false,
		// @JsonIgnore public String String discount_type; // null,
		// @JsonIgnore public String String discount_percentage; // null,
		// @JsonIgnore public String String discount_percentage_absolute; // null,
		// @JsonIgnore public String String discount_amount; // null,
		// @JsonIgnore public String String discount_to_display; // null
		
		public Product() {}

		@Override
		public String toString() {
			return "Product [add_to_cart_url=" + add_to_cart_url + ", url=" + url + ", canonical_url=" + canonical_url
					+ ", id_product=" + id_product + ", reference=" + reference + ", description_short="
					+ description_short + ", link_rewrite=" + link_rewrite + ", name=" + name + ", manufacturer_name="
					+ manufacturer_name + ", position=" + position + ", link=" + link + ", rate=" + rate + ", tax_name="
					+ tax_name + ", price=" + price + ", unit_price=" + unit_price + ", price_amount=" + price_amount
					+ ", regular_price_amount=" + regular_price_amount + ", regular_price=" + regular_price
					+ ", cover=" + cover + "]";
		}
		
		@JsonIgnoreProperties
		public static class Cover {
			public Data large;
			public Cover() {}
			@Override
			public String toString() {
				return "Cove - large=" + large;
			}

			@JsonIgnoreProperties
			public static class Data {
				public String url;
				public int width;
				public int height;
				public Data() {}
				@Override
				public String toString() {
					return "[url=" + url + ", width=" + width + ", height=" + height + "]";
				}				
			}
			
			
		}
	}
}
