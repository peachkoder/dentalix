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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingtols.br.scraper.model.entity.adapters.DouromedEntityAdapter;
import com.tradingtols.br.scraper.model.entity.responses.CliniclicSearchResponse;
import com.tradingtols.br.scraper.model.entity.responses.DouromedSearchResponse;

@Service
public class DouromedCurlScraper extends CurlScraper implements Scraper {
	
	@Autowired
	private DouromedEntityAdapter adapter;

	private static final String URL = "https://nl6s3msh3y-dsn.algolia.net/1/indexes/cc_products_production/"
			+ "query?x-algolia-agent=Algolia%20for%20JavaScript%20(4.17.0)%3B%20Browser%20(lite)&"
			+ "x-algolia-api-key=ff77fed04904de5c8a442fbe27889e6d&x-algolia-application-id=NL6S3MSH3Y";

	@Override
	public void scrap(String search) {
		final String searchCleaned = search.trim().replace(" ", "+");

		List<DouromedSearchResponse.Product> list = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			List<DouromedSearchResponse.Product> gTag = null;
			DouromedSearchResponse response = null;			
			int page = 0;
			
			do {
				page++;
				response = mapper.readValue(sendHttpRequest(searchCleaned, page),DouromedSearchResponse.class);
				if (response==null) break;
				gTag = response.getGtag();
				if(!list.addAll(gTag)) break;
				
//				System.out.println("gTag size = " + gTag.size());
				
			} while (gTag != null && gTag.size() > 0);
			System.out.println(response);
			
			System.out.println("\nPages = " + page);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("list size = " + list.size());
//		list.stream().forEach(prod -> System.out.println("\n" + prod.toString()));
		list.stream().forEach(prod -> repository.save(adapter.toEntity(prod)));

	}

	private String sendHttpRequest(String search, int page) {
		try {
			URL url = new URL("https://www.douromed.com/ajax/search-produto/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
			conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			conn.setRequestProperty("Origin", "https://www.douromed.com");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Referer",
					"https://www.douromed.com/?gad_source=1&gclid=EAIaIQobChMItbGesJiviQMV2xAGAB0VCAL8EAMYAiAAEgL5VPD_BwE");
			conn.setRequestProperty("Cookie",
					"dmstore=7d80bdd36eebe3169d261a2ccdf036f9; user-dmstore=jXV716021%2BIdp7WfOGfHvno4xwfsmzDZECe2hAnlZgVXTGitytoWICaEf8g5MQ1YxAr4Ztzm1hJKgjh6P1Z4a%2BPbDTKzE8HdqsqnEaP0fQcR30mQ3X0nJwFotD7wrBVLTUKO0KwUacCbY5XhqpKU41MIa5JVlobwf%2BdA3HBnZNESE6sm5C77wvO3xEiVQC6%2BeZXjJBm76QGGzumEMzOCXOl%2FFELz9HuE3lOppyM9i3AJ2wUZgxJ6xzysUwuRHFFPGp3KCKNEkuTK1ytNn%2FsVMA%3D%3D; _pk_id.1019859.6fec=772616a292fc033b.1730058672.2.1730107171.1730107168.; _pk_ref.1019859.6fec=%5B%22%22%2C%22%22%2C1730107168%2C%22https%3A%2F%2Fwww.google.com%2F%22%5D; _gcl_aw=GCL.1730107172.EAIaIQobChMItbGesJiviQMV2xAGAB0VCAL8EAMYAiAAEgL5VPD_BwE; _gcl_gs=2.1.k1$i1730107171$u161893417; _gcl_au=1.1.1498893292.1730058672; _ga=GA1.1.189836460.1730058672; _gid=GA1.2.466430414.1730058672; _gac_UA-189845733-1=1.1730107171.EAIaIQobChMItbGesJiviQMV2xAGAB0VCAL8EAMYAiAAEgL5VPD_BwE; _ga_EPPT3YG3CJ=GS1.1.1730107167.2.1.1730107171.56.0.0; _fbp=fb.1.1730058672776.163107446370479125; _clck=1u4rv54%7C2%7Cfqe%7C0%7C1761; _clsk=182zka8%7C1730107172198%7C3%7C1%7Cs.clarity.ms%2Fcollect; __zlcmid=1ORn7reKgLpAaCR; _pk_ses.1019859.6fec=*; _gat_gtag_UA_189845733_1=1");
			conn.setRequestProperty("Sec-Fetch-Dest", "empty");
			conn.setRequestProperty("Sec-Fetch-Mode", "cors");
			conn.setRequestProperty("Sec-Fetch-Site", "same-origin");

			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			String payload = String
					.format("obterHtml=false&page=%s&order=0&promo=0&outlet=0&favorite=0&novidade=0&"
							+ "exclusive=0&breadcrumbs=https://www.douromed.com/search/&price=true&"
							+ "num_produtos=3&searchText=%s&categoria=0&new_url=https://www.douromed.com/", page, search);

			writer.write(payload);
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
				
//				System.out.println(response.toString());
				// Imprime a resposta decodificada
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
