package com.mini_colombia;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mini_colombia.dao.DaoPersistencia;
import com.mini_colombia.dao.DaoTimestamp;
import com.mini_colombia.db.DataBaseHelper;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.values.Noticia;
import com.mini_colombia.values.Persistencia;
import com.mini_colombia.values.Timestamp;


public class InicioActivity extends Activity 
{
	private DataBaseHelper databaseHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);


		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo red  = conMgr.getActiveNetworkInfo();
		boolean conexionInternet = red!=null && red.getState() == NetworkInfo.State.CONNECTED;


		try 
		{
			Dao<Persistencia, Integer> dao = getHelper().darDaoPersistencia();
			if(!DaoPersistencia.hayPersistencia(dao) && conexionInternet)
			{
				new FetchInicialNoticias().execute("");

			}
			else if(!DaoPersistencia.hayPersistencia(dao) && !conexionInternet)
			{
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
				alertBuilder.setMessage("Debes tener accesso a internet la primera vez que ejecutas la aplicacion");
				alertBuilder.setCancelable(false);
				alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
				{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						finish();
						
						
					}
				});
				AlertDialog alerta = alertBuilder.create();
				alerta.show();

			}
			else if(conexionInternet)
			{
				new ActualizarNoticias().execute("");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//////////////////////////////////////////////////////////////////
	//Metodos de carga inicial de datos
	//////////////////////////////////////////////////////////////////
	

	
	private class FetchInicialNoticias extends AsyncTask<String, Integer, Boolean>
	{
		private Dao<Noticia, Integer> daoNoticia;
		
		private Dao<Timestamp, Integer> daoTimestamp;
		
		private Dao<Persistencia, Integer> daoPersistencia;

		ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Carga inicial de noticias...",false);
		}

		@Override
		protected Boolean doInBackground(String... params) 
		{
			daoNoticia = darDaoNoticia();
			daoTimestamp = darDaoTimestamp();
			daoPersistencia = darDaoPersistencia();
			
			Parser jparser = new Parser();
			String s = (getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL))+"/0";
			JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+"0");


			try 
			{
				String timeStamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
				BigDecimal fTimestamp =new BigDecimal(timeStamp);
				DaoTimestamp.primeraVez(daoTimestamp, fTimestamp,"noticias");
				
				DaoPersistencia.primeraVez(daoPersistencia);
				
				JSONArray noticias = jsonObject.getJSONArray(getString(R.string.TAG_NOTICIAS));

				for(int i = 0; i<noticias.length();i++)
				{
					JSONObject noticia = noticias.getJSONObject(i);
					String categoria = noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
					String resumen = noticia.getString(getString(R.string.TAG_NOTICIAS_RESUMEN));
					String pagina = noticia.getString(getString(R.string.TAG_NOTICIAS_PAGINA));
					String titulo = noticia.getString(getString(R.string.TAG_NOTICIAS_TITULO));

					//Manejo imagen thumbnail
					String thumbnail = noticia.getString(getString(R.string.TAG_NOTICIAS_THUMBNAIL));
					String nombreImagenThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA)+categoria;
					descargarImagen(thumbnail, nombreImagenThumbnail);
					String fechaCreacion = noticia.getString(getString(R.string.TAG_NOTICIAS_FECHA_CREACION));
					String url = noticia.getString(getString(R.string.TAG_NOTICIAS_URL));

					Noticia tNoticia = new Noticia(categoria,resumen,pagina,titulo,nombreImagenThumbnail,fechaCreacion,url);

					daoNoticia.create(tNoticia);

				}

				List<Noticia> n= daoNoticia.queryForAll();
				for(Noticia n1: n)
				{
					Log.d("asfasdfadf", n1.getTitulo());
				}

			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss(); 

		}

	}
	
	//////////////////////////////////////////////////////////////////
	//Metodos de actualizacion
	//////////////////////////////////////////////////////////////////
	

	
	private class ActualizarNoticias extends AsyncTask<String, Integer, Boolean>
	{

		private Dao<Timestamp, Integer> daoTimestamp;
		private Dao<Noticia, Integer> daoNoticia;
		private boolean hayActualizaciones;

		ProgressDialog progress;
		
		
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Actualizando noticias...",false);
		}

		@Override
		protected Boolean doInBackground(String... params) 
		{
			daoNoticia = darDaoNoticia();
			daoTimestamp = darDaoTimestamp();
			Parser jparser = new Parser();
			



			JSONArray noticias;
			try 
			{
				BigDecimal d = DaoTimestamp.darTimestamp(daoTimestamp,"noticias");
				String s = getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+DaoTimestamp.darTimestamp(daoTimestamp,"noticias")+"/";
				JSONObject jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_CARGAR_NOTICIAS_URL)+DaoTimestamp.darTimestamp(daoTimestamp,"noticias")+"/");
				
				hayActualizaciones = jsonObject.getBoolean(getString(R.string.TAG_HAY_ACTUALIZACIONES));
				if(hayActualizaciones)
				{
					
					String timeStamp = jsonObject.getString(getString(R.string.TAG_TIMESTAMP));
					BigDecimal fTimestamp =new BigDecimal(timeStamp);
					DaoTimestamp.primeraVez(daoTimestamp, fTimestamp,"noticias");
					
					
					
					noticias = jsonObject.getJSONArray(getString(R.string.TAG_NOTICIAS));
					for(int i = 0; i<noticias.length();i++)
					{
						UpdateBuilder<Noticia, Integer> updateBuilder = daoNoticia.updateBuilder();
						JSONObject noticia = noticias.getJSONObject(i);
						updateBuilder.updateColumnValue("categoria", noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA)));
						updateBuilder.updateColumnValue("resumen", noticia.getString(getString(R.string.TAG_NOTICIAS_RESUMEN)));
//						String pagina = noticia.getString(getString(R.string.TAG_NOTICIAS_PAGINA));
//						updateBuilder.updateColumnValue("pagina", pagina);
						updateBuilder.updateColumnValue("titulo", noticia.getString(getString(R.string.TAG_NOTICIAS_TITULO)));

						//Manejo del thumbnail
						String nombreImagenEliminar = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA)+noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
						eliminarImagen( nombreImagenEliminar);
						String thumbnail = noticia.getString(getString(R.string.TAG_NOTICIAS_THUMBNAIL));
						String nombreThumbnail = getString(R.string.CONSTANTE_NOMBRE_THUMBNAIL_NOTICIA) + noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA));
						descargarImagen(thumbnail, nombreThumbnail);
						updateBuilder.updateColumnValue("thumbnail", nombreThumbnail);

						updateBuilder.updateColumnValue("fechaCreacion",noticia.getString(getString(R.string.TAG_NOTICIAS_FECHA_CREACION)));
						updateBuilder.updateColumnValue("url",noticia.getString(getString(R.string.TAG_NOTICIAS_URL)));
						updateBuilder.where().eq("categoria", noticia.getString(getString(R.string.TAG_NOTICIAS_CATEGORIA)));
						daoNoticia.update(updateBuilder.prepare());



					}
				}
				

				
				List<Noticia> n = daoNoticia.queryForAll();
				n.size();
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SQLException e) 
			{
				Log.d("Tutorial", e.getMessage());
				e.printStackTrace();
			}
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if(progress.isShowing())
				progress.dismiss();
		}
	}
	

	//////////////////////////////////////////////////////////////////
	//Metodo para manejo de las imagenes dentro del dispositivo
	//////////////////////////////////////////////////////////////////

	/**
	 * Metodo que descarga una imagen de una url y la guarda en el dispostivo
	 * @param bitmap
	 * @param nombre
	 */
	public void descargarImagen(String dirUrl, String nombre)
	{
		URL url;
		Bitmap bitmap = null;
		try 
		{
			url = new URL(dirUrl);

			//Seccion de descarga de la imagen
			HttpURLConnection connection= (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream is = connection.getInputStream();
			bitmap=BitmapFactory.decodeStream(is);


			//Seccion de almacenamiento de la imagen
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			FileOutputStream fos = openFileOutput(nombre, Context.MODE_WORLD_WRITEABLE);
			fos.write(bytes.toByteArray());
			fos.close();



		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			Toast toast = Toast.makeText(this, "Se perdio el accesso a Internet. Reinicia la aplicacion", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
	}

	/**
	 * Metodo utilizado para eliminar una imagen. Este se usa para los updates y los deletes
	 * @param nombre
	 */
	public void eliminarImagen(String nombre)
	{
		this.deleteFile(nombre);
	}

	//////////////////////////////////////////////////////////////////
	//Metodos para obtener los objetos DAO de cada una de las tablas
	//////////////////////////////////////////////////////////////////

	/**
	 * Metodo necesario para la creacion de un objeto DAO para acceder a cada uno de las tablas
	 * @return
	 */
	public DataBaseHelper getHelper()
	{
		if(databaseHelper == null)
			databaseHelper = OpenHelperManager.getHelper(this,DataBaseHelper.class);
		return databaseHelper;
	}


//	public Dao<Edicion, String> darDaoEdicion()
//	{
//
//		Dao<Edicion, String> daoEdicion = null;
//		try 
//		{
//			daoEdicion = getHelper().darDaoEdicion();
//		} 
//		catch (SQLException e) 
//		{
//			e.printStackTrace();
//		}
//		return daoEdicion;
//	}
//
//	public Dao<Modelo,String> darDaoModelo()
//	{
//		Dao<Modelo, String> daoModelo = null;
//
//		try 
//		{
//			daoModelo = getHelper().darDaoModelo();
//		} 
//		catch (SQLException e) 
//		{
//			e.printStackTrace();
//		}
//		return daoModelo;
//
//	}

	public Dao<Persistencia, Integer> darDaoPersistencia()
	{
		Dao<Persistencia, Integer> daoPErsistencia = null;

		try
		{
			daoPErsistencia = getHelper().darDaoPersistencia();
		}
		catch (SQLException e) 
		{

		}
		return daoPErsistencia;
	}

	public Dao<Timestamp, Integer> darDaoTimestamp()
	{
		Dao<Timestamp, Integer> daoTimestamp = null;

		try 
		{
			daoTimestamp = getHelper().darDaoTimestamp();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return daoTimestamp;
	}

	public Dao<Noticia, Integer> darDaoNoticia()
	{
		Dao<Noticia, Integer> daoNoticia = null;
		try 
		{
			daoNoticia = getHelper().darDaoNoticia();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return daoNoticia;
	}


	//////////////////////////////////////////////////////////////////
	//Metodos de la clase activity
	//////////////////////////////////////////////////////////////////


	/**
	 * Metodo que retorna el contexto de esta actividad
	 * @return
	 */
	public Context darContexto()
	{
		return this;
	}


	////////////////////////////////////////////////
	// Metodos para el manejo de los botones
	////////////////////////////////////////////////


	public void inicioInstrucciones(View v)
	{
		Intent i = new Intent(InicioActivity.this, InstruccionesActivity.class);
		startActivity(i);
	}

	public void inicioTabs(View v)
	{
		Intent i = new Intent(InicioActivity.this, ManejadorTabs.class);
		i.putExtra("tab", 0);
		startActivity(i);
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if(databaseHelper != null)
			OpenHelperManager.releaseHelper();
		databaseHelper = null;
	}



}
