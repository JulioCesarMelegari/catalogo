package com.jcm.catalogo.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResouce {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		List<Category> list = categoryService.findAll();
		//list.add(new Category(1L, "Books"));
		//list.add(new Category(2L, "Electronics"));
		return ResponseEntity.ok().body(list);
	}
}
