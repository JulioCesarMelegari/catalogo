package com.jcm.catalogo.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jcm.catalogo.entities.Product;
import com.jcm.catalogo.repositories.ProductRepository;
import com.jcm.catalogo.services.exceptions.DatabaseException;
import com.jcm.catalogo.services.exceptions.ResouceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	//teste de unidade, sem carregar outros componentes
	//teremos que mokar, visto que o teste de unidade
	//tende a ser mais rápido
	
	@InjectMocks
	private ProductService service;
	
	//quando cria um mock, precisamos configurar um comportamento simulado
	//temos que configurar o repository quando chamamos o metodo delete para 
	//um id que não existe
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	
	@BeforeEach
	void setUp(){
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 3L;
		page = new PageImpl<>(List.of(product));
		
		
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);	
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWehnIdDoesNotExist() {
		Assertions.assertThrows(ResouceNotFoundException.class, () ->{
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		//verifica se o metodo delete foi chamado no teste
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () ->{
			service.delete(dependentId);
		});
	}
	
}
