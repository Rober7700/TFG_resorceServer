package com.numen.springboot.rest.model.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface ISubirFicheroService {
	public UrlResource load(String nombreFoto) throws MalformedURLException;

	public String copy(MultipartFile archivo) throws IOException;

	public boolean delete(String nombreFoto);

	public Path getPath(String nombreFoto);
}
