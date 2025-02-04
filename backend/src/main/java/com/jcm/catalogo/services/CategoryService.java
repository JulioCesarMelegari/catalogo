package com.jcm.catalogo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true) //atentar ao import, transactional do spring
	public List<Category> findAll(){ //readOnly true- não trava o banco na requisicao
		return repository.findAll();
	}

}
