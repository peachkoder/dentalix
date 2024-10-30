
package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.brotli.dec.BrotliInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingtols.br.scraper.model.entity.adapters.FoquimDentalEntityAdapter;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse.Product;
import com.tradingtols.br.scraper.model.entity.responses.FoquimDentalSearchResponse;

@Service
public class FoquimDentalCurlScraper extends CurlScraper implements Scraper {
	
	@Autowired
	private FoquimDentalEntityAdapter adapter;

	@Override
	public void scrap(String search) {
		final String searchCleaned = search.trim().replace(" ", "+");

		final List<Product> products = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			FoquimDentalSearchResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					FoquimDentalSearchResponse.class);
			products.addAll(response.getProducts());

			AtomicBoolean isTooQuick = new AtomicBoolean(false);
			AtomicInteger waitTime = new AtomicInteger(2000);

			int nPage = response.getPagination().pages_count;
			System.out.println("nPage="+ nPage);
			IntStream.range(2, nPage + 1).parallel().forEach(page -> {
				int maxRequest = 4;
				do {
					try {
						if (isTooQuick.get()) {
							Thread.sleep(waitTime.get());
						}
						System.out.println("Pagina n.º = " + page);
						var res = mapper.readValue(sendHttpRequest(searchCleaned, page),
								FoquimDentalSearchResponse.class);
						products.addAll(res.getProducts());
						isTooQuick.set(false);
						waitTime.set(3000);
					} catch (JsonProcessingException e) {
						System.out.println("JsonProcessingException");
//						e.printStackTrace();
					} catch (InterruptedException e) {
						System.out.println("InterruptedException");
//						e.printStackTrace();
					} catch (IOException e) {
//								e.printStackTrace();
						System.out.println("IOException");
						isTooQuick.set(true);
						waitTime.getAndAccumulate(500, (acc, time) -> acc + time);
						System.out.println("WaitTime=" + waitTime.get());
					}
					maxRequest--;
				} while (isTooQuick.get() && maxRequest > 0);
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("list size = " + products.size());
		products.stream().forEach(prod -> repository.save(adapter.toEntity(prod)));
//		products.stream().forEach(prod -> System.out.println("\n" + prod.toString()));

	}

	private String sendHttpRequest(String search, int page) throws IOException {
		final String body = String.format("s=%s&resultsPerPage=100&ajax=true&page=%s", search, page);

		URL url = new URL("https://foquimdental.com/shop/module/iqitsearch/searchiqit");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");

		conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
		conn.setRequestProperty("Referer", "https://foquimdental.com/shop/module/iqitsearch/searchiqit?s=turbina");
		conn.setRequestProperty("Origin", "https://foquimdental.com");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Cookie","PrestaShop-40ab31366c3e7354aa74a83d61cb8116=def50200b1e9234c4ee3b941d948408c26bfaee008955cc25bd89b0ec794ca166e894c5f72ce3922fab6135dab3273cd10a538479df47732d62ca70f152f621685557633fc675196273082c8a711aa973a10f61fe460b4cb83a82050cf0adf44d735512257481b0ab303e247cbbd8fe8b79a31cf722344c0a8bc84ff0414b8fb94df8c1d9c4132a3d79a26cb270aee8bb5599bfe8cdf8b51ced084b696608c3963b0bc07c7de3a29ab970befc3b897a6ea1f0a5e07f6719ff4fac578730b924cad9c4b3df696fb618b3cd89daf1a5a0f69b3f91d16; __wpdm_client=b6075b922806e1e9026fa7cb0c602f7e; _gcl_aw=GCL.1730227222.EAIaIQobChMI1bvy6Ju0iQMVoZtoCR0_QjXIEAMYASAAEgJBuPD_BwE; _gcl_gs=2.1.k1$i1730227220$u133940505; _gcl_au=1.1.409534616.1730227222; _ga_Z41B84TBQY=GS1.1.1730227222.1.0.1730227228.54.0.0; _ga=GA1.2.1962937513.1730227222; _gid=GA1.2.809574868.1730227223; _gac_UA-72484857-1=1.1730227223.EAIaIQobChMI1bvy6Ju0iQMVoZtoCR0_QjXIEAMYASAAEgJBuPD_BwE; _gac_UA-114194559-1=1.1730227223.EAIaIQobChMI1bvy6Ju0iQMVoZtoCR0_QjXIEAMYASAAEgJBuPD_BwE; _ga_9DV25W2ZS3=GS1.2.1730227223.1.1.1730230928.60.0.0; _fbp=fb.1.1730227223340.443442936889479231; PHPSESSID=5281b20bcbf2b60522fba8930954e06b; iqitpopup_15904=1; _gat=1");
		conn.setRequestProperty("Sec-Fetch-Dest", "empty");
		conn.setRequestProperty("Sec-Fetch-Mode", "no-cors");
		conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Priority", "u=4");
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setRequestProperty("Cache-Control", "no-cache");

		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(body);
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
		} else if ("br".equalsIgnoreCase(encoding)) {
			inputStream = new BrotliInputStream(inputStream);
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

	}

}
