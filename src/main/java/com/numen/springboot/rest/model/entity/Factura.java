package com.numen.springboot.rest.model.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.numen.springboot.rest.dto.CarritoItemDto;
import com.numen.springboot.rest.dto.FacturaDto;
import com.numen.springboot.rest.model.enumeration.EFacturaStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "facturas")
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "cliente_id", referencedColumnName = "id")
	private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "factura")
	private List<CarritoItems> carritoItems;

	private Date date;

	@Enumerated(EnumType.STRING)
	private EFacturaStatus estadoFactura;

	private String metodoPago;

	private BigDecimal precioConDescuento;

	private BigDecimal precioOriginal;

	public FacturaDto getFacturaDto() {
		List<CarritoItemDto> carritoList = carritoItems.stream()
				.map(CarritoItems::getCarritoItemDto).collect(Collectors.toList());;
		return new FacturaDto(carritoList, estadoFactura, id, metodoPago, precioConDescuento, precioOriginal, date);
	}
}
