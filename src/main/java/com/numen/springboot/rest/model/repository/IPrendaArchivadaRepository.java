package com.numen.springboot.rest.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numen.springboot.rest.model.entity.PrendaArchivada;

@Repository
public interface IPrendaArchivadaRepository extends JpaRepository<PrendaArchivada, Long>{
	
	Optional<PrendaArchivada> findByClienteIdAndPrendaIdAndArchivadorPrendasId(Long clienteId, Long prendaId, Long archivadorPrendasId);
}
