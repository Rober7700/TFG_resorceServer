package com.numen.springboot.rest.utilities;

import java.io.IOException;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;

public interface IResponses {
	 
	public ResponseEntity<Map<String,Object>> DataAccess(DataAccessException e);
	
	public ResponseEntity<Map<String, Object>> handleIOException(IOException e);
	
	public ResponseEntity<Map<String,Object>> NotFound(Long id, String tipo);
	
	public ResponseEntity<Map<String,Object>> NotFound(String tipoPrenda, String tipo);
	
	public ResponseEntity<Map<String,Object>> found(Object obj, String tipo);
	
	public ResponseEntity<Map<String,Object>> created(Object obj, String tipo);
	
	public ResponseEntity<Map<String,Object>> updated(Object obj, String tipo);
	
	public ResponseEntity<Map<String,Object>> updatedFoto(Object obj, String tipo);

	public ResponseEntity<Map<String,Object>> badRequest(Object obj);
	
	public ResponseEntity<Map<String,Object>> deleted(String tipo);
	
}
