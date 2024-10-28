package com.tradingtols.br.scraper.model.entity.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DentaLeaderSearchResponse {

	private String custom_results_id;
	@JsonIgnore
	private String facets;
	private String hashid;
	private String max_score;
	private String page;
	private String query;
	private String query_counter;
	private String query_name;
	private int results_per_page;
	private int total;
	private int total_found;
	private List<Result> results = new ArrayList<>();
	
	public DentaLeaderSearchResponse() {super();}

	public DentaLeaderSearchResponse(String custom_results_id, String facets, String hashid, String max_score,
			String page, String query, String query_counter, String query_name, int results_per_page, int total,
			int total_found, List<Result> results) {
		super();
		this.custom_results_id = custom_results_id;
		this.facets = facets;
		this.hashid = hashid;
		this.max_score = max_score;
		this.page = page;
		this.query = query;
		this.query_counter = query_counter;
		this.query_name = query_name;
		this.results_per_page = results_per_page;
		this.total = total;
		this.total_found = total_found;
		this.results = results;
	}



	public String getCustom_results_id() {
		return custom_results_id;
	}

	public String getFacets() {
		return facets;
	}

	public String getHashid() {
		return hashid;
	}

	public String getMax_score() {
		return max_score;
	}

	public String getPage() {
		return page;
	}

	public String getQuery() {
		return query;
	}

	public String getQuery_name() {
		return query_name;
	}

	public int getResults_per_page() {
		return results_per_page;
	}

	public int getTotal() {
		return total;
	}

	public int getTotal_found() {
		return total_found;
	}

	public List<Result> getResults() {
		return results;
	}
	
	public String getQuery_counter() {
		return query_counter;
	}

	@Override
	public String toString() {
		return "ProdutoDentalixSearchResponse [custom_results_id=" + custom_results_id + ", facets=" + facets
				+ ", hashid=" + hashid + ", max_score=" + max_score + ", page=" + page + ", query=" + query
				+ ", query_name=" + query_name + ", result_per_page=" + results_per_page + ", total=" + total
				+ ", total_found=" + total_found + ", results=" + results.toString() + "]";
	}



	public static class Result {
		public String add_to_cart;
		public String description;
		public String dfid;
		public String df_rating;
		public String id;
		public String image_link;
		public String link;
		public String price;
		public String sale_price;
		public String title;
		public String type;
		
		@Override
		public String toString() {
			return "Result [add_to_cart=" + add_to_cart + ", description=" + description + ", dfid=" + dfid
					+ ", df_rating=" + df_rating + ", id=" + id + ", image_link=" + image_link + ", link=" + link
					+ ", price=" + price + ", sale_price=" + sale_price + ", title=" + title + ", type=" + type + "]";
		}
		
	}
}
