package com.numen.springboot.rest.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.enumeration.EPrenda;

@Repository
public interface IPrendaRepository extends JpaRepository<Prenda, Long> {

	Optional<List<Prenda>> findByTipoAndVendidoFalse(EPrenda tipo);
	
	Optional<List<Prenda>> findByTallaAndVendidoFalse(String talla);

	List<Prenda> findByVendidoTrue();

	List<Prenda> findByEscaparateTrue();

}
