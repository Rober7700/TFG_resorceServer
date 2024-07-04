package com.numen.springboot.rest.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.numen.springboot.rest.dto.CarritoItemDto;
import com.numen.springboot.rest.dto.FacturaDto;
import com.numen.springboot.rest.dto.FinPedidoDto;

public interface ICarritoService {

	ResponseEntity<?> addPrendaAlCarrito(CarritoItemDto dto);

	FacturaDto obtenerCarritoByClienteId(Long clienteId);
	
	FacturaDto finFactura(FinPedidoDto finPedidoDto);
	
	List<FacturaDto> obtenerFacturaByClienteId(Long clienteId);
	
	List<FacturaDto> obtenerAllFacturas();
	
	ResponseEntity<?> eliminarPrendaDelCarrito(Long clienteId, Long prendaId);

	void cambiarPrecioConDescuento(Long prendaId, BigDecimal nuevoPrecioConDescuento);
	
}
