package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tradingtols.br.scraper.model.entity.adapters.MontellanoEntityAdapter;
import com.tradingtols.br.scraper.model.entity.responses.DentalixSearchResponse;
import com.tradingtols.br.scraper.model.entity.responses.DentalixSearchResponse.ProductData;
import com.tradingtols.br.scraper.model.entity.responses.DentalixSearchResponse.ProductList;
import com.tradingtols.br.scraper.model.entity.responses.DentalixSearchResponse.ProductResponse;
import com.tradingtols.br.scraper.model.entity.responses.MontellanoSearchResponse;
import com.tradingtols.br.scraper.model.entity.responses.MontellanoSearchResponse.Product;
import com.tradingtols.br.scraper.model.entity.responses.MontellanoSearchResponse.Product2;

@Service
public class MontellanoCurlScraper extends CurlScraper  implements Scraper {
	
	@Autowired
	private MontellanoEntityAdapter adapter;

	private static final String URL = "https://www.montellano.pt/api/products?q=%s&p=%s&limit=100&"
										+"orderBy[_score]=desc&skip_filters=true&skip_featureds=true";
	
    
	@Override
	public void scrap(String search) {
		
		final String searchCleaned = search.trim().replace(" ", "+"); 
		final List<Product2> list = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			MontellanoSearchResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					MontellanoSearchResponse.class);
			list.addAll(response.getProducts());

			// paginação
			int nPages = response.getTotal() / 100;  // 100 -> max produtos por página
			int mod = response.getTotal() / 100;
			nPages = mod == 0 ? nPages : nPages + 1; 
			System.out.println("nPages = " + nPages);
			
			IntStream.range(2, nPages).parallel().forEach(i -> {
				try {
					var res = mapper.readValue(sendHttpRequest(searchCleaned, i), MontellanoSearchResponse.class);
					list.addAll(res.getProducts());
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//debug
//		list.stream().parallel().forEach(prod->System.out.println("\n"+prod.toString()));
		list.stream().parallel().forEach(prod-> repository.save(adapter.toEntity(prod)));
	}


	
	private String sendHttpRequest(String search, int page) {

		final String newURL = String.format(URL, search, page);
		final String referer = String.format("https://www.montellano.pt/products/search?q=%s&p=%s&limit=100&orderBy[_score]=desc", 
				search, page);
		
		try {
			URL url = new URL(newURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
	        conn.setRequestProperty("Accept", "application/json, text/plain, */*");
	        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
	        conn.setRequestProperty("Alt-Used", "www.montellano.pt");
	        conn.setRequestProperty("Connection", "keep-alive");
	        conn.setRequestProperty("Referer", referer);
	        conn.setRequestProperty("Cookie", "campaign=%7B%22accountid%22%3A%224042105190%22%2C%22device%22%3A%22c%22%2C%22network%22%3A%22x%22%2C%22campaign_name%22%3A%22%7Bcampaign_name%7D%22%2C%22campaign_type%22%3A%22search%22%2C%22geoloc%22%3A%2220866%22%2C%22gad_source%22%3A%221%22%2C%22gclid%22%3A%22EAIaIQobChMIzJzLq7quiQMV6YCDBx2NLgfYEAAYASAAEgLo8fD_BwE%22%2C%22is_stored_campaign%22%3Afalse%2C%22utm_medium%22%3A%22cpc%22%2C%22utm_term%22%3A%22%22%2C%22utm_content%22%3Anull%2C%22utm_source%22%3A%22google%22%2C%22campaign%22%3A%22SEM-GA-PMAX%22%2C%22utm_campaign%22%3A%2219771878410%22%2C%22created_at%22%3A1730028493%7D; session_id=b010110184eb38543dda3e63d37dd0868028e6e0; PHPSESSID_PROSHOP=t8ofr72pd95hr5efhr6v2ffc0m; _clck=1udx9lm%7C2%7Cfqd%7C0%7C1761; _gcl_aw=GCL.1730028541.EAIaIQobChMIzJzLq7quiQMV6YCDBx2NLgfYEAAYASAAEgLo8fD_BwE; _gcl_gs=2.1.k1$i1730028483$u93535489; _gcl_au=1.1.189802634.1730028485; _ga_EBTQSQT2MZ=GS1.1.1730028484.1.0.1730028540.4.0.0; _ga=GA1.1.1986572334.1730028485; _clsk=ywvkcg%7C1730028544689%7C3%7C1%7Cz.clarity.ms%2Fcollect; cookie_professional=true; cookies_consent_statistics=false; cookies_consent_marketing=false; cookie_options_accepted=true; _uetsid=88905240945611efab6c7fe98832dc02; _uetvid=88906de0945611efa7ac7be1d03bbf90");
	        conn.setRequestProperty("Sec-Fetch-Dest", "empty");
	        conn.setRequestProperty("Sec-Fetch-Mode", "cors");
	        conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
	        conn.setRequestProperty("Priority", "u=0");
	        conn.setRequestProperty("TE", "trailers");

	        InputStream inputStream = conn.getResponseCode() / 100 == 2
	                ? conn.getInputStream()
	                : conn.getErrorStream();
	        if ("gzip".equals(conn.getContentEncoding())) {
	            inputStream = new GZIPInputStream(inputStream);
	        }

			// Lê a resposta como UTF-8
			try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();
				// Imprime a resposta decodificada
				//System.out.println(response.toString());
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}


}
