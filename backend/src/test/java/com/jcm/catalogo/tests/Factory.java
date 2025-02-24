package com.jcm.catalogo.tests;

import java.time.Instant;

import com.jcm.catalogo.dto.ProductDTO;
import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		
		Product product = new Product(1L, "Phone", "Galax A15", 950.00, "https://img.com/img.pgn", Instant.parse("2025-02-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;	
	}
	
	public static ProductDTO createProductDTO() {		
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
