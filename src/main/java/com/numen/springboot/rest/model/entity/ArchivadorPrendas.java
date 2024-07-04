package com.numen.springboot.rest.model.entity;

import java.util.List;

import com.numen.springboot.rest.dto.ArchivadorPrendasDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "archivador_prendas")
public class ArchivadorPrendas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "cliente_id", referencedColumnName = "id")
	private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "archivadorPrendas")
	private List<PrendaArchivada> prendaArchivada;

	public ArchivadorPrendasDto getArchivadorPrendaDto() {
		return new ArchivadorPrendasDto(id, cliente.getEmail(), null);
	}
	
}
