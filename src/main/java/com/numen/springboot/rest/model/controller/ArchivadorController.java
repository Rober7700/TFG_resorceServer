package com.numen.springboot.rest.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numen.springboot.rest.dto.ArchivadorPrendasDto;
import com.numen.springboot.rest.dto.PrendaArchivadaDto;
import com.numen.springboot.rest.model.service.IPrendaArchivadaService;

@RestController
@RequestMapping("/archivador")
public class ArchivadorController {

	@Autowired
	private IPrendaArchivadaService prendaSercive;
	
	@GetMapping("/get/{clienteId}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> getPrendaToCarrito(@PathVariable Long clienteId){
		ArchivadorPrendasDto archivadorPrendasDto = prendaSercive.obtenerArchivadorByClienteId(clienteId);
		if (archivadorPrendasDto == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(archivadorPrendasDto);
		}
	}
	
	@PostMapping("/post")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> postPrendaToArchivador(@RequestBody PrendaArchivadaDto dto){
		return prendaSercive.addPrendaAlArchivador(dto);
	}
	
	@DeleteMapping("{clienteId}/delete/{prendaId}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> eliminarPrendaDelArchivador(@PathVariable Long clienteId, @PathVariable Long prendaId) {
	    return prendaSercive.eliminarPrendaDelArchivador(clienteId, prendaId);
	}
	
}
