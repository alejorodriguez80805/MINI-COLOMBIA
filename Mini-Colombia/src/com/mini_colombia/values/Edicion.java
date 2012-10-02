package com.mini_colombia.values;

import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class Edicion implements Serializable
{
	
	//Atributos
	
	private static final long serialVersionUID = 1L;

	private String imagen;

	private String nombre;
	
	private String descripcion;
	
	private String thumbnail;
	
	private boolean test_drive;

	private String templateColor;
	

	
	public Edicion(String imagen, String nombre, String descripcion, String thumbnail, boolean test_drive, String templateColor)
	{
		this.imagen = imagen;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.thumbnail = thumbnail;
		this.test_drive = test_drive;
		this.templateColor = templateColor;
	}

	public String getTemplateColor() {
		return templateColor;
	}

	public void setTemplateColor(String templateColor) {
		this.templateColor = templateColor;
	}

	public String getImagen() 
	{
		return imagen;
	}

	public void setImagen(String imagen) 
	{
		this.imagen = imagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	public String getDescripcion() 
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion) 
	{
		this.descripcion = descripcion;
	}

	public String getThumbnail() 
	{
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) 
	{
		this.thumbnail = thumbnail;
	}

	public boolean isTest_drive() 
	{
		return test_drive;
	}

	public void setTest_drive(boolean test_drive) 
	{
		this.test_drive = test_drive;
	}
	
	
}
