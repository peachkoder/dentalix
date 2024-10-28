package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class CliniclicSearchResponse {

	private List<Hit> hits = new ArrayList<>();
	private int nbHits; // 2003,
	private int page; // 0,
	private int nbPages; // 34,
	private int hitsPerPage; // 30,
	

	/*
	 * Usar no futuro, há outros campos a serem adicionados.
	 */
	
	// @JsonIgnore private String queryAfterRemoval; // //@JsonIgnore private String
	// LIMAS//@JsonIgnore private String ,
	// @JsonIgnore private String exhaustiveFacetsCount; // true,
	// @JsonIgnore private String exhaustiveFacetValues; // false,
	// @JsonIgnore private String exhaustiveNbHits; // true,
	// @JsonIgnore private String exhaustiveTypo; // true,

	// @JsonIgnore private String query; // //@JsonIgnore private String
	// LIMAS//@JsonIgnore private String ,

	// @JsonIgnore private String params; // //@JsonIgnore private String
	// query=LIMAS&userToken=anonymous&page=0&hitsPerPage=30&facets=%5B%22*%22%5D&filters=ProductDistributor.Markets%3A%27ckPor%27&getRankingInfo=true&clickAnalytics=true&analytics=true&attributesToRetrieve=%5B%22Categories%22%2C%22DateCreated%22%2C%22Description%22%2C%22Description_PT%22%2C%22ManufacturerName%22%2C%22MarketPrice%22%2C%22Name%22%2C%22Name_PT%22%2C%22Name_FR%22%2C%22PriceCC%22%2C%22ProductDistributor.DistributorHasStock%22%2C%22ProductDistributor.DistributorId%22%2C%22ProductDistributor.DistributorProductId%22%2C%22ProductDistributor.MarketPrice%22%2C%22ProductDistributor.ProductDistributorName%22%2C%22ProductDistributor.Markets%22%2C%22ProductDistributor.Stock%22%2C%22ProductDistributor.TopSales%22%2C%22ProductDistributor.TrustedStock%22%2C%22ProductDistributor.ExclusiveToClinics%22%2C%22ProductId%22%2C%22ProductImage%22%2C%22Reference%22%2C%22TotalPedidosHechos%22%2C%22objectID%22%2C%22_tags%22%5D&attributesToHighlight=%5B%5D//@JsonIgnore
	// private String ,
	// @JsonIgnore private String queryID; // //@JsonIgnore private String
	// c316d7666ac708fc2aeafccc479416b4//@JsonIgnore private String ,
	// @JsonIgnore private String serverUsed; // //@JsonIgnore private String
	// c27-eu-1.algolia.net//@JsonIgnore private String ,
	// @JsonIgnore private String indexUsed; // //@JsonIgnore private String
	// cc_products_production//@JsonIgnore private String ,
	// @JsonIgnore private String parsedQuery; // //@JsonIgnore private String
	// limas//@JsonIgnore private String ,
	// @JsonIgnore private String timeoutCounts; // false,
	// @JsonIgnore private String timeoutHits; // false,

	// @JsonIgnore private List<Object> exhaustive; //tem que ser implementado.
	
	public CliniclicSearchResponse() {}	

	public List<Hit> getHits() {
		return hits;
	}

	public int getNbHits() {
		return nbHits;
	}

	public int getPage() {
		return page;
	}

	public int getNbPages() {
		return nbPages;
	}

	public int getHitsPerPage() {
		return hitsPerPage;
	}

	@JsonIgnoreProperties
	public static class Hit {
		
		public String Name; // public String PROTAPER GOLD 25 mm set iniciacion surtido 6 udpublic String ,
        public String Name_PT; // public String Conjunto inicial PROTAPER GOLD 25 mm sortido 6 peças.public String ,
        public String Reference; // public String A0409225G0103public String ,
        public float PriceCC; // 48.68,
        public String ManufacturerName; // public String DENTSPLY MAILLEFER SIRONApublic String ,
        public String ProductId; // public String 458092public String ,
        public String ProductImage; // public String https://cdn.clubclinico.com/products/458092.jpegpublic String ,
        public String Description; // public String PROTAPER GOLD 25 mm set iniciacion surtido 6 ud La misma técnica que Protaper Universal, ahora con mayor flexibilidad. La metalurgia visiblemente avanzada de PROTAPER GOLD™ crea una diferencia que se puede ver y notar. Esto es gracias a que las limas PROTAPER GOLD tienen exactamente la misma geometría que PROTAPER® UNIVERSAL, pero ofrecen una mayor flexibilidad y resistencia a la fatiga clínica. Esto es especialmente importante en las limas de finalización, cuando se instrumentan conductos curvados en la zona apical. *El surtido contiene las limas SX, S1, S2, F1, F2 y F3. Descargar Ficha técnicapublic String ,
        public String Description_PT; // public String Conjunto inicial PROTAPER GOLD 25 mm sortido 6 peças.public String ,
        public String DateCreated; // public String 2021-10-15T08:59:28.4476409public String ,
        public String objectID; // public String 458092public String ,
//        @JsonIgnore public String TotalPedidosHechos; // 86,
//        @JsonIgnore public String Name_FR; // public String PROTAPER GOLD 25 mm starter set assortment 6 pcs.public String ,
        /*
         * Há outros campos a inserir/
         */
		public Hit() {}
		@Override
		public String toString() {
			return "Hit [Name=" + Name + ", Name_PT=" + Name_PT + ", Reference=" + Reference + ", PriceCC=" + PriceCC
					+ ", ManufacturerName=" + ManufacturerName + ", ProductId=" + ProductId + ", ProductImage="
					+ ProductImage + ", Description=" + Description + ", Description_PT=" + Description_PT
					+ ", DateCreated=" + DateCreated + ", objectID=" + objectID + "]";
		}
	}
}
