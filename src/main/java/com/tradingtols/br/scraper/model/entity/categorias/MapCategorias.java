package com.tradingtols.br.scraper.model.entity.categorias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapCategorias {
	
	
	private static Map<ProdutoCategoria, List<String>> map = new HashMap<>();
	private static List<String> lista = new ArrayList<>();
	
	public static Map<ProdutoCategoria, List<String>> getMap() {
			
		map.put(ProdutoCategoria.SEM_CATEGORIA, new ArrayList<String>());
		
		var arr = new String[] {
				"composito", "resina", "amalgama", "irm", "brunidor", "acido ortofosforico", "selante",	
				"matriz", "cunha", "fotopolimerizador", "papel articular", "papel quimico",
				"bonding", "dappen", "escala", "herculite", "fio de retraçao", "fio retraçao",
				"viscostat", "bond", "palete", "lentulo", "espigao", "poste", "pino rosqueavel",
				"etching", "cavit", "flow", "seal", "fissurit", "composite", "adesivo", "fiber",
				"acido gravador", "acid", "screw"
		};
		map.put(ProdutoCategoria.DENTISTICA, Arrays.asList(arr));

		arr = new String[] {
				"sutura", "sindesmotomo", "forceps", "alavanca", "gazes", "inserto",
				"agulha de sutura", "canula", "hemostatico", "cirurgica", "cirurgia",
				"periostomo", "bisturi eletronico", "kit cirugico", "pano de campo",
				"tesoura", "tijera", "colagen", "gelatamp", "luxador", "bata", "cirurgiao"
		};
		map.put(ProdutoCategoria.CIRURGIA, Arrays.asList(arr));
		
		arr = new String[] {
				"bracket", "mola", "arco", "nicr", "hirax", "expansor",
				"mantenedor de espaço", "resort", "elastico ortodontico", "implante ortodontico",
				"barra lingual", "contençao", "adesivo", "bond", "cirurgia", "acido gravador"
		};
		map.put(ProdutoCategoria.ORTODONTIA, Arrays.asList(arr));
		
		arr = new String[] {
				"branqueamento", "clareamento", "composito", "faceta", "piercing", 
				"coroa", "harmonizaçao", "escala", "fio retraçao", "palete", "adesivo", "fiber",
				"post ", "acido gravador", "acid", "screw"
		};
		map.put(ProdutoCategoria.ESTETICA, Arrays.asList(arr));
		
		arr = new String[] {
				"lima", "alargador", "cimento endodontico", "solvente", "edta", 
				"hipoclorito", "irrigação", "regua", "periapical", "intracanal",
				"mecanizada", "limas niti", "flexofile", "limas K", "Hedstroem",
				"limas S", "reciprocante", "motor de endodontia", "dique", "endo",
				"pontas de papel", "lentulo", "ponta de gutapercha", "gutta", "stops de silicone"
		};
		map.put(ProdutoCategoria.ENDODONTIA, Arrays.asList(arr));
		
		arr = new String[] {
				"cureta", "ultrassom", "bisturi periodontal", "irrigaçao", 
				"clorexidina", "oxigenada", "hemostatico", "bisturi eletronico",
				"destartarizador", "regua periodontal", "sonda", "extractor",
				"kit cirugico", "pano de campo", "colagen", "gelatamp"
		};
		map.put(ProdutoCategoria.PERIODONTIA, Arrays.asList(arr));
		
		arr = new String[] {
				"coroa", "acrilico", "ceramica", "fresadora", "faceta",
				"gesso", "vibrador", "termoplastificadora", "placa termica",
				"godiva", "papel articular", "papel quimico", "dente", "prensa",
				"dappen", "escala", "arranca coroas", "fio retraçao", "viscostat",
				"acido fluoridrico", "espigao", "poste", "pino rosqueavel", "fiber",
				"post", "acido gravador", "acid", "screw"
		};
		map.put(ProdutoCategoria.PROTESE, Arrays.asList(arr));
		
		arr = new String[] {
				"composito", "fotopolimerizador", "selante", "ácido", "bonding",
				"composito fluido" , "escala", "bond", "palete", "adesivo"
		};
		map.put(ProdutoCategoria.COMPOSITO, Arrays.asList(arr));
		
		arr = new String[] {
				"matriz", "cunha" 
		};
		map.put(ProdutoCategoria.MATRIZ_CUNHA, Arrays.asList(arr));
		
		arr = new String[] {
				"clareamento", "branqueamento", "carbamida", "agua oxigenada", "dique",
				"proteçao gengival", "laser", "palete"
		};
		map.put(ProdutoCategoria.BRANQUEAMENTO, Arrays.asList(arr));
		
		arr = new String[] {
				"lima", "hedstroem", "limas K", "limas H", "limas S", "flexofile",
				"limas niti", 
		};
		map.put(ProdutoCategoria.LIMAS, Arrays.asList(arr));
		
		arr = new String[] {
				"broca", "tungstenio", "carbide", "diamante"
		};
		map.put(ProdutoCategoria.BROCAS, Arrays.asList(arr));
		
		arr = new String[] {
				"radiografia", "ortopantomografia", "liquido de revelaçao",
				"liquido de fixaçao", "autorrevelante", "pelicula", "raio x"
		};
		map.put(ProdutoCategoria.RX, Arrays.asList(arr));
		
		arr = new String[] {
				"implantes", "membrana", "plaqueta", "centrifuga", "motor de implantes",
				"implantodontia", "trepano", "trifosfato", "difosfato", "silicato",
				"kit cirugico", "pano de campo", "colagen", "gelatamp", "implantologia"
		};
		map.put(ProdutoCategoria.IMPLANTES, Arrays.asList(arr));
		
		arr = new String[] {
				"corante", "ponta de destartarizador", "ponta de destararizaçao",
				"ultrassom", "destartarização", "escova", "polidor", "jato de bicarbonato",
				"oralwash"
		};
		map.put(ProdutoCategoria.PROFILAXIA, Arrays.asList(arr));
		
		arr = new String[] {
				"cimento", "espatula", "placa de vidro", "cavit", "meron"
		};
		map.put(ProdutoCategoria.CIMENTOS, Arrays.asList(arr));
		
		arr = new String[] {
				"silicon", "espatula de silicon", 
		};
		map.put(ProdutoCategoria.SILICONAS, Arrays.asList(arr));
		
		arr = new String[] {
				"algin", "espatula de alginato", 
		};
		map.put(ProdutoCategoria.ALGINATO, Arrays.asList(arr));
		
		arr = new String[] {
				"silicona", "alginato", "moldeira", "vibrador", "gesso",
				"molde", "espatula de gesso", "vibrador"
		};
		map.put(ProdutoCategoria.IMPRESSAO, Arrays.asList(arr));
		
		arr = new String[] {
				"3D", "CAD", "empress", " ips", "easyshade", "blocs", "enamic",
				"bloco", "discos lava", "lava", "vacumat"
		};
		map.put(ProdutoCategoria.IMPRESSAO3D, Arrays.asList(arr));
		
		arr = new String[] {
				"desinfecção", "toalhete", "alcool", "clorexidina", 
				"cloreto de oxigenio", "CL2O", "desinfetante", 
				"antisseptico", "hipoclorito", "indicador biologico",
				"higienizante", "escova para lavagem de maos"
		};
		map.put(ProdutoCategoria.DESINFECCAO, Arrays.asList(arr));
		
		arr = new String[] {
				"descartavel", "babete", "corrente de babete", "porta babete",
				"sugador", "ejetores", "protetores de calçado",
				"coberturas de proteçao", "luva", "mascara", "gorro",
				"guardanapos", "descartaveis"
				
		};
		map.put(ProdutoCategoria.DESCARTAVEL, Arrays.asList(arr));
		
		arr = new String[] {
				"instrumental", "instrumentais", "forceps", "alavancas",
				"sindesmotomo", "cabo bisturi", "bisturi", "periostomo",
				"espelho", "pinça", "explorador", "cureta", "colher dentina",
				"raspador", "afastador", "alargador", "alicate", "expansor",
				"luxador", "rolo de algodao", "chave de fendas", "faca"
		};
		map.put(ProdutoCategoria.INSTRUMENTAL, Arrays.asList(arr));
		
		arr = new String[] {
				"cadeira", "refletor", "armario", "bomba de vacuo", "laser",
				"destartarizador", "pisoeletronico", "bisturi eletronico",
				"pedal", "unidade auxiliar", "deposito de agua", "ips", 
				"compressor", "lampada", "lampara", "termometro", "oculos",
				"bandeja de aluminio"
		};
		map.put(ProdutoCategoria.EQUIPAMENTOS, Arrays.asList(arr));
		
		arr = new String[] {
				"turbina", "contra-angulo", "baixa rotaçao", "motor de implante",
		};
		map.put(ProdutoCategoria.TURBINA, Arrays.asList(arr));
		
		arr = new String[] {
				"uniforme", "calças", "pijama", "bata", "camisola", "sapato"
		};
		map.put(ProdutoCategoria.UNIFORME, Arrays.asList(arr));
		
		arr = new String[] {"varios"};
		map.put(ProdutoCategoria.VARIOS, Arrays.asList(arr));
	
		return  new HashMap<ProdutoCategoria, List<String>>(map);
	}
}
