package com.numen.springboot.rest.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.numen.springboot.rest.dto.CarritoItemDto;
import com.numen.springboot.rest.dto.FacturaDto;
import com.numen.springboot.rest.dto.FinPedidoDto;
import com.numen.springboot.rest.dto.MensajeDto;
import com.numen.springboot.rest.model.entity.CarritoItems;
import com.numen.springboot.rest.model.entity.Cliente;
import com.numen.springboot.rest.model.entity.Factura;
import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.enumeration.EFacturaStatus;
import com.numen.springboot.rest.model.repository.ICarritoItemsRepository;
import com.numen.springboot.rest.model.repository.IClienteRepository;
import com.numen.springboot.rest.model.repository.IFacturaRepository;
import com.numen.springboot.rest.model.repository.IPrendaRepository;
import com.numen.springboot.rest.model.service.ICarritoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService implements ICarritoService {

	private static final Long CANTIDAD_POR_DEFECTO = 1L;
	private static final BigDecimal PRECIO_POR_DEFECTO = new BigDecimal("0.00");

	private final ICarritoItemsRepository carritoItemsRepository;
	private final IFacturaRepository facturaRepository;
	private final IPrendaRepository prendaRepository;
	private final IClienteRepository clienteRepository;

	@Override
	public ResponseEntity<?> addPrendaAlCarrito(CarritoItemDto dto) {
		Factura facturaPendiente = facturaRepository.findByClienteIdAndEstadoFactura(dto.clienteId(),
				EFacturaStatus.PENDIENTE);
		Optional<CarritoItems> optionalCarritoItem = carritoItemsRepository
				.findByClienteIdAndPrendaIdAndFacturaId(dto.clienteId(), dto.prendaId(), facturaPendiente.getId());
		if (optionalCarritoItem.isPresent()) {
			CarritoItemDto prendaExistente = new CarritoItemDto(null, null, null, null, null, null, null, null, null, null, false);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(prendaExistente);
		} else {
			Optional<Prenda> optionalPrenda = prendaRepository.findById(dto.prendaId());
			Optional<Cliente> optionalCliente = clienteRepository.findById(dto.clienteId());
			if (optionalPrenda.isPresent() && optionalCliente.isPresent()) {
				Prenda prenda = optionalPrenda.get();
				CarritoItems carritoItem = CarritoItems.builder().prenda(prenda).cliente(optionalCliente.get())
						.cantidad(CANTIDAD_POR_DEFECTO).factura(facturaPendiente)
						.precioOriginal(prenda.getPrecioOriginal()).precioConDescuento(prenda.getPrecioConDescuento())
						.build();
				CarritoItems carritoActualizado = carritoItemsRepository.save(carritoItem);
				facturaPendiente
						.setPrecioOriginal(facturaPendiente.getPrecioOriginal().add(carritoItem.getPrecioOriginal()));
				facturaPendiente.setPrecioConDescuento(
						facturaPendiente.getPrecioConDescuento().add(carritoItem.getPrecioConDescuento()));
				facturaPendiente.getCarritoItems().add(carritoItem);
				facturaRepository.save(facturaPendiente);
				CarritoItemDto carritoDtoActualizado = new CarritoItemDto(carritoActualizado.getId(), null, null, null,
						null, null, null, null, null, null, false);
				return ResponseEntity.status(HttpStatus.CREATED).body(carritoDtoActualizado);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MensajeDto("Usuario o producto no encontrado"));
			}
		}
	}

	@Override
	public FacturaDto obtenerCarritoByClienteId(Long clienteId) {
		Factura facturaPendiente = facturaRepository.findByClienteIdAndEstadoFactura(clienteId,
				EFacturaStatus.PENDIENTE);
		List<CarritoItemDto> carritoItemDtoList = facturaPendiente.getCarritoItems().stream()
				.map(CarritoItems::getCarritoItemDto).collect(Collectors.toList());
		FacturaDto facturaDto = new FacturaDto(carritoItemDtoList, facturaPendiente.getEstadoFactura(), facturaPendiente.getId(),
				null, facturaPendiente.getPrecioConDescuento(), facturaPendiente.getPrecioOriginal(), null);
		return facturaDto;
	}

	@Override
	public FacturaDto finFactura(FinPedidoDto finPedidoDto) {
		Factura facturaPendiente = facturaRepository.findByClienteIdAndEstadoFactura(finPedidoDto.clienteId(),
				EFacturaStatus.PENDIENTE);
		Optional<Cliente> optionalCliente = clienteRepository.findById(finPedidoDto.clienteId());
		if (optionalCliente.isPresent()) {
			facturaPendiente.setDate(new Date());
			facturaPendiente.setEstadoFactura(EFacturaStatus.COMPLETADO);
			facturaPendiente.setMetodoPago(finPedidoDto.metodoPago());
			facturaPendiente.setPrecioConDescuento(facturaPendiente.getPrecioConDescuento());
			facturaPendiente.setPrecioOriginal(facturaPendiente.getPrecioOriginal());
			facturaRepository.save(facturaPendiente);
			Factura factura = Factura.builder().estadoFactura(EFacturaStatus.PENDIENTE).cliente(optionalCliente.get())
					.precioOriginal(PRECIO_POR_DEFECTO).precioConDescuento(PRECIO_POR_DEFECTO).build();
			facturaRepository.save(factura);
			List<CarritoItems> listaCarritos = facturaPendiente.getCarritoItems();
			for (CarritoItems carritoItems : listaCarritos) {
				carritoItems.getPrenda().setVendido(true);
				carritoItems.getPrenda().setEscaparate(false);
				prendaRepository.save(carritoItems.getPrenda());
			}
			return facturaPendiente.getFacturaDto();
		}
		return null;
	}

	@Override
	public List<FacturaDto> obtenerFacturaByClienteId(Long clienteId) {
		return facturaRepository.findAllByClienteIdAndEstadoFactura(clienteId, EFacturaStatus.COMPLETADO).stream()
				.map(Factura::getFacturaDto).collect(Collectors.toList());
	}

	@Override
	public List<FacturaDto> obtenerAllFacturas() {
		return facturaRepository.findAllByEstadoFactura(EFacturaStatus.COMPLETADO).stream().map(Factura::getFacturaDto)
				.collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<?> eliminarPrendaDelCarrito(Long clienteId, Long prendaId) {
		Factura facturaPendiente = facturaRepository.findByClienteIdAndEstadoFactura(clienteId,
				EFacturaStatus.PENDIENTE);
		Optional<Prenda> optionalPrenda = prendaRepository.findById(prendaId);
		Optional<CarritoItems> optionalCarritoItem = carritoItemsRepository
				.findByClienteIdAndPrendaIdAndFacturaId(clienteId, prendaId, facturaPendiente.getId());
		if (optionalCarritoItem.isPresent() && optionalPrenda.isPresent()) {
			CarritoItems carritoItem = optionalCarritoItem.get();
			facturaPendiente
					.setPrecioOriginal(facturaPendiente.getPrecioOriginal().subtract(carritoItem.getPrecioOriginal()));
			facturaPendiente.setPrecioConDescuento(
					facturaPendiente.getPrecioConDescuento().subtract(carritoItem.getPrecioConDescuento()));
			carritoItemsRepository.delete(carritoItem);
			if (facturaPendiente.getCarritoItems().isEmpty()) {
				facturaPendiente.setPrecioOriginal(PRECIO_POR_DEFECTO);
				facturaPendiente.setPrecioConDescuento(PRECIO_POR_DEFECTO);
			}
			facturaRepository.save(facturaPendiente);
			return ResponseEntity.ok(new MensajeDto("Elemento eliminado del carrito correctamente"));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public void cambiarPrecioConDescuento(Long prendaId, BigDecimal nuevoPrecioConDescuento) {
		List<Factura> facturasPendientes = facturaRepository.findAllByEstadoFactura(EFacturaStatus.PENDIENTE);

		for (Factura factura : facturasPendientes) {
			for (CarritoItems carrito : factura.getCarritoItems()) {
				if (carrito.getPrenda().getId().equals(prendaId)
						&& !carrito.getPrecioConDescuento().equals(nuevoPrecioConDescuento)) {
					factura.setPrecioConDescuento(factura.getPrecioConDescuento()
							.subtract(carrito.getPrecioConDescuento().subtract(nuevoPrecioConDescuento)));
					carrito.setPrecioConDescuento(nuevoPrecioConDescuento);
					carritoItemsRepository.save(carrito);
					facturaRepository.save(factura);
				}
			}
		}
	}

}
