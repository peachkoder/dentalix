package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MontellanoSearchResponse {
	
	private int total;
	private List<Product2> products = new ArrayList<>();
	@JsonIgnore
	private List<String> filters;
	@JsonIgnore
	private List<String> aggregates;
	@JsonIgnore
	private List<String> filterVisibility;
	@JsonIgnore
	private List<String> filtersWithCount;
	@JsonIgnore
	private String lucky_url;
	@JsonIgnore
	private List<String> banners;
	
	public MontellanoSearchResponse() {	}
	
	public List<Product2> getProducts() {
		return products;
	}

	public int getTotal() {
		return total;
	}

	@JsonIgnoreProperties
	public static class Product {
		public String name;
        public String highlightName;//null,
        public String sku;//null,
        public String brand;//public String KERRpublic String ,
        public float regularPrice;//public String 12,95 €public String ,
        public float minRegularPrice;//public String 12,95 €public String ,
        public float minimumPrice;//null,
        public String image;//public String https;////d3tfk74ciyjzum.cloudfront.net/proclinic-pt/products/0460n_v2.jpgpublic String ,
        public String thumb;//public String https;////d3tfk74ciyjzum.cloudfront.net/proclinic-pt/products/thumb-0460n_v2.jpgpublic String ,
        public String url;//public String limas-k-flex-n45-80public String ,
        public boolean haveChildrenDifferentPrices;//false,
        /*
         * A usar no futuro
         */
//        @JsonIgnore public String offer;//[
//        @JsonIgnore public String catalogPages;//[
//        @JsonIgnore public boolean isRequestQuote;//false,
//        @JsonIgnore public boolean isHiddenPrice;//false,
//        @JsonIgnore public boolean isDoctorExclusive;//false,
//        @JsonIgnore public boolean isAnesthesiaVisibility;//false
		public Product() {}
		@Override
		public String toString() {
			return "Product [name=" + name + ", highlightName=" + highlightName + ", sku=" + sku + ", brand=" + brand
					+ ", regularPrice=" + regularPrice + ", minRegularPrice=" + minRegularPrice + ", minimumPrice="
					+ minimumPrice + ", image=" + image + ", thumb=" + thumb + ", url=" + url
					+ ", haveChildrenDifferentPrices=" + haveChildrenDifferentPrices + "]";
		}
	}
	
	@JsonIgnoreProperties
	public static class Product2 {
		public String sku; //public String 1027923public String ,
		public String publicSku; //public String 1027923public String ,
		public String brand; //public String GARRISON@JsonIgnore  public String ,
		public String brandImage; //public String https://d3tfk74ciyjzum.cloudfront.net/proclinic-dc/brands/garrison.pngpublic String ,
		public String type; //public String simplepublic String ,
		public String outlet; //false,
		public String description; //public String @JsonIgnore  public String ,
		public String family; //public String ORTODONTIApublic String ,
		public String image; //public String https://d3tfk74ciyjzum.cloudfront.net/proclinic-pt/products/L8089_2.jpgpublic String ,
		public String thumb; //public String https://d3tfk74ciyjzum.cloudfront.net/proclinic-pt/products/thumb-L8089_2.jpgpublic String ,
		public String url; //public String cabo-limas-sunset-strippublic String ,
		public boolean isOutOfStock; //false,
		public String regularPrice; //  public String 34,01 €  public String ,
		public String minRegularPrice; //  public String 34,01 €  public String ,
		public String specialPrice; //null,
		public String minimumPrice; //null,
		// 
		//A usar no futuro
//		@JsonIgnore  public int savePercent; //100,
//		@JsonIgnore  public String brandSlogan; //public String public String ,
//        @JsonIgnore  public boolean isEco; //false,
//        @JsonIgnore  public boolean showGiftIcon; //false,
//        @JsonIgnore  public boolean hasDiscount; //false,
//        @JsonIgnore  public String offerKey; //null,
//        @JsonIgnore  public boolean hasExcludeOffer; //false,
//        @JsonIgnore  public String advice; //null,
//        @JsonIgnore  public String name; //public String CABO LIMAS SUNSET STRIP@JsonIgnore  public String ,
//        @JsonIgnore  @JsonProperty("new") public boolean _new; //false,
//        @JsonIgnore  public String content; //null,
//        @JsonIgnore  public boolean isDoctorExclusive; //false,
//        @JsonIgnore  public boolean isMinibio; //false,
//        @JsonIgnore  public boolean isExclusiveProfessionals; //false,
//        @JsonIgnore  public boolean isRequestQuote; //false,
//        @JsonIgnore  public boolean isAvailabilityOnRequest; //false,
//        @JsonIgnore  public boolean isAcrylic; //false,
//        @JsonIgnore  public int maxPerOrder; //9999,
//        @JsonIgnore  public List<String> offer; //[
//        @JsonIgnore  public String subfamily; //public String STRIPPING@JsonIgnore  public String ,
//        @JsonIgnore @JsonProperty("package") public String package_; //public String ENVASE 1 unidad@JsonIgnore  public String ,
//        @JsonIgnore  public String children; //null,
//        @JsonIgnore  public String haveChildrenDifferentPrices; //false,
//        @JsonIgnore  public String needCustomization; //false,
//        @JsonIgnore  public boolean isLupas; //false,
//        @JsonIgnore  public boolean isRequestDemo; //false,
//        @JsonIgnore  public boolean isNextRelease; //false,
//        @JsonIgnore  public String supplierOffer; //null,
//        @JsonIgnore  public boolean isHiddenPrice; //false,
//        @JsonIgnore  public boolean isAnesthesiaVisibility; //false,
//        @JsonIgnore  public String subfamilySapId; //public String 184@JsonIgnore  public String ,
//        @JsonIgnore  public String lockedText; //public String @JsonIgnore  public String 
		public Product2() {}
		@Override
		public String toString() {
			return "Product2 [sku=" + sku + ", publicSku=" + publicSku + ", brand=" + brand + ", brandImage="
					+ brandImage + ", type=" + type + ", outlet=" + outlet + ", description=" + description
					+ ", family=" + family + ", image=" + image + ", thumb=" + thumb + ", url=" + url
					+ ", isOutOfStock=" + isOutOfStock + ", regularPrice=" + regularPrice + ", minRegularPrice="
					+ minRegularPrice + ", specialPrice=" + specialPrice + ", minimumPrice=" + minimumPrice + "]";
		}
		
        
	}
}
