package com.numen.springboot.rest.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.enumeration.EPrenda;
import com.numen.springboot.rest.model.repository.IPrendaRepository;
import com.numen.springboot.rest.model.service.IPrendaService;

@Service
public class PrendaService implements IPrendaService {
	
	@Autowired
	private IPrendaRepository prendaRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findAll() {
		return (List<Prenda>) prendaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Prenda> findAll(Pageable pageable) {
		return prendaRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Prenda findById(Long id) {
		return prendaRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public Prenda save(Prenda prenda) {
		return prendaRepository.save(prenda);
	}

	@Override
	@Transactional()
	public void delete(Long id) {
		prendaRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findByTipo(EPrenda tipo) {
		return prendaRepository.findByTipoAndVendidoFalse(tipo).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findByTalla(String talla) {
		return prendaRepository.findByTallaAndVendidoFalse(talla).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findByVendido() {
		return prendaRepository.findByVendidoTrue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findByEscaparate() {
		return prendaRepository.findByEscaparateTrue();
	}
}
