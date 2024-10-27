package com.tradingtols.br.scraper.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradingtols.br.scraper.model.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer>{

	Optional<Marca> findByBrand(String s);

}
