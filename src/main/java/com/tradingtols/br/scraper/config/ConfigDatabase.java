package com.tradingtols.br.scraper.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.tradingtols.br.scraper.model.entity.BuscaPadrao;
import com.tradingtols.br.scraper.model.entity.Marca;
import com.tradingtols.br.scraper.model.repository.BuscaPadraoRepository;
import com.tradingtols.br.scraper.model.repository.MarcaRepository;

@Component
public class ConfigDatabase {

	private BuscaPadraoRepository repo;
	
	private MarcaRepository marcaRepository;

	String[] buscas = new String[] { "ortodontia", "dentistica", "cirurgia", "profilaxia", "fluor", "enddontia", "cam",
			"equipamento", "instrumental", "uniforme", "roupa", "lima", "cone", "cones de gutapecha", "cones de papel",
			"cimento", "silicone", "ficha", "alginato", "moldagem", "moldeira", "composito", "radigrafia", "rx",
			"revelador", "película radiografica", "alargador", "apical", "elastico", "roth", "bisturi", "implante",
			"membrana", "enxerto", "osseo", "resina", "broca", "articulação", "amalagama", "turbina", "contra-angulo",
			"descartave", "impressao", "aparelho", "pino", "espigão", "espigões", "desinfecçao", "profila", "polidores",
			"uniforme", "luva", "seringa", "mascara", "babete", "rolos de algodao", "gazes", "bandeja", "aplicador",
			"pince", "oculos", "boquilhas", "proteção de equipamentos", "acido", "adesivo", "bonding", "selante",
			"matriz", "poste", "ionomero", "reconstruçao de cotos", "obturaçao", "ceramica", "cunha", "bata", "sapato",
			"tunica", "gorro", "calças", "irrigaçao", "dique", "grampo", "regua", "caixa de endodontia", "arco",
			"quelante", "protetor de apoio de cabeça", "bobina", "copo", "aspirador", "rolo de algodao", "gelo",
			"spray", "bracket", "arco", "tubo", "aditamento", "mola", "ligadura", "NITI", "extraora", "stripping",
			"instrumentos para ortodontia", "banda", "hemostatico", "sutura", "brocas cirurgicas", "campos cirurgicos",
			"irrigaçao", "biocompative", "piezoeletrico", "ultrassom", "lupa", "primeiros socorros", "canula",
			"nagatoscopio", "fosforo", "porta-radiografia", "avental de proteçao", "liquido de revelaçao",
			"liquido fixador", "lubrificaçao", "toalhetes", "termosseladora", "indicadores quimicos",
			"indicadores biologicos", "incubadoras", "bolsa de esterilizaçao", "manga", "tina", "contentor",
			"anestesia", "espelho", "pinça", "alavanca", "forceps", "elevador", "escavador", "brunidor", "espatula",
			"condensador de amalgama", "cureta", "bisturi", "tesoura", "separador", "arranca-coroas", "abre-boca",
			"sindesmotomo", "periostomo", "protese", "dessensibilizante", "branqueamento", "barreira gengival",
			"detetor de carie", "detetor de placa", "corante", "autclave", "fotopolimerizador", "aeropolidor",
			"inserto", "lamapada", "laser", "motores de implantes", "motores de endodontia", "localizador apical",
			"cadeira", "mobiliario", "microscopio", "compressor", "aspiraçao", "camara intra-oral", "sensor",
			"ortopantomografo", "sedaçao", "Raios-X", "termoformavel", "dente", "cera", "maquiagem", "placas base",
			"fita metalica", "papel articular", "porta-matriz", "vacuo", "silicato", "irm", "tricalcico",
			"dicalcico", };

	public ConfigDatabase(BuscaPadraoRepository repo, MarcaRepository marcaRepository) {
		this.repo = repo;
		this.marcaRepository = marcaRepository;
		
		if (repo.count() < buscas.length) {
			List<String> lista = Arrays.asList(buscas);
			Set<String> set = new HashSet<>(lista);
			List<BuscaPadrao> buscasPadrao = set.stream().map(s -> new BuscaPadrao(null, s)).toList();
			repo.saveAll(buscasPadrao);
		}
		//addMarcas();
	}
	
	public void addMarcas() {
		List<String> list = new ArrayList<>();
		char[] c = new char[255];
		FileReader f = null;
		
		try {
			f = new FileReader("/scraper/src/main/java/com/tradingtols/br/scraper/config/marcas.csv");
			if (f.read(c)!=-1) {
				list.add(String.valueOf(c));
			}
			System.err.println("marcas");
			list.stream().forEach(s -> {
					if (marcaRepository.findByBrand(s).isEmpty())
						marcaRepository.save(new Marca(0, s));
				});			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
