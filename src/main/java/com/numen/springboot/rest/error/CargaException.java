package com.numen.springboot.rest.error;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.numen.springboot.rest.utilities.IResponses;

@Component
public class CargaException implements IResponses {

	private Map<String, Object> responses;

	@Override
	public ResponseEntity<Map<String, Object>> DataAccess(DataAccessException e) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "Error de conexion de la base de datos");
		responses.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> handleIOException(IOException e) {
        responses = new HashMap<String, Object>();
        responses.put("mensaje", "Error de al subir la imagen");
        responses.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@Override
	public ResponseEntity<Map<String, Object>> NotFound(Long id, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + "  ID: ".concat(id.toString().concat(" No existe en la base de datos")));
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.NOT_FOUND);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> NotFound(String tipoPrenda, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "Prendas con tipo " + tipoPrenda + ": ".concat(" No existe en la base de datos"));
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.NOT_FOUND);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> badRequest(Object obj) {
		responses = new HashMap<String, Object>();
		responses.put("errors", obj);
		System.out.println(obj);
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<Map<String, Object>> found(Object obj, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + " ha sido encontrado con exito");
		responses.put("data", obj);
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Map<String, Object>> created(Object obj, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + "  ha sido creado con exito");
		responses.put("data", obj);
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Map<String, Object>> updated(Object obj, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + "  ha sido modificado con exito");
		responses.put("data", obj);
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> updatedFoto(Object obj, String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + "  ha subido correctamente la imagen");
		responses.put("data", obj);
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Map<String, Object>> deleted(String tipo) {
		responses = new HashMap<String, Object>();
		responses.put("mensaje", "El objeto " + tipo + "  eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(this.responses, HttpStatus.OK);
	}
}