package com.numen.springboot.rest.model.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numen.springboot.rest.dto.PrendaArchivadaDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "prenda_archivada")
public class PrendaArchivada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private BigDecimal precioOriginal; // Precio sin descuento
    
    private BigDecimal precioConDescuento; // Precio con descuento

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "prenda_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Prenda prenda;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cliente_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "archivadorPrendas_id")
	private ArchivadorPrendas archivadorPrendas;
	
	public PrendaArchivadaDto getPrendaArchivadaDto() {
		return new PrendaArchivadaDto(id, precioOriginal, precioConDescuento, prenda.getId(),
				prenda.getDescripcion(), prenda.getNombre(), cliente.getId(), prenda.getFotos(), 
				archivadorPrendas.getId(), prenda.isVendido());
	}

}
