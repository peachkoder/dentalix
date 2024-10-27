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

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tradingtols.br.scraper.model.entity.DentalixSearchResponse;
import com.tradingtols.br.scraper.model.entity.DentalixSearchResponse.ProductData;
import com.tradingtols.br.scraper.model.entity.DentalixSearchResponse.ProductList;
import com.tradingtols.br.scraper.model.entity.DentalixSearchResponse.ProductResponse;

@Service
public class DentaltixCurlScraper extends CurlScraper implements Scraper {

	private static final String DENTALTIX_URL = "https://www.dentaltix.com/";
	private static final String URL = "https://front.dentaltix.com/product/v1/search?"
			+ "keyword=%s&_page=%s&q=%s&appid=4UiYurrDTDOmVlOzoggdkg";

	@Override
	public void scrap(String search) {
		final String searchCleaned = search.trim().replace(" ", "+"); 

		List<DentalixSearchResponse> list = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
	//	mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		try {
			DentalixSearchResponse response = mapper.readValue(sendHttpRequest(searchCleaned, 1),
					DentalixSearchResponse.class);
			list.add(response);

			int nPages = response.getProductList().pages;
			System.out.println("nPages = " + nPages);
			IntStream.range(2, nPages).parallel().forEach(i -> {
				try {
					var res = mapper.readValue(sendHttpRequest(searchCleaned, i), DentalixSearchResponse.class);
					list.add(res);
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

		JsonArray jarr = new JsonArray();
		list.stream().map(res -> res.getProductList().idList).reduce((list1, list2) -> {
			list1.addAll(list2);
			return list1;
		}).get().stream().parallel().forEach(id -> jarr.add(id));

		String sendProductHttpRequest = sendProductHttpRequest(jarr.toString());
		try {
			var res = mapper.readValue(sendProductHttpRequest, ProductResponse.class);
			res.data.stream().forEach(pd-> System.out.println("\n" + pd.toString()));
		} catch (JsonMappingException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String sendRequest(String search, int page) {
		var newUrl = String.format(URL, search, page, search);

		var arr = new String[] { "curl", "-s", // Modo silencioso
				newUrl, "--compressed", "-H",
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0", "-H",
				"Accept: application/json, text/plain, */*", "-H", "Accept-Language: en-US,en;q=0.5", "-H",
				"Accept-Encoding: gzip, deflate, br, zstd", "-H",
				"x-context: eyJ4LXNob3AtdXJsIjoiaHR0cHM6Ly93d3cuZGVudGFsdGl4LmNvbS9wdCJ9", "-H",
				"Origin: https://www.dentaltix.com", "-H", "Connection: keep-alive", "-H",
				"Referer: https://www.dentaltix.com/",
//                "-H", "Cookie: _gcl_aw=GCL.1729846705.EAIaIQobChMIkejKwpSpiQMVmD8GAB2Wujg2EAAYASAAEgIgHPD_BwE; _gcl_gs=2.1.k1\\$i1729846527\\$u243162391; _ga_CLFED4KXQM=GS1.1.1729968872.6.1.1729968917.15.0.0; CURRf17c5478dc1a1ddd0ee3b589d5dfbe6b=EUR; _gcl_au=1.1.1817064578.1729846897; _conv_v=vi^%^3A1*sc^%^3A6*cs^%^3A1729968895*fs^%^3A1729846897*pv^%^3A21*ps^%^3A1729953377; _ga=GA1.1.905901694.1729846897; _fbp=fb.1.1729846900149.242185328494181847; pc_v_T5SYSRtvSxiVYyMuMTf3KA=XacBBf0mTUyeEMqesBUmtg; pc_sessid_T5SYSRtvSxiVYyMuMTf3KA=bT0N69X3RVSoOfktTv4SGg; CURR0ab3b3961df40fe80e607ed99f283d0e=EUR; _conv_s=si^%^3A6*sh^%^3A1729968894517-0.8653515579520213*pv^%^3A5; mp_0c2010913c10307ca02fe928c80e0cb8_mixpanel=^%^7B^%^22distinct_id^%^22^%^3A^%^20^%^22^%^24device^%^3A192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954^%^22^%^2C^%^22^%^24device_id^%^22^%^3A^%^20^%^22192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954^%^22^%^2C^%^22^%^24search_engine^%^22^%^3A^%^20^%^22google^%^22^%^2C^%^22^%^24initial_referrer^%^22^%^3A^%^20^%^22https^%^3A^%^2F^%^2Fwww.google.com^%^2F^%^22^%^2C^%^22^%^24initial_referring_domain^%^22^%^3A^%^20^%^22www.google.com^%^22^%^2C^%^22__mps^%^22^%^3A^%^20^%^7B^%^7D^%^2C^%^22__mpso^%^22^%^3A^%^20^%^7B^%^22^%^24initial_referrer^%^22^%^3A^%^20^%^22https^%^3A^%^2F^%^2Fwww.google.com^%^2F^%^22^%^2C^%^22^%^24initial_referring_domain^%^22^%^3A^%^20^%^22www.google.com^%^22^%^7D^%^2C^%^22__mpus^%^22^%^3A^%^20^%^7B^%^7D^%^2C^%^22__mpa^%^22^%^3A^%^20^%^7B^%^7D^%^2C^%^22__mpu^%^22^%^3A^%^20^%^7B^%^7D^%^2C^%^22__mpr^%^22^%^3A^%^20^%^5B^%^5D^%^2C^%^22__mpap^%^22^%^3A^%^20^%^5B^%^5D^%^7D",
//                "-H", "Cookie: _gcl_aw=GCL.1729846705.EAIaIQobChMIkejKwpSpiQMVmD8GAB2Wujg2EAAYASAAEgIgHPD_BwE; _gcl_gs=2.1.k1$i1729846527$u243162391; _ga_CLFED4KXQM=GS1.1.1729846896.1.1.1729848217.29.0.0; CURRf17c5478dc1a1ddd0ee3b589d5dfbe6b=EUR; pc_v_4UiYurrDTDOmVlOzoggdkg=wCEDOxdaRsGAi_GzsHaQJA; _gcl_au=1.1.1817064578.1729846897; _conv_v=vi%3A1*sc%3A1*cs%3A1729846897*fs%3A1729846897*pv%3A4; _conv_s=si%3A1*sh%3A1729846896802-0.40999522234913655*pv%3A4; _ga=GA1.1.905901694.1729846897; _fbp=fb.1.1729846900149.242185328494181847; pc_sessid_4UiYurrDTDOmVlOzoggdkg=UUiEPGpsQF-wBzH1n0-ryQ; mp_0c2010913c10307ca02fe928c80e0cb8_mixpanel=%7B%22distinct_id%22%3A%20%22%24device%3A192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24device_id%22%3A%20%22192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24search_engine%22%3A%20%22google%22%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%2C%22__mps%22%3A%20%7B%7D%2C%22__mpso%22%3A%20%7B%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%7D%2C%22__mpus%22%3A%20%7B%7D%2C%22__mpa%22%3A%20%7B%7D%2C%22__mpu%22%3A%20%7B%7D%2C%22__mpr%22%3A%20%5B%5D%2C%22__mpap%22%3A%20%5B%5D%7D",
				"-H", "Sec-Fetch-Dest: empty", "-H", "Sec-Fetch-Mode: cors", "-H", "Sec-Fetch-Site: same-site", "-H",
				"If-None-Match:", "-H", "If-None-Match: W/\"e83d-xixC7AG/AQTV4ut42QqkcOhmpbg\"", "-H", "TE: trailers" };

		return sendRequest(Arrays.asList(arr));
	}

	private String sendProductRequest(String jsonArr) {
		JsonObject json = new JsonObject();
		// String requestBody = "{\"idList\": [\"384815\"], \"idType\": \"nid\"}";
		json.addProperty("idList", jsonArr);
		json.addProperty("idType", "nid");
		String requestBody = json.toString();

		var arr = new String[] { "curl", "-s", "-X", "POST", "-v",
				"https://front.dentaltix.com/product/v1/product/product-summary/get",
				"-H User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0",
				"-H Host: front.dentaltix.com", "-H Accept: application/json, text/plain, */*",
				"-H Accept-Language: en-US,en;q=0.5", "-H Accept-Encoding: gzip, deflate, br, zstd",
				"-H Content-Type: application/json; utf-8",
				"-H x-context: eyJ4LXNob3AtdXJsIjoiaHR0cHM6Ly93d3cuZGVudGFsdGl4LmNvbS9wdCJ9",
				"-H Origin: https://www.dentaltix.com", "-H Connection: keep-alive",
				"-H Referer: https://www.dentaltix.com/",
//                "-H Cookie: _gcl_aw=GCL.1729846705.EAIaIQobChMIkejKwpSpiQMVmD8GAB2Wujg2EAAYASAAEgIgHPD_BwE; _gcl_gs=2.1.k1$i1729846527$u243162391; _ga_CLFED4KXQM=GS1.1.1729902950.3.0.1729902950.60.0.0; CURRf17c5478dc1a1ddd0ee3b589d5dfbe6b=EUR; pc_v_4UiYurrDTDOmVlOzoggdkg=bL_UDwRdQ1mrubEN0TqWhA; _gcl_au=1.1.1817064578.1729846897; _conv_v=vi%3A1*sc%3A3*cs%3A1729902950*fs%3A1729846897*pv%3A13*ps%3A1729852980; _ga=GA1.1.905901694.1729846897; _fbp=fb.1.1729846900149.242185328494181847; mp_0c2010913c10307ca02fe928c80e0cb8_mixpanel=%7B%22distinct_id%22%3A%20%22%24device%3A192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24device_id%22%3A%20%22192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24search_engine%22%3A%20%22google%22%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%2C%22__mps%22%3A%20%7B%7D%2C%22__mpso%22%3A%20%7B%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%7D%2C%22__mpus%22%3A%20%7B%7D%2C%22__mpa%22%3A%20%7B%7D%2C%22__mpu%22%3A%20%7B%7D%2C%22__mpr%22%3A%20%5B%5D%2C%22__mpap%22%3A%20%5B%5D%7D; _conv_s=si%3A3*sh%3A1729902950496-0.14536992724488118*pv%3A1; pc_sessid_4UiYurrDTDOmVlOzoggdkg=cqhs_zjSReOJ-OWdrpevGA",
				"-H Sec-Fetch-Dest: empty", "-H Sec-Fetch-Mode: cors", "-H Sec-Fetch-Site: same-site",
				"-H Priority: u-4", "-H Pragma: no-cache", "-H Cache-Control: no-cache", "-H TE: trailers", "--data",
				requestBody// .replace("\\", "^")
		};

		System.out.println(requestBody);
		String sendRequest = sendRequest(Arrays.asList(arr));
		System.out.println(sendRequest);
		return sendRequest;
	}

	private String sendHttpRequest(String s, int i) {
		try {
			String url = "https://front.dentaltix.com/product/v1/search?keyword=%s&_page=%s&q=%s&appid=T5SYSRtvSxiVYyMuMTf3KA";
			url = String.format(url, s, i, s);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Configuração do método e headers
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
			con.setRequestProperty("Accept", "application/json, text/plain, */*");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
			con.setRequestProperty("x-context", "{\"x-shop-url\":\"https://www.dentaltix.com/en\"}");
			con.setRequestProperty("Origin", "https://www.dentaltix.com");
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Referer", "https://www.dentaltix.com/");
			con.setRequestProperty("Cookie",
					"_gcl_aw=GCL.1729846705.EAIaIQobChMIkejKwpSpiQMVmD8GAB2Wujg2EAAYASAAEgIgHPD_BwE; _gcl_gs=2.1.k1$i1729846527$u243162391; _ga_CLFED4KXQM=GS1.1.1729968872.6.1.1729968917.15.0.0; CURRf17c5478dc1a1ddd0ee3b589d5dfbe6b=EUR; _gcl_au=1.1.1817064578.1729846897; _conv_v=vi%3A1*sc%3A6*cs%3A1729968895*fs%3A1729846897*pv%3A21*ps%3A1729953377; _ga=GA1.1.905901694.1729846897; _fbp=fb.1.1729846900149.242185328494181847; pc_v_T5SYSRtvSxiVYyMuMTf3KA=XacBBf0mTUyeEMqesBUmtg; pc_sessid_T5SYSRtvSxiVYyMuMTf3KA=bT0N69X3RVSoOfktTv4SGg; CURR0ab3b3961df40fe80e607ed99f283d0e=EUR; _conv_s=si%3A6*sh%3A1729968894517-0.8653515579520213*pv%3A5; mp_0c2010913c10307ca02fe928c80e0cb8_mixpanel=%7B%22distinct_id%22%3A%20%22%24device%3A192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24device_id%22%3A%20%22192948b7acf954-019e7ef9426796-f545721-144000-192948b7acf954%22%2C%22%24search_engine%22%3A%20%22google%22%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%7D");
			con.setRequestProperty("Sec-Fetch-Dest", "empty");
			con.setRequestProperty("Sec-Fetch-Mode", "cors");
			con.setRequestProperty("Sec-Fetch-Site", "same-site");
			con.setRequestProperty("If-None-Match", "W/\"b2a7-F9dKRQtSi5DTiRp1L0JH7L+h86U\"");
			con.setRequestProperty("TE", "trailers");

			// Processa a resposta com tratamento de encoding gzip/deflate/br
			InputStream inputStream = con.getInputStream();
			String encoding = con.getContentEncoding();
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

	private String sendProductHttpRequest(String data) {
		data = String.format("{\"idList\": %s,\"idType\":\"nid\"}", data);
		System.out.println(data);

		try {
			String url = "https://front.dentaltix.com/product/v1/product/product-summary/get";
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			// configuração do método POST
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0");
			conn.setRequestProperty("Accept", "application/json, text/plain, */*");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-context", "{\"x-shop-url\":\"https://www.dentaltix.com/en\"}");
			conn.setRequestProperty("Origin", "https://www.dentaltix.com");
			conn.setRequestProperty("Referer", "https://www.dentaltix.com/");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Sec-Fetch-Dest", "empty");
			conn.setRequestProperty("Sec-Fetch-Mode", "cors");
			conn.setRequestProperty("Sec-Fetch-Site", "same-site");
			conn.setDoOutput(true);
			// define o corpo da requisição
			try (DataOutputStream dOut = new DataOutputStream(conn.getOutputStream())) {
				byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
				dOut.write(bytes);
			}
			// verifica a codificação da resposta
			InputStream input = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
			String encoding = conn.getContentEncoding();
			if ("gzip".equalsIgnoreCase(encoding)) {
				input = new GZIPInputStream(input);
			} else if ("deflate".equalsIgnoreCase(encoding)) {
				input = new InflaterInputStream(input);
			}

			try (BufferedReader in = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
//				System.out.println("\n" + response.toString());
				return response.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
