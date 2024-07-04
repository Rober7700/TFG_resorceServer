package com.numen.springboot.rest.model.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.numen.springboot.rest.model.service.ISubirFicheroService;
import com.numen.springboot.rest.utilities.IResponses;

@Service
public class SubirArchivoService implements ISubirFicheroService {

	private final Logger LOG = LoggerFactory.getLogger(SubirArchivoService.class);

	private final static String DIRECTORIO_UPLOAD = "uploads";

	@Autowired
	IResponses control;

	@Override
	public UrlResource load(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);
		UrlResource recurso = null;

		recurso = new UrlResource(rutaArchivo.toUri());

		if (!recurso.exists() && !recurso.isReadable()) {

			rutaArchivo = Paths.get("src/main/resources/static/img").resolve("IconoMas.png").toAbsolutePath();
			recurso = new UrlResource(rutaArchivo.toUri());
			LOG.info("Error no se pudo cargar la imagen: " + nombreFoto);
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile archivo) throws IOException {
		
		String nombreFile = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		Path rutaUpload = getPath(nombreFile);

		Files.copy(archivo.getInputStream(), rutaUpload);
		return nombreFile;
	}

	@Override
	public boolean delete(String nombreFoto) {
		LOG.info("Comprobando el nombre de la foto de la prenda: {}", nombreFoto);
		if (nombreFoto != null && nombreFoto.length() > 0) {

			Path rutaUploadAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			File fileFotoAnterior = rutaUploadAnterior.toFile();
			LOG.info("Comprobando la foto {} en la galeria para borrarla", nombreFoto);
			if (fileFotoAnterior.exists() && fileFotoAnterior.canRead()) {
				fileFotoAnterior.delete();
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
