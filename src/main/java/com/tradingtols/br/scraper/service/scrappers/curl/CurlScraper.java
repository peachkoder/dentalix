package com.tradingtols.br.scraper.service.scrappers.curl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public abstract class CurlScraper {

	protected String sendRequest(List<String> command) {
//			String newURL = String.format(searchUrl, page, search);
			try {
	            // Criação do ProcessBuilder para executar o comando cURL
				ProcessBuilder processBuilder = new ProcessBuilder(command);
//	            ProcessBuilder processBuilder = new ProcessBuilder(
//	                "curl",
//	                "-s",
//	                newURL,
//	                //"https://eu1-search.doofinder.com/5/search?hashid=7d8a9d38400a9973aff61111fcf370d0&query_counter=23&page=1&rpp=30&transformer=basic&session_id=e0daed06423690c9b14fa74453a3398d&query=limas",
//	                "--compressed",
//	                "-H", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0",
//	                "-H", "Accept: */*",
//	                "-H", "Accept-Language: en-US,en;q=0.5",
//	                "-H", "Accept-Encoding: gzip, deflate, br, zstd",
//	                "-H", "Referer: " + originUrl,
//	                "-H", "Origin: " + originUrl,
//	                "-H", "Connection: keep-alive",
//	                "-H", "Sec-Fetch-Dest: empty",
//	                "-H", "Sec-Fetch-Mode: cors",
//	                "-H", "Sec-Fetch-Site: cross-site",
//	                "-H", "Priority: u=4",
//	                "-H", "TE: trailers"
//	            );
	
	            // Redirecionar a saída de erro para a saída padrão
	            processBuilder.redirectErrorStream(true);
	
	            // Iniciar o processo
	            Process process = processBuilder.start();
	
	            // Ler a saída do comando
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            StringBuilder output = new StringBuilder();
	            String line;
	
	            // Ler cada linha da saída
	            while ((line = reader.readLine()) != null) {
	                output.append(line).append("\n");
	            }
	
	            // Esperar pelo término do processo e capturar o código de saída
	            int exitCode = process.waitFor();
	
	            // Exibir a saída e o código de saída
//	            System.out.println("Exit Code: " + exitCode);
	//            System.out.println("Output:\n" + output.toString());
	            
	            String result = output.toString();
	//            int idx = result.indexOf("{");
	//            result = output.toString().substring(idx);
	//            System.out.println(result);
	            return result;
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return "";
		}

}