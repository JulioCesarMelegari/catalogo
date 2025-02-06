package com.jcm.catalogo.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jcm.catalogo.services.exceptions.ResouceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResouceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResouceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now()); //horario atual
		err.setStatus(HttpStatus.NOT_FOUND.value()); //.value() pega o numero do erro
		err.setError("Resouce not found"); //mensagem padrão quando estourar erro
		err.setMessage(e.getMessage()); //mensagem que vem do service
		err.setPath(request.getRequestURI()); //caminho da requisicao
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

}
