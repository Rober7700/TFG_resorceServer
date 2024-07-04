package com.numen.springboot.rest.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numen.springboot.rest.model.entity.CarritoItems;

@Repository
public interface ICarritoItemsRepository extends JpaRepository<CarritoItems, Long>{

	Optional<CarritoItems> findByClienteIdAndPrendaIdAndFacturaId(Long clienteId, Long prendaId, Long facturaId);

}
