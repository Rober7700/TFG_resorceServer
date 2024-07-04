package com.numen.springboot.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public record PrendaArchivadaDto(
		Long id,
	    BigDecimal precioOriginal,
	    BigDecimal precioConDescuento,
		Long prendaId,
		String descripcion,
		String nombre,
		Long clienteId,
		List<String> imagenes,
		Long archivadorPrendasId,
		boolean vendido) {

}
