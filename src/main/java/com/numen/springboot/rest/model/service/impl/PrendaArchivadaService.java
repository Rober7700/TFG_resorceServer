package com.numen.springboot.rest.model.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.numen.springboot.rest.dto.ArchivadorPrendasDto;
import com.numen.springboot.rest.dto.MensajeDto;
import com.numen.springboot.rest.dto.PrendaArchivadaDto;
import com.numen.springboot.rest.model.entity.ArchivadorPrendas;
import com.numen.springboot.rest.model.entity.Cliente;
import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.entity.PrendaArchivada;
import com.numen.springboot.rest.model.repository.IArchivadorPrendasRepository;
import com.numen.springboot.rest.model.repository.IClienteRepository;
import com.numen.springboot.rest.model.repository.IPrendaArchivadaRepository;
import com.numen.springboot.rest.model.repository.IPrendaRepository;
import com.numen.springboot.rest.model.service.IPrendaArchivadaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrendaArchivadaService implements IPrendaArchivadaService{

	private final IPrendaArchivadaRepository prendaArchivadaRepository;
	private final IArchivadorPrendasRepository archivadorPrendasRepository;
	private final IPrendaRepository prendaRepository;
	private final IClienteRepository clienteRepository;
	
	@Override
	public ResponseEntity<?> addPrendaAlArchivador(PrendaArchivadaDto dto) {
		ArchivadorPrendas archivadorPrendas = archivadorPrendasRepository.findByClienteId(dto.clienteId());
		Optional<PrendaArchivada> optionalPrendaArchivada = prendaArchivadaRepository.findByClienteIdAndPrendaIdAndArchivadorPrendasId(
				dto.clienteId(), dto.prendaId(), archivadorPrendas.getId());
		if (optionalPrendaArchivada.isPresent()) {
			PrendaArchivadaDto prendaArchivada = new PrendaArchivadaDto(null, null, null, null, null, null, null, null, null, false);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(prendaArchivada); 
		} else {
			Optional<Prenda> optionalPrenda = prendaRepository.findById(dto.prendaId());
			Optional<Cliente> optionalCliente = clienteRepository.findById(dto.clienteId());
			if (optionalPrenda.isPresent() && optionalCliente.isPresent()) {
				Prenda prenda = optionalPrenda.get();
				PrendaArchivada prendaArchivada = PrendaArchivada.builder()
						.prenda(prenda)
						.cliente(optionalCliente.get())
						.precioOriginal(prenda.getPrecioOriginal())
						.precioConDescuento(prenda.getPrecioConDescuento())
						.archivadorPrendas(archivadorPrendas)
						.build();
				PrendaArchivada prendaActualizada = prendaArchivadaRepository.save(prendaArchivada);
				archivadorPrendas.getPrendaArchivada().add(prendaActualizada);
				archivadorPrendasRepository.save(archivadorPrendas);
				PrendaArchivadaDto archivadaDto = new PrendaArchivadaDto(prendaActualizada.getId(), null, null, null, null, null, null, null, null, false);
				return ResponseEntity.status(HttpStatus.CREATED).body(archivadaDto);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeDto("Usuario o producto no encontrado"));
			}
		}
	}

	@Override
	public ResponseEntity<?> eliminarPrendaDelArchivador(Long clienteId, Long prendaId) {
		ArchivadorPrendas archivadorPrendas = archivadorPrendasRepository.findByClienteId(clienteId);
		Optional<Prenda> optionalPrenda = prendaRepository.findById(prendaId);
		Optional<PrendaArchivada> optionalPrendaArchivada = prendaArchivadaRepository.findByClienteIdAndPrendaIdAndArchivadorPrendasId(clienteId, prendaId, archivadorPrendas.getId());
	    if (optionalPrendaArchivada.isPresent() && optionalPrenda.isPresent()) {
	    	PrendaArchivada prendaArchivada = optionalPrendaArchivada.get();
	        prendaArchivadaRepository.delete(prendaArchivada);
	        archivadorPrendasRepository.save(archivadorPrendas);
	        return ResponseEntity.ok(new MensajeDto("Elemento eliminado del carrito correctamente"));
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@Override
	public ArchivadorPrendasDto obtenerArchivadorByClienteId(Long clienteId) {
		ArchivadorPrendas archivadorPrendas = archivadorPrendasRepository.findByClienteId(clienteId);
		List<PrendaArchivadaDto> prendaArchivadaDto = archivadorPrendas.getPrendaArchivada()
				.stream().map(PrendaArchivada::getPrendaArchivadaDto).collect(Collectors.toList());
		ArchivadorPrendasDto archivadorPrendasDto = new ArchivadorPrendasDto(archivadorPrendas.getId(), null, prendaArchivadaDto);
		return archivadorPrendasDto;
	}
	
	@Override
	public void cambiarPrecioConDescuento(Long prendaId, BigDecimal nuevoPrecioConDescuento) {
	    List<ArchivadorPrendas> listArchivadorPrendas = archivadorPrendasRepository.findAll();

	    for (ArchivadorPrendas archivadorPrendas : listArchivadorPrendas) {
	    	for (PrendaArchivada prendaArchivada : archivadorPrendas.getPrendaArchivada()) {
		        if (prendaArchivada.getPrenda().getId().equals(prendaId) && !prendaArchivada.getPrecioConDescuento().equals(nuevoPrecioConDescuento)) {
		            prendaArchivada.setPrecioConDescuento(nuevoPrecioConDescuento);
		            prendaArchivadaRepository.save(prendaArchivada);
		        }
		    }	
		}
	}


}
