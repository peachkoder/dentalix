package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingtols.br.scraper.model.entity.DentaLeaderSearchResponse;

@Service
public class DentaleaderCurlScraper extends CurlScraper implements Scraper {

	private static final String DENTALEADER_URL = "https://www.dentaleader.com";
	private static final String URL = "https://eu1-search.doofinder.com/5/search?hashid=7d8a9d38400a9973aff61111fcf370d0&" + 
	"query_counter=23&page=%s&rpp=30&transformer=basic&session_id=e0daed06423690c9b14fa74453a3398d&query=%s";

	@Override
	public void scrap(String search) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			DentaLeaderSearchResponse response = mapper.readValue(sendRequest(search, 1),
					DentaLeaderSearchResponse.class);
//			System.out.println(response);
			
			var mod = response.getTotal() % response.getResults_per_page();
			System.out.println(mod);
			var nPages = response.getTotal() / response.getResults_per_page();
			System.out.println(nPages);
			var i = mod==0 ? nPages : nPages + 1;
			System.out.println(i);
			
			for (int j = 2; j <= i; j++) {
				response = mapper.readValue(sendRequest(search, j),DentaLeaderSearchResponse.class);
				System.out.println("-------------------------------");
				System.out.println(response);
				System.out.println("numero de páginas = " + j + "\n");
			} 
			
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	protected String sendRequest(String search, int page) {
		String newURL = String.format(URL, page, search);
		var arr = new String[] {
				"curl",
                "-s",
                newURL,
                "--compressed",
                "-H", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0",
                "-H", "Accept: */*",
                "-H", "Accept-Language: en-US,en;q=0.5",
                "-H", "Accept-Encoding: gzip, deflate, br, zstd",
                "-H", "Referer: " + DENTALEADER_URL,
                "-H", "Origin: " + DENTALEADER_URL,
                "-H", "Connection: keep-alive",
                "-H", "Sec-Fetch-Dest: empty",
                "-H", "Sec-Fetch-Mode: cors",
                "-H", "Sec-Fetch-Site: cross-site",
                "-H", "Priority: u=4",
                "-H", "TE: trailers"
		};
		
		return sendRequest(Arrays.asList(arr));
	}
}
