package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.tradingtols.br.scraper.model.entity.ProdutoDentaliberica;
import com.tradingtols.br.scraper.model.entity.adapters.DentalibericaEntityAdapter;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse;
import com.tradingtols.br.scraper.model.entity.responses.DentalibericaSearchResponse.Product;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;

@Service
public class DentalibericaCurlScraper extends CurlScraper implements Scraper {

	@Autowired
	private DentalibericaEntityAdapter adapter;

	private static final String URL = "https://nl6s3msh3y-dsn.algolia.net/1/indexes/cc_products_production/"
			+ "query?x-algolia-agent=Algolia%20for%20JavaScript%20(4.17.0)%3B%20Browser%20(lite)&"
			+ "x-algolia-api-key=ff77fed04904de5c8a442fbe27889e6d&x-algolia-application-id=NL6S3MSH3Y";

	@Override
	public void scrap(String search) {
		final String searchCleaned = search.trim().replace(" ", "+");

		final List<Product> products = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			DentalibericaSearchResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					DentalibericaSearchResponse.class);

			AtomicBoolean isTooQuick = new AtomicBoolean(false);
			AtomicInteger waitTime = new AtomicInteger(2000);
			
			int nPage = response.getPagination().pages_count;
			IntStream.range(2, nPage + 1)
					.parallel()
					.forEach(page -> {
						int maxRequest = 4;
						do {
							try {
								if (isTooQuick.get()) {
									Thread.sleep(waitTime.get());
								}
								System.out.println("Pagina n.º = " + page);
								var res = mapper.readValue(sendHttpRequest(searchCleaned, page),
										DentalibericaSearchResponse.class);
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
		products.stream().forEach(prod->repository.save(adapter.toEntity(prod)));
//		products.stream().forEach(prod -> System.out.println("\n" + prod.toString()));

	}

	private String sendHttpRequest(String search, int page) throws IOException {
		final String newUrl = String.format("https://dentaliberica.com/pt/jolisearch?search_query=%s&page=%s&from-xhr",
				search, page);

		URL url = new URL(newUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
		conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		conn.setRequestProperty("Alt-Used", "dentaliberica.com");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://dentaliberica.com/pt/jolisearch?search_query=cera");
		conn.setRequestProperty("Cookie",
				"__lglaw=2; PHPSESSID=e7ppkg9244a4d88n1o4qmg82b1; PrestaShop-61cc070115705ff0f69e20744dbb3273=def50200abacd3c0bd7fce66973202286a47ba5af95748bbc074d55468abeeb084b1393681988992044e06246be2eeaabc1454745e99382b91005f76e2c40134e54912780b62e50199235aafe007f640043a2af7329ed67934a1a8c1d36041cebea00769c6331350ae213ee60869c717ca4b112f144cb41709ba489ca2f00a280f4df8806d158f5e02fc2201926013a724e23907b5c2b8b24d1447550e5a0797dd55c1d10444d658d6e2420018f92b82ea7fd428ad5d6c9a7ff775be2280a3122b100ce37e9a0c450238586f1e0a00345b399ab2854056c67270c97c38788bcf93f306a32c8b82f86f748b115091273a26f9dbc8486494119cc0b5ca935646283fdc92f1fbc8ca7a060591c165b75f258973cfe371ee91eecd9cca9492db; cf_clearance=pzSnTfjZxTVvonhiAR6NI5Ane1x7uWwHDQ.6iideZPg-1730157588-1.2.1.1-E8pBQhJ_3_fkHF30i7pwCUdID8dB7XYUmZoWCEwVSy0T0OLl4G0GaTCQ6cdkSML45y2w4vE9J9qLkq_R36IYSUEKhJk2JGAr30y4A55G7iLma8ZHzDAZt.c5nWLtxKLvH9ovrEMYWTDtbsoLCGtKRYg4FV6w58p4RRfbzZODcAU_.0V6mtO6UtDgl62tKqeiJtAmKprrsstg3s.aStvImK9K0qpbuIeRpiv18bvXtB74GsIM1kP0AcNwOMR5FmkvBlX5e0ktyQF57vj4Yx9mEHndNLi4pcAyx6DwRrnCqJh00JmRrHr295tlc8KTzLz0JgxzkJMLH9ZEX_tkHTbGYYi6Lztf1HibqU6gz9md65Y; _gcl_aw=GCL.1730157608.EAIaIQobChMIvKrilJiyiQMVqXJBAh07vi6nEAMYASAAEgJ94vD_BwE; _gcl_au=1.1.1438291727.1730157578; _ga_686BNZ9MR5=GS1.1.1730157578.1.1.1730159144.60.0.0; _ga=GA1.1.1802679914.1730157578; cp-popup-last-displayed=1730157584; cp-popup-9=1730157584");
		conn.setRequestProperty("Sec-Fetch-Dest", "empty");
		conn.setRequestProperty("Sec-Fetch-Mode", "cors");
		conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
		conn.setRequestProperty("Priority", "u=0");
		conn.setRequestProperty("TE", "trailers");

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
