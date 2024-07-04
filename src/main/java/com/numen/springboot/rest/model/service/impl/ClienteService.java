package com.numen.springboot.rest.model.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.numen.springboot.rest.model.entity.ArchivadorPrendas;
import com.numen.springboot.rest.model.entity.Cliente;
import com.numen.springboot.rest.model.entity.Factura;
import com.numen.springboot.rest.model.enumeration.EFacturaStatus;
import com.numen.springboot.rest.model.repository.IArchivadorPrendasRepository;
import com.numen.springboot.rest.model.repository.IClienteRepository;
import com.numen.springboot.rest.model.repository.IFacturaRepository;
import com.numen.springboot.rest.model.service.IClienteService;

@Service
public class ClienteService implements IClienteService {

	private static final BigDecimal PRECIO_POR_DEFECTO = new BigDecimal("0.00");
	
	@Autowired
	private IClienteRepository clienteRepository;
	@Autowired
	private IFacturaRepository facturaRepository;
	@Autowired
	private IArchivadorPrendasRepository archivadorPrendasRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteRepository.findAll();
	}

	@Override

	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional()
	public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional()
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findByEmail(String email) {
		return clienteRepository.findByEmail(email).orElse(null);
	}

	@Override
	public Cliente findById(Long id) {
		return null;
	}

	@Override
	public Cliente iniciarClienteCarrito(String email) {
		Cliente clienteRegistrado = iniciarCliente(email);
		Factura factura = new Factura();
		factura.setPrecioOriginal(PRECIO_POR_DEFECTO);
		factura.setPrecioConDescuento(PRECIO_POR_DEFECTO);
		factura.setEstadoFactura(EFacturaStatus.PENDIENTE);
		factura.setCliente(clienteRegistrado);
		facturaRepository.save(factura);
		ArchivadorPrendas archivadorPrendas = ArchivadorPrendas.builder()
				.cliente(clienteRegistrado)
				.build();
		archivadorPrendasRepository.save(archivadorPrendas);
		return clienteRegistrado;
	}
	
	private Cliente iniciarCliente(String email) {
		Cliente newCliente = Cliente.builder()
				.email(email)
				.build();
		return save(newCliente);
	}

}
