package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tradingtols.br.scraper.model.entity.adapters.DotamedEntityAdpater;
import com.tradingtols.br.scraper.model.entity.responses.DotamedSeacrhResponse;
import com.tradingtols.br.scraper.model.entity.responses.DotamedSeacrhResponse.Product;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;

@Service
public class DotamedCurlScraper  implements Scraper {
	
	@Autowired
	private DotamedEntityAdpater adapter;
	@Autowired
	private ProdutoRepository repository;

	private static final String URL = "https://www.montellano.pt/api/products?q=%s&p=%s&limit=100&"
										+"orderBy[_score]=desc&skip_filters=true&skip_featureds=true";
	
    
	@Override
	public void scrap(String search) {
		
		final String searchCleaned = search.trim().replace(" ", "+"); 
		final List<Product> list = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			DotamedSeacrhResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					DotamedSeacrhResponse.class);
			list.addAll(response.getProducts());

			// paginação
//			int nPages = response.getTotal() / 100;  // 100 -> max produtos por página
//			int mod = response.getTotal() / 100;
//			nPages = mod == 0 ? nPages : nPages + 1; 
//			System.out.println("nPages = " + nPages);
//			
//			IntStream.range(2, nPages).parallel().forEach(i -> {
//				try {
//					var res = mapper.readValue(sendHttpRequest(searchCleaned, i), MontellanoSearchResponse.class);
//					list.addAll(res.getProducts());
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//
//				}
//			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//debug
//		list.stream().parallel().forEach(prod->System.out.println("\n"+prod.toString()));
		list.stream().forEach(prod-> repository.save(adapter.toEntity(prod)));
	}


	
	private String sendHttpRequest(String search, int page) {

		final String newURL = String.format(URL, search, page);
		final String referer = String.format("https://www.dotamedsaojoao.com/?s=%s&post_type=product&type_aws=true", 
				search);
		
		try {
			URL url = new URL("https://www.dotamedsaojoao.com/?wc-ajax=aws_action");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");

	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
	        conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        conn.setRequestProperty("Origin", "https://www.dotamedsaojoao.com");
	        conn.setRequestProperty("Alt-Used", "www.dotamedsaojoao.com");
	        conn.setRequestProperty("Connection", "keep-alive");
	        conn.setRequestProperty("Referer",referer);
	        conn.setRequestProperty("Cookie", "sbjs_migrations=1418474375998%3D1; sbjs_current_add=fd%3D2024-10-27%2015%3A33%3A43%7C%7C%7Cep%3Dhttps%3A%2F%2Fwww.dotamedsaojoao.com%2F%7C%7C%7Crf%3Dhttps%3A%2F%2Fwww.google.com%2F; sbjs_first_add=fd%3D2024-10-27%2015%3A33%3A43%7C%7C%7Cep%3Dhttps%3A%2F%2Fwww.dotamedsaojoao.com%2F%7C%7C%7Crf%3Dhttps%3A%2F%2Fwww.google.com%2F; sbjs_current=typ%3Dorganic%7C%7C%7Csrc%3Dgoogle%7C%7C%7Cmdm%3Dorganic%7C%7C%7Ccmp%3D%28none%29%7C%7C%7Ccnt%3D%28none%29%7C%7C%7Ctrm%3D%28none%29%7C%7C%7Cid%3D%28none%29%7C%7C%7Cplt%3D%28none%29%7C%7C%7Cfmt%3D%28none%29%7C%7C%7Ctct%3D%28none%29; sbjs_first=typ%3Dorganic%7C%7C%7Csrc%3Dgoogle%7C%7C%7Cmdm%3Dorganic%7C%7C%7Ccmp%3D%28none%29%7C%7C%7Ccnt%3D%28none%29%7C%7C%7Ctrm%3D%28none%29%7C%7C%7Cid%3D%28none%29%7C%7C%7Cplt%3D%28none%29%7C%7C%7Cfmt%3D%28none%29%7C%7C%7Ctct%3D%28none%29; sbjs_udata=vst%3D1%7C%7C%7Cuip%3D%28none%29%7C%7C%7Cuag%3DMozilla%2F5.0%20%28Windows%20NT%2010.0%3B%20Win64%3B%20x64%3B%20rv%3A131.0%29%20Gecko%2F20100101%20Firefox%2F131.0; sbjs_session=pgs%3D3%7C%7C%7Ccpg%3Dhttps%3A%2F%2Fwww.dotamedsaojoao.com%2F%3Fs%3Dlimas%26post_type%3Dproduct%26type_aws%3Dtrue; cf_clearance=uw5wH1hh4aVbwGHYXBW_QEqarmCekmc_ebz._JDd588-1730043233-1.2.1.1-h30D.gSKPV3KHokhq9hbCyyh30ztKn0EB8rkc4rFvn3gqp4LMaiLR83.dbDohRKG7Bd_U0fiBYrvjWttwUkAqdZK4W0qQicDYAjf05hwCqQsCkIko_REJUP5CTQyHLKU8o0Pm3XjWsWdRfTt5JFCesAWgfkG3dGCV.3Yp77gUeMtO.4ecZlNRDdFf.N184jBr_8LEWjB3Xpt7xsMLcmCIci54WizOatT5yGo0NyJXSJL2HoSjfw8sGA0L49xlauOyQzY2RZBn6NJ77RZNY_AnfbCuuiDACgG8VNpmxtkg0AmgnGd_EJVQP.7uWgQE3gT_bHDZidg1JJUNQgPDZq4bIbhlMIbmpE7Xd0H9WL9N4k; store_notice3501ac2cf8c326aab106541d6765c11e=hidden");
	        conn.setRequestProperty("Sec-Fetch-Dest", "empty");
	        conn.setRequestProperty("Sec-Fetch-Mode", "cors");
	        conn.setRequestProperty("Sec-Fetch-Site", "same-origin");

	        conn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	        writer.write("action=aws_action&keyword=limA&aws_page=16395&aws_tax=&lang=&pageurl=https%3A%2F%2Fwww.dotamedsaojoao.com%2F%3Fs%3Dlimas%26post_type%3Dproduct%26type_aws%3Dtrue&typedata=json");
	        writer.flush();
	        writer.close();
	        conn.getOutputStream().close();

	        InputStream responseStream = conn.getResponseCode() / 100 == 2
	                ? conn.getInputStream()
	                : conn.getErrorStream();
	        if ("gzip".equals(conn.getContentEncoding())) {
	            responseStream = new GZIPInputStream(responseStream);
	        } else if ("deflate".equalsIgnoreCase(conn.getContentEncoding())) {
	        	responseStream = new InflaterInputStream(responseStream);
			}

			// Lê a resposta como UTF-8
			try (BufferedReader in = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();
				// Imprime a resposta decodificada
				System.out.println(response.toString());
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}


}
