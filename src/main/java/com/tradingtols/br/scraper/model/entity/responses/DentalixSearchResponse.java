package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DentalixSearchResponse {
	
	private String refId;
	@JsonIgnore
	private String filter;
	private ProductList productList;
	@JsonIgnore
	private List<String> recommenders = new ArrayList<>();
	
    public DentalixSearchResponse() {}
	
	public DentalixSearchResponse(String refId, String filter, ProductList productList) {
		super();
		this.refId = refId;
		this.filter = filter;
		this.productList = productList;
	}

	public String getRefId() {
		return refId;
	}

	public String getFilter() {
		return filter;
	}

	public ProductList getProductList() {
		return productList;
	}

	public List<String> getRecommenders() {
		return recommenders;
	}

	public static class ProductList {
		public String idType;
		public List<String> idList = new ArrayList<>();
		public int numFound;
		public int pages;
		
	}
	
	/* ----------------
	 * Product Response
	 * ----------------
	 */
	
	public static class ProductResponse {
		@JsonIgnore
		public boolean ok;
		public int status;
		@JsonIgnore
		public String statusCode;
		public List<ProductData> data = new ArrayList<>();
		public ProductResponse() {}
		public ProductResponse(boolean ok, int status, String statusCode, List<ProductData> data) {
			this.ok = ok;
			this.status = status;
			this.statusCode = statusCode;
			this.data = data;
		}
	}
	
	public static class ProductData {
        public String sku; //DTXFPP2-20,
        public String modelSku; //PN00033434,
        public String name; //Máscaras de Adultos EPI FFP2 Brancas,
        public String variant; //Pack 20 Unidades,
        public String url; //https; ////www.dentaltix.com/pt/varios/mascara-ffp2-n95-20-unidades,
        @JsonIgnore
        public int stock; //2,
        @JsonIgnore
        public boolean stockControl; //true,
        @JsonIgnore
        public int packQty; //1,
        public String promoTitle; //null,
        public int variationsCount; //3,
        @JsonIgnore
        public Rating rating;
        public Imagem image;
        public Brand brand;
        @JsonIgnore
        public boolean isNoPriceProduct;
        public Price price;
        @JsonIgnore
        public String deliveryTime;
        
        public ProductData() {}

		@Override
		public String toString() {
			return "ProductData [sku=" + sku + ", modelSku=" + modelSku + ", name=" + name + ", variant=" + variant
					+ ", url=" + url + ", packQty=" + packQty + ", promoTitle=" + promoTitle + ", variationsCount="
					+ variationsCount + ", image=" + image.toString() + ", brand=" + brand.toString() 
					+ ", price=" + price.toString() + "]";
		}

		public static class Price {
        	public float sales;
        	public float salesTaxed;
        	public float recommended;
        	public float recommendedTaxed;
        	public float discount;
        	public String currency;
			public Price() {}
			@Override
			public String toString() {
				return "Price [sales=" + sales + currency + ", salesTaxed=" + salesTaxed + currency + "]";
			}
        	
        }
        
        public static class Brand{
        	public String name;
			@Override
			public String toString() {
				return "Brand [name=" + name + "]";
			}
        }
        
        public static class Imagem {
        	public String alt;
        	public List<Variant> variants = new ArrayList<>();
        	
        	@Override
			public String toString() {
				return "Imagem [alt=" + alt + ", variants=" + variants + "]";
			}

			public static class Variant {
        		public String type;
        		public String url;
				@Override
				public String toString() {
					return "Data [type=" + type + ", url=" + url + "]";
				}
        	}
        }
        
        public static class Rating {
        	public static float ratingValue;
        	public static int ratingCount;
        }
	}

}
