package com.tradingtols.br.scraper.model.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Classe usada para converter string em atributo para coluna categorias da
 * classe produto. O campo categorias ficara na forma: "a;b;c;d" na DB, ou seja
 * os atributos são separados por ponto e vírgula, pois é um campo que poderá
 * conter vários valores.
 */
	public class StringToProdutoCategoriaFlagConverter implements AttributeConverter<List<ProdutoCategoria>, String> {

	@Override
	public String convertToDatabaseColumn(List<ProdutoCategoria> attribute) {
		if (attribute == null || attribute.size() == 0)
			return null;
		return attribute.stream().map(ProdutoCategoria::name).collect(Collectors.joining(";"));
	}

	@Override
	public List<ProdutoCategoria> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return new ArrayList<>(); // Retorna uma lista vazia se não houver dados
		}
		// Separa a String por ";" e converte para uma lista de ProdutoCategoria
		return Arrays.stream(dbData.split(";")).map(s -> {
			try {
				return ProdutoCategoria.valueOf(s); // Converte a string em enum
			} catch (IllegalArgumentException e) {
				// Em caso de erro, pode retornar um valor padrão ou lançar uma exceção
				return ProdutoCategoria.VARIOS; // Substitui por um valor padrão se não encontrar
			}
		}).collect(Collectors.toList());
	}
}
