package com.numen.springboot.rest.model.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import com.numen.springboot.rest.dto.ArchivadorPrendasDto;
import com.numen.springboot.rest.dto.PrendaArchivadaDto;

public interface IPrendaArchivadaService {

	ResponseEntity<?> addPrendaAlArchivador(PrendaArchivadaDto dto);
	
	ResponseEntity<?> eliminarPrendaDelArchivador(Long clienteId, Long prendaId);
	
	ArchivadorPrendasDto obtenerArchivadorByClienteId(Long clienteId);

	void cambiarPrecioConDescuento(Long prendaId, BigDecimal nuevoPrecioConDescuento);
}
