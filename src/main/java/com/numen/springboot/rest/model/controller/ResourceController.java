package com.numen.springboot.rest.model.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numen.springboot.rest.dto.MensajeDto;

@RestController
@RequestMapping("/resource")
public class ResourceController {

	@GetMapping("/user")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER')")
	public ResponseEntity<MensajeDto> usuario(Authentication authentication){
		return ResponseEntity.ok(new MensajeDto("Hola " + authentication.getName()));
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<MensajeDto> admin(Authentication authentication){
		return ResponseEntity.ok(new MensajeDto("Hola Mr." + authentication.getName()));
	}
	
	@GetMapping("/anonymus")
	public ResponseEntity<MensajeDto> anonimo(){
		return ResponseEntity.ok(new MensajeDto("Hola desconocido"));
	}
}
