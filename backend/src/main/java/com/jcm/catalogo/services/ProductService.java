package com.jcm.catalogo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcm.catalogo.dto.CategoryDTO;
import com.jcm.catalogo.dto.ProductDTO;
import com.jcm.catalogo.entities.Category;
import com.jcm.catalogo.entities.Product;
import com.jcm.catalogo.repositories.CategoryRepository;
import com.jcm.catalogo.repositories.ProductRepository;
import com.jcm.catalogo.services.exceptions.DatabaseException;
import com.jcm.catalogo.services.exceptions.ResouceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true) //atentar ao import, transactional do spring
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){ //readOnly true- não trava o banco na requisicao
		Page<Product> list = repository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(()-> new ResouceNotFoundException("Entity not found"));
		return new ProductDTO(entity,entity.getCategories());
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		//para atualizar utilizar o metodo abaixo para não ter que acessar 
		//o banco duas vezes(pesquisar e atualizar)
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
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
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto: dto.getCategories()) {
			Category category = categoryRepository.findById(catDto.getId()).get();
			entity.getCategories().add(category);
		}
	}

}
