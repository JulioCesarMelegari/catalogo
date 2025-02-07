package com.jcm.catalogo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcm.catalogo.dto.CategoryDTO;
import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.repositories.CategoryRepository;
import com.jcm.catalogo.services.exceptions.DatabaseException;
import com.jcm.catalogo.services.exceptions.ResouceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
		Category category = obj.orElseThrow(()-> new ResouceNotFoundException("Entity not found"));
		return new CategoryDTO(category);
	}
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		//para atualizar utilizar o metodo abaixo para não ter que acessar 
		//o banco duas vezes(pesquisar e atualizar)
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) { //EntityNotFound.. do Jakarta
			throw new ResouceNotFoundException("Id not found" + id);
		}		//ResouceNotFo.. classe que foi criada na nossa aplicacao
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResouceNotFoundException("Recurso não encontrado");
		}try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
		
	}

}
