package com.numen.springboot.rest.dto;

import java.math.BigDecimal;
import java.util.List;

public record CarritoItemDto(
		Long id,
	    BigDecimal precioOriginal,
	    BigDecimal precioConDescuento,
		Long cantidad,
		Long prendaId,
		Long facturaId,
		String nombre,
		String descripcion,
		List<String> imagenes,
		Long clienteId,
		boolean vendido
) {}
