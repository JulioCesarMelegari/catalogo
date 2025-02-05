package com.jcm.catalogo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcm.catalogo.dto.CategoryDTO;
import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.repositories.CategoryRepository;
import com.jcm.catalogo.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true) //atentar ao import, transactional do spring
	public List<CategoryDTO> findAll(){ //readOnly true- não trava o banco na requisicao
		List<Category> list = repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category category = obj.orElseThrow(()-> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(category);
	}

}
