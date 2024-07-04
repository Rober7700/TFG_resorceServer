package com.numen.springboot.rest.model.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numen.springboot.rest.model.entity.Cliente;
import com.numen.springboot.rest.model.service.IClienteService;
import com.numen.springboot.rest.utilities.IResponses;

import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteController {

	private static final String TIPO_CLIENTE = "cliente";

	private final Logger LOG = LoggerFactory.getLogger(ClienteController.class);

	@Autowired
	private IResponses control;

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<Cliente> index() {

		LOG.info("Metodo indexClientes:");
		return clienteService.findAll();
	}

	@GetMapping("/clientes/page/{page}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Page<Cliente> index(@PathVariable Integer page) {

		LOG.info("Metodo indexClientes:");
		LOG.info("Pagina nº{}", page);
		return clienteService.findAll(PageRequest.of(page, 5));
	}

	@GetMapping("/clientesNo/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getCliente(@PathVariable Long id) {

		LOG.info("Metodo getCliente:");

		Cliente cliente = null;

		try {
			cliente = clienteService.findById(id);
			if (cliente == null) {

				LOG.error("Cliente {} no encontrado", id);
				return control.NotFound(id, TIPO_CLIENTE);
			}
		} catch (DataAccessException e) {

			LOG.error("Error al buscar cliente con id {}", id);
			return control.DataAccess(e);
		}

		LOG.info("Cliente encontrado con id " + id);
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@GetMapping("/clientes/{email}")
	public ResponseEntity<?> getClienteEmail(@PathVariable String email) {

		LOG.info("Metodo getCliente");

		Cliente cliente = null;

		try {
			
			cliente = clienteService.findByEmail(email);
			if (cliente == null) {
				LOG.info("Cliente {} no encontrado", email);
				cliente = clienteService.iniciarClienteCarrito(email);
				return new ResponseEntity<Cliente>(cliente, HttpStatus.CREATED);
			}
		} catch (DataAccessException e) {

			LOG.error("Error al buscar cliente con email {}", email);
			return control.DataAccess(e);
		}

		LOG.info("Cliente encontrado con email {}", email);
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	

	@PostMapping("/clientes")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

		LOG.info("Metodo createCliente:");

		Cliente clienteNew = null;

		if (result.hasErrors()) {

			LOG.error("Error en el resultado");
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			return control.badRequest(errors);
		}

		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {

			LOG.error("Error al intentar guardar cliente");
			return control.DataAccess(e);
		}

		LOG.info("Cliente creado con id {}", cliente.getId());
		return control.created(clienteNew, TIPO_CLIENTE);
	}

	@PutMapping("/clientes/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

		LOG.info("Metodo updateCliente:");

		LOG.info("Buscando cliente con id {}", id);
		Cliente clienteActual = clienteService.findById(id);

		Cliente clienteUpdated = null;

		if (result.hasErrors()) {

			LOG.error("Error en el resultado");
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			return control.badRequest(errors);
		}

		if (clienteActual == null) {

			LOG.info("Error al encontrar cliente con id {}", id);
			return control.NotFound(id, TIPO_CLIENTE);
		}

		try {
			/*
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());

			clienteUpdated = clienteService.save(clienteActual);
			*/
		} catch (DataAccessException e) {

			LOG.error("Error al actualizar cliente con id {}", id);
			return control.DataAccess(e);
		}

		LOG.info("Cliente {} actualizado", id);
		return control.updated(clienteUpdated, TIPO_CLIENTE);
	}

	@DeleteMapping("/clientes/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		LOG.info("Metodo deleteCliente:");

		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {

			LOG.error("Error al borrar cliente con id: {}", id);
			return control.DataAccess(e);
		}

		LOG.info("Cliente {} borrado", id);
		return control.deleted(TIPO_CLIENTE);
	}
}