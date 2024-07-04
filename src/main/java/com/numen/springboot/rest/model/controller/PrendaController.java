package com.numen.springboot.rest.model.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.numen.springboot.rest.model.entity.Prenda;
import com.numen.springboot.rest.model.enumeration.EPrenda;
import com.numen.springboot.rest.model.service.ICarritoService;
import com.numen.springboot.rest.model.service.IPrendaArchivadaService;
import com.numen.springboot.rest.model.service.IPrendaService;
import com.numen.springboot.rest.model.service.ISubirFicheroService;
import com.numen.springboot.rest.utilities.IResponses;

import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/almacen")
public class PrendaController {

	private static final String TIPO_PRENDA = "prenda";

	private final Logger LOG = LoggerFactory.getLogger(PrendaController.class);

	@Autowired
	IResponses control;

	@Autowired
	private ISubirFicheroService uploadService;
	
	@Autowired
	private IPrendaService prendaService;	
	
	@Autowired
	private ICarritoService carritoService;
	
	@Autowired
	private IPrendaArchivadaService archivadorService;

	@GetMapping("/prendas")
	public List<Prenda> getAllPrendas() {
		LOG.info("Metodo getAllPrendas:");
		return prendaService.findAll();
	}

    @GetMapping("/prendas/tipo/{tipo}")
    public ResponseEntity<?> findByTipo(@PathVariable String tipo) {

    	List<Prenda> lista = null;
    	
    	lista = prendaService.findByTipo(EPrenda.valueOf(tipo));
    	
    	if (lista == null) {
			LOG.error("No hay prendas de ese tipo");
			return control.NotFound(tipo, TIPO_PRENDA);
		}
    	
        return control.found(lista, TIPO_PRENDA);
    }
    
    @GetMapping("/prendas/ropa/{talla}")
    public ResponseEntity<?> findRopaByTalla(@PathVariable String talla) {
        List<Prenda> listaTalla = prendaService.findByTalla(talla);
        
        if (listaTalla.isEmpty()) {
            LOG.error("No se encontraron prendas para la talla especificada");
            return control.NotFound(talla, TIPO_PRENDA);
        }
        
        return control.found(listaTalla, TIPO_PRENDA);
    }
    
    @GetMapping("/prendas/vendido")
    public ResponseEntity<?> findRopaByVendido() {
    	List<Prenda> lista = prendaService.findByVendido();
        
        if (lista.isEmpty()) {
            LOG.error("No se encontraron prendas para la talla especificada");
            return control.NotFound("Vendido", TIPO_PRENDA);
        }
        
        return control.found(lista, TIPO_PRENDA);
    }
    
    @GetMapping("/prendas/escaparate")
    public ResponseEntity<?> findRopaByEscaparate() {
        List<Prenda> lista = prendaService.findByEscaparate();
        
        if (lista.isEmpty()) {
            LOG.error("No se encontraron prendas para la talla especificada");
            return control.NotFound("Escaparate" + "", TIPO_PRENDA);
        }
        
        return control.found(lista, TIPO_PRENDA);
    }
	
	@GetMapping("/prendas/{id}")
	public ResponseEntity<?> getPrenda(@PathVariable Long id) {

		LOG.info("Metodo getPrenda:");
		Prenda prenda = null;

		try {
			prenda = prendaService.findById(id);
			if (prenda == null) {
				LOG.error("Prenda {} no encontrada ", id);
				return control.NotFound(id, TIPO_PRENDA);
			}
		} catch (DataAccessException e) {
			LOG.error("Error al intentar encontrar prenda");
			return control.DataAccess(e);
		}

		LOG.info("Prenda encontrada con id: {}", id);
		return control.found(prenda, TIPO_PRENDA);
	}
	
	@PostMapping("/prendas")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> create(@Valid @RequestBody Prenda prenda, BindingResult result) {

		LOG.info("Metodo createPrenda:");
		Prenda prendaNew = null;

		if (result.hasErrors()) {
			LOG.error("Error en el result");
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			return control.badRequest(errors);
		}

		try {
			prendaNew = prendaService.save(prenda);
		} catch (DataAccessException e) {
			LOG.error("Error al intentar guardar prenda");
			return control.DataAccess(e);
		}

		LOG.info("Prenda creada con id: {}", prenda.getId());
		return control.created(prendaNew, TIPO_PRENDA);
	}

	@PutMapping("/prendas/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> update(@Valid @RequestBody Prenda prenda, BindingResult result, @PathVariable Long id) {

		LOG.info("Metodo updatePrenda:");

		LOG.info("Buscando prenda con id: {}", id);
		Prenda prendaActual = prendaService.findById(id);

		Prenda prendaUpdated = null;

		if (result.hasErrors()) {
			LOG.error("Error en el result");
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			return control.badRequest(errors);
		}

		if (prendaActual == null) {
			LOG.error("Error en el encontrando la prenda con id {}", id);
			return control.NotFound(id, TIPO_PRENDA);
		}

		try {

			LOG.info("Asignamos valores de color, talla y fecha a la prenda con id {}", id);
			prendaActual.setTipo(prenda.getTipo());
			prendaActual.setColor(prenda.getColor());
			prendaActual.setTalla(prenda.getTalla());
			prendaActual.setDescripcion(prenda.getDescripcion());
			prendaActual.setPrecioOriginal(prenda.getPrecioOriginal());
			BigDecimal precioActual = prendaActual.getPrecioConDescuento();
			prendaActual.setPrecioConDescuento(prenda.getPrecioConDescuento());
			prendaActual.setVendido(prenda.isVendido());
			prendaActual.setEscaparate(prenda.isEscaparate());
			prendaActual.setCreateAt(prendaActual.getCreateAt());
			LOG.info("Intentamos guardar la prenda con id {}", id);
			prendaUpdated = prendaService.save(prendaActual);

			if (precioActual != prenda.getPrecioConDescuento()) {
				carritoService.cambiarPrecioConDescuento(id, prenda.getPrecioConDescuento());
				archivadorService.cambiarPrecioConDescuento(id, prenda.getPrecioConDescuento());
				
			}
		} catch (DataAccessException e) {
			LOG.error("Error en el encontrando la prenda con id", id);
			return control.DataAccess(e);
		}

		LOG.info("Prenda {} actualizada", id);
		return control.updated(prendaUpdated, TIPO_PRENDA);
	}

	@DeleteMapping("/prendas/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		LOG.info("Metodo deletePrenda: ");
		try {
			LOG.info("Buscando prenda con id: {}", id);
			Prenda prenda = prendaService.findById(id);
			List<String> nombreFotoAnterior = prenda.getFotos();

			for (String foto : nombreFotoAnterior) {
				uploadService.delete(foto);
			}
			prendaService.delete(id);
		} catch (DataAccessException e) {
			LOG.error("Error en borarndo la prenda con id: {}", id);
			return control.DataAccess(e);
		}
		LOG.info("Prenda borrada");
		return control.deleted(TIPO_PRENDA);
	}

	@PostMapping("/prendas/upload")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files, @RequestParam("id") Long id) {

	    LOG.info("Metodo uploadPrenda:");
	    LOG.info("Buscando prenda con id: {}", id);
	    Prenda prenda = prendaService.findById(id);

	    List<String> nombreFiles = new ArrayList<>();

	    for (MultipartFile file : files) {
	        LOG.info("Comprobando el file: {}", file.getOriginalFilename());
	        if (!file.isEmpty()) {
	            try {
	                String nombreFile = uploadService.copy(file);
	                nombreFiles.add(nombreFile);
	            } catch (IOException e) {
	                LOG.error("Error copiando el file");
	                control.handleIOException(e);
	            }
	        }
	    }

	    List<String> nombreFotosAnteriores = prenda.getFotos();
	    
	    if (nombreFotosAnteriores != null) {
		    for (String nombreFotoAnterior : nombreFotosAnteriores) {
		        uploadService.delete(nombreFotoAnterior);
		    }
	    }

	    LOG.info("Guardando las fotos {} a la prenda con id {}", nombreFiles, id);
	    prenda.setFotos(nombreFiles);
	    prendaService.save(prenda);

	    return control.updatedFoto(prenda, TIPO_PRENDA);
	}


	@GetMapping("/prendas/img/{nombreFoto:.+}")
	public ResponseEntity<UrlResource> verFoto(@PathVariable String nombreFoto) {

		LOG.info("Metodo verFoto:");
		
		UrlResource recurso = null;
		try {
			recurso = uploadService.load(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpHeaders cabecera = new HttpHeaders();
	    cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"");

		return new ResponseEntity<UrlResource>(recurso, cabecera, HttpStatus.OK);
	}
}
