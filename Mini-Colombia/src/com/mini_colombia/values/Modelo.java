package com.mini_colombia.values;

import java.io.Serializable;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class Modelo implements Serializable
{
	

	//Atributos
	
	private static final long serialVersionUID = 1L;


	private String imagen;
	

	private String nombre;
	

	private String thumbnail;
	
	private ArrayList<Edicion> ediciones;

	/**
	 * Constructor
	 * @param imagen
	 * @param nombre
	 * @param descripcion
	 */
	public Modelo(String imagen, String nombre, String thumbnail, ArrayList<Edicion> ediciones)
	{
		this.imagen = imagen;
		this.nombre = nombre;
		this.thumbnail = thumbnail;
		this.ediciones = ediciones;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getImagen() 
	{
		return imagen;
	}


	public void setImagen(String imagen) 
	{
		this.imagen = imagen;
	}


	public String getNombre() 
	{
		return nombre;
	}


	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	
	public ArrayList<Edicion> getEdiciones()
	{
		return ediciones;
	}

	
	
	
}
