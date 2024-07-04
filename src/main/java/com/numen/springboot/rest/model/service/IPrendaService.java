package com.numen.springboot.rest.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.enumeration.EPrenda;

public interface IPrendaService {
	
	public List<Prenda> findAll();
	
	public Page<Prenda> findAll(Pageable pageable);	
	
	public Prenda findById(Long id);

	public Prenda save(Prenda prenda);

	public void delete(Long id);

	List<Prenda> findByTipo(EPrenda tipo);
	
	List<Prenda> findByTalla(String talla);

	List<Prenda> findByEscaparate();

	List<Prenda> findByVendido();
}

