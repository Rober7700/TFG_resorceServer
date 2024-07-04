package com.numen.springboot.rest.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numen.springboot.rest.dto.CarritoItemDto;
import com.numen.springboot.rest.dto.FacturaDto;
import com.numen.springboot.rest.dto.FinPedidoDto;
import com.numen.springboot.rest.model.service.ICarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

	@Autowired
	private ICarritoService carritoSercive;
	
	@PostMapping("/post")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> postPrendaToCarrito(@RequestBody CarritoItemDto dto){
		return carritoSercive.addPrendaAlCarrito(dto);
	}
	
	@GetMapping("/get/{clienteId}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> getPrendaToCarrito(@PathVariable Long clienteId){
		FacturaDto factura = carritoSercive.obtenerCarritoByClienteId(clienteId);
		if (factura == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(factura);
		}
	}
	
	@PostMapping("/finPedido")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> finPedido(@RequestBody FinPedidoDto finPedidoDto){
		FacturaDto facturaDto = carritoSercive.finFactura(finPedidoDto);
		if (facturaDto == null) {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body(facturaDto);
		}
	}
	
	@GetMapping("/getFacturas/{clienteId}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> getFacturasCliente(@PathVariable Long clienteId){
		List<FacturaDto> listaFacturas = carritoSercive.obtenerFacturaByClienteId(clienteId);
		if (listaFacturas == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(listaFacturas);
		}
	}
	
	@GetMapping("/getFacturas")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getFacturas(){
		List<FacturaDto> listaFacturas = carritoSercive.obtenerAllFacturas();
		if (listaFacturas == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(listaFacturas);
		}
	}
	
	@DeleteMapping("{clienteId}/delete/{prendaId}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'OIDC_USER', 'ROLE_ADMIN')")
	public ResponseEntity<?> eliminarPrendaDelCarrito(@PathVariable Long clienteId, @PathVariable Long prendaId) {
	    return carritoSercive.eliminarPrendaDelCarrito(clienteId, prendaId);
	}

}
