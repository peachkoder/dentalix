package com.tradingtols.br.scraper.model.entity.categorias;

public enum ProdutoCategoria {
	DENTISTICA("dentística"), 
	CIRURGIA("cirurgia"), 
	ORTODONTIA("ortodontia"), 
	ESTETICA("estética"),
	ENDODONTIA("endodontia"),
	PERIODONTIA("periodontia"),
	PROTESE("prótese"),
	COMPOSITO("compósitos"),
	MATRIZ_CUNHA("matrizes e cunhas"),
	BRANQUEAMENTO("branqueamento"),
	LIMAS("limas"),
	BROCAS("brocas"),
	RX("RX"),
	IMPLANTES("implantes"),
	PROFILAXIA("profilaxia"),
	CIMENTOS("cimentos"),
	SILICONAS("silicona"),
	ALGINATO("alginato"),
	GESSO("gesso"),
	IMPRESSAO("impressão"),
	IMPRESSAO3D("impressão 3d"),
	DESINFECCAO("desinfecção"), 
	DESCARTAVEL("descartáveis"),
	INSTRUMENTAL("instrumental"), 
	EQUIPAMENTOS("equipamentos"),
	TURBINA("turbinas"),
	UNIFORME("uniformes"),
	VARIOS("vários"),
	SEM_CATEGORIA(null);

	private String name;

	public String getName() {
		return name;
	}

	ProdutoCategoria(String string) {
		this.name = string;
	}
}
