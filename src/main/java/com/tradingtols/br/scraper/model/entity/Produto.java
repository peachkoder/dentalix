package com.tradingtols.br.scraper.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;
import com.tradingtols.br.scraper.model.tools.StringToProdutoCategoriaFlagConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String compania;
	@Column(columnDefinition = "TEXT")
	private String description;
	@Column(name = "external_id")
	private String externalId;
	@Column(columnDefinition = "TEXT")
	private String link;
	private String price;
	@Column(name = "src_image", columnDefinition = "TEXT")
	private String srcImage;
	private String brand;
	private Date data;
	@Convert(converter = StringToProdutoCategoriaFlagConverter.class)
	private List<ProdutoCategoria> categorias;// = new ArrayList<>();
	
	
	public Produto() {}

	public Produto(Long id, String compania, String description, String externalId, String link, String price,
			String srcImage, String brand, Date data) {
		super();
		this.id = id;
		this.compania = compania;
		this.description = description;
		this.externalId = externalId;
		this.link = link;
		this.price = price;
		this.srcImage = srcImage;
		this.brand = brand;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public String getCompania() {
		return compania;
	}

	public String getDescription() {
		return description;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getLink() {
		return link;
	}

	public String getPrice() {
		return price;
	}

	public String getSrcImage() {
		return srcImage;
	}

	public String getBrand() {
		return brand;
	}

	public Date getData() {
		return data;
	}
	
	public List<ProdutoCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<ProdutoCategoria> categorias) {
		if (categorias == null) return;
		this.categorias = categorias;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", compania=" + compania + ", \ndescription=" + description + ", externalId="
				+ externalId + ", \nlink=" + link + ", \nprice=" + price + ", \nsrcImage=" + srcImage + ", \nbrand=" + brand
				+ ", data=" + data + "]";
	}
	
	
}
