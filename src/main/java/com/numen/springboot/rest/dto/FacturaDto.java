package com.numen.springboot.rest.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.numen.springboot.rest.model.enumeration.EFacturaStatus;

public record FacturaDto(
		List<CarritoItemDto> carritoItemDtoList,
		EFacturaStatus estadoFactura,
		Long id,
		String metodoPago,
		BigDecimal precioConDescuento,
		BigDecimal precioOriginal,
		Date date) {

}
