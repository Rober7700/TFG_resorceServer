package com.numen.springboot.rest.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numen.springboot.rest.model.entity.Factura;
import com.numen.springboot.rest.model.enumeration.EFacturaStatus;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, Long>{

	Factura findByClienteIdAndEstadoFactura(Long clienteId, EFacturaStatus estadoFactura);

	List<Factura> findAllByClienteIdAndEstadoFactura(Long clienteId, EFacturaStatus estadoFactura);

	List<Factura> findAllByEstadoFactura(EFacturaStatus estadoFactura);

}
