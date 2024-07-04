package com.numen.springboot.rest.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numen.springboot.rest.model.entity.ArchivadorPrendas;

@Repository
public interface IArchivadorPrendasRepository extends JpaRepository<ArchivadorPrendas, Long>{

	ArchivadorPrendas findByClienteId(Long clienteId);
}
