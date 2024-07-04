package com.numen.springboot.rest.dto;

import java.util.List;

public record ArchivadorPrendasDto(
		Long id,
		String email,
		List<PrendaArchivadaDto> prendaArchivadaDto
		) {

}
