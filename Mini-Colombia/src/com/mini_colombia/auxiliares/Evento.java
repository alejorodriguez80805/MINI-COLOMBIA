package com.mini_colombia.auxiliares;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

public class Evento implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subtitulo;
	
	private String contenido;
	
	private String fecha;
	
	private String template;
	
	private String posicion;
	
	private String titulo;
	
	private Bitmap thumbnailURL;
	
	private ArrayList<String> urlImagenes;
	
	private String templateColor;
	
	public Evento(String subtitulo, String contenido, String fecha, String template, String posicion, String titulo, Bitmap thumbnailURL, ArrayList<String> urlImagenes, String templateColor)
	{
		this.subtitulo = subtitulo;
		this.contenido = contenido;
		this.fecha = fecha;
		this.template = template;
		this.posicion = posicion;
		this.titulo = titulo;
		this.thumbnailURL = thumbnailURL;
		this.urlImagenes = urlImagenes;
		this.templateColor = templateColor;
	}

	public String getTemplateColor() {
		return templateColor;
	}

	public void setTemplateColor(String templateColor) {
		this.templateColor = templateColor;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Bitmap getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(Bitmap thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public ArrayList<String> getUrlImagenes() {
		return urlImagenes;
	}
}
