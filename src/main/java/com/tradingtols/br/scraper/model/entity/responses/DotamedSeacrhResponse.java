package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DotamedSeacrhResponse {

	private List<Product> products = new ArrayList<>();
	@JsonIgnore private List<String> tax;
	@JsonIgnore private List<String> data;
	public DotamedSeacrhResponse() {}
	


	public List<Product> getProducts() {
		return products;
	}





	@JsonIgnoreProperties
	public static class Product {
		public int id; // 45116,
        public int parent_id; // 45116,
        public String title; //<strong>Lima<\/strong> CA NITI 9% ONE FLARE No.25 | Micromegapublic String ,
        public String excerpt; //public String ,
        public String link; //https:\/\/www.dotamedsaojoao.com\/produto\/lima-ca-niti-9-one-flare-micromega\/public String ,
        public String image; //https:\/\/www.dotamedsaojoao.com\/wp-content\/uploads\/2023\/12\/lima-one-flare-micromega-200x200.jpgpublic String ,
        public String price; //<span class=\public String electro-price\public String ><span class=\public String woocommerce-Price-amount amount\public String ><bdi><span class=\public String woocommerce-Price-currencySymbol\public String >&euro;<\/span>48.95<\/bdi><\/span> + IVA <\/span>public String ,
        public String on_sale; //public String ,
        public String sku; //public String ,
        public String stock_status; //public String ,
        public String featured; //public String ,
        public String f_price; //48.95public String ,
        /*
         * A usar no futuro
         */
//        @JsonIgnore public String f_rating; //0@JsonIgnore public String ,
//        @JsonIgnore public String f_reviews; // 0,
//        @JsonIgnore public boolean f_stock; // true,
//        @JsonIgnore public boolean f_sale; // false,
//        @JsonIgnore public List<postData> post_data; //implementar PostData
		public Product() {}
        
        
	}
}
