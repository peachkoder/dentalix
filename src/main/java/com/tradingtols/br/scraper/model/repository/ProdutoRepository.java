package com.tradingtols.br.scraper.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradingtols.br.scraper.model.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
