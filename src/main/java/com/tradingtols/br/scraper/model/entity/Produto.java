package com.tradingtols.br.scraper.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
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

	@Override
	public String toString() {
		return "Produto [id=" + id + ", compania=" + compania + ", \ndescription=" + description + ", externalId="
				+ externalId + ", \nlink=" + link + ", \nprice=" + price + ", \nsrcImage=" + srcImage + ", \nbrand=" + brand
				+ ", data=" + data + "]";
	}
	
	
}
