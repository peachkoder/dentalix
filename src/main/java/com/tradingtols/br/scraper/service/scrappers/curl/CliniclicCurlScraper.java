package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingtols.br.scraper.model.entity.responses.CliniclicSearchResponse;

@Service
public class CliniclicCurlScraper extends CurlScraper implements Scraper {

	private static final String URL = "https://nl6s3msh3y-dsn.algolia.net/1/indexes/cc_products_production/"
			+ "query?x-algolia-agent=Algolia%20for%20JavaScript%20(4.17.0)%3B%20Browser%20(lite)&"
			+ "x-algolia-api-key=ff77fed04904de5c8a442fbe27889e6d&x-algolia-application-id=NL6S3MSH3Y";

	@Override
	public void scrap(String search) {
		final String searchCleaned = search.trim().replace(" ", "+");

		List<CliniclicSearchResponse.Hit> list = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			CliniclicSearchResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					CliniclicSearchResponse.class);
			
			list.addAll(response.getHits());
			
			System.out.println("\nPages = " + response.getNbPages());
			
			int nPages = response.getNbPages();
			System.out.println("nPages = " + nPages);
			IntStream.range(2, nPages).parallel().forEach(i -> {
				try {
					var res = mapper.readValue(sendHttpRequest(searchCleaned, i), CliniclicSearchResponse.class);
					list.addAll(res.getHits());
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();

				}
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		list.stream()
		.map(hit-> {
			final var name = hit.Name;
			if (name!= null && name.toLowerCase().contains("limas")){
				hit.Name_PT = hit.Name_PT.replace("FICHEIROS", "LIMAS");
				hit.Description = hit.Description.replace("FICHEIROS", "LIMAS");
				hit.Description_PT = hit.Description_PT.replace("FICHEIROS", "LIMAS");
			}
			return hit;
		})
		.forEach(hit -> System.out.println("\n" + hit.toString()));

	}

	private String sendHttpRequest(String search, int page) {
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("x-algolia-usertoken", "anonymous");
			conn.setRequestProperty("Origin", "https://cliniclic.com");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Referer", "https://cliniclic.com/");
			conn.setRequestProperty("Sec-Fetch-Dest", "empty");
			conn.setRequestProperty("Sec-Fetch-Mode", "cors");
			conn.setRequestProperty("Sec-Fetch-Site", "cross-site");

			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			final String str = "{\"query\":\"%s\",\"userToken\":\"anonymous\",\"page\":%s,\"hitsPerPage\":30,"
					+ "\"facets\":[\"*\"],\"filters\":\"ProductDistributor.Markets:'ckPor'\","
					+ "\"getRankingInfo\":true,\"clickAnalytics\":true,\"analytics\":true,"
					+ "\"attributesToRetrieve\":[\"Categories\",\"DateCreated\",\"Description\","
					+ "\"Description_PT\",\"ManufacturerName\",\"MarketPrice\",\"Name\",\"Name_PT\","
					+ "\"Name_FR\",\"PriceCC\",\"ProductDistributor.DistributorHasStock\","
					+ "\"ProductDistributor.DistributorId\",\"ProductDistributor.DistributorProductId\","
					+ "\"ProductDistributor.MarketPrice\",\"ProductDistributor.ProductDistributorName\","
					+ "\"ProductDistributor.Markets\",\"ProductDistributor.Stock\",\"ProductDistributor.TopSales\","
					+ "\"ProductDistributor.TrustedStock\",\"ProductDistributor.ExclusiveToClinics\","
					+ "\"ProductId\",\"ProductImage\",\"Reference\",\"TotalPedidosHechos\",\"objectID\","
					+ "\"_tags\"],\"attributesToHighlight\":[]}";
			writer.write(String.format(str, search, page));
			writer.flush();
			writer.close();
			conn.getOutputStream().close();

			// Processa a resposta com tratamento de encoding gzip/deflate/br
			InputStream inputStream = conn.getInputStream();
			String encoding = conn.getContentEncoding();
			if ("gzip".equalsIgnoreCase(encoding)) {
				inputStream = new GZIPInputStream(inputStream);
			} else if ("deflate".equalsIgnoreCase(encoding)) {
				inputStream = new InflaterInputStream(inputStream);
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
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
