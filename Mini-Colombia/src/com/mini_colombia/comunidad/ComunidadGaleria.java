package com.mini_colombia.comunidad;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.ImagenGaleria;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.AsyncTaskListener;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.ImageAdapter;
import com.mini_colombia.servicios.Resize;

public class ComunidadGaleria extends Activity implements AsyncTaskListener<ArrayList<ImagenGaleria>>
{

	private ArrayList<ImagenGaleria> arregloImagenes;
	private ArrayList<String> imagenes;
	private int numImagenes;
	private int numActualImagenes;
	private static final int AVANCE_IMAGENES =4;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comunidad_galeria);

		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView) findViewById(R.id.textoGaleria);
		titulo.setText("GALERIA.");
		titulo.setTypeface(tipoMini);
		String url = getString(R.string.CONSTANTE_COMUNIDAD_GALERIA);
		inicializarTareaAsincrona();

		Button verMas = (Button) findViewById(R.id.comunidadGaleriaBoton);
		verMas.setTypeface(tipoMini);
		numActualImagenes = 0;
		arregloImagenes = new ArrayList<ImagenGaleria>();

	}

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	} 


	private class DescargarInformacion extends AsyncTask<String, Void, ArrayList<ImagenGaleria>>
	{	
		private ArrayList<ImagenGaleria> arreglo;

		private Context context;

		private AsyncTaskListener<ArrayList<ImagenGaleria>> callback;

		private ProgressDialog progress;

		private boolean primeraVez;
		Resources res;

		public DescargarInformacion(Context context, AsyncTaskListener<ArrayList<ImagenGaleria>> callback, boolean primeraVez)
		{
			this.context = context;

			this.callback = callback;

			this.primeraVez = primeraVez;
			
			res = getResources();
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(darContexto(),"","Cargando imagenes...",false);
		}

		@Override
		protected ArrayList<ImagenGaleria> doInBackground(String... params) 
		{
			arreglo = new ArrayList<ImagenGaleria>();
			Parser jparser = new Parser();
			JSONObject jsonObject;
			if(primeraVez)
			{
				jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_COMUNIDAD_GALERIA));
				try 
				{
					numImagenes = Integer.parseInt(jsonObject.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_NUMERO)));
				} 
				catch (NumberFormatException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else
				jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_COMUNIDAD_GALERIA_SIGUIENTES)+ params[0]);


			try 
			{
				JSONArray imagenes = jsonObject.getJSONArray(getString(R.string.TAG_COMUNIDAD_GALERIA_IMAGENES));
				float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 145.5, res.getDisplayMetrics());
				for(int i = 0; i<imagenes.length(); i++)
				{
					JSONObject imagen = imagenes.getJSONObject(i);

					String nombre = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_NOMBRE));
					String thumbnailURL = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_THUMBNAIL));
					Bitmap imagenThumbnailPreliminar = DescargarImagenOnline.descargarImagen(thumbnailURL);
					Bitmap imagenThumbnailFinal = Resize.resizeBitmap(imagenThumbnailPreliminar, (int) anchoImagen, (int) anchoImagen);
					
					String imagenURL = imagen.getString(getString(R.string.TAG_COMUNIDAD_GALERIA_IMAGEN));
					ImagenGaleria j = new ImagenGaleria(nombre, imagenThumbnailFinal, imagenURL);
					arreglo.add(j);

				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return arreglo;
		}

		@Override
		protected void onPostExecute(ArrayList<ImagenGaleria> result) 
		{ 
			if(progress.isShowing())
				progress.dismiss();
			callback.onTaskComplete(result);
		}

	}


	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}

	@Override
	public void onTaskComplete(ArrayList<ImagenGaleria> result) 
	{
		numActualImagenes+=AVANCE_IMAGENES;
		arregloImagenes.addAll(result);
		final GridView grid = (GridView) findViewById(R.id.gridGaleria);
		Resources res = getResources();
		grid.setAdapter(new ImageAdapter(this, arregloImagenes, darPadre(),res));

	}

	public void inicializarTareaAsincrona ()
	{
		DescargarInformacion tarea = new DescargarInformacion(darContexto(),this, true);
		tarea.execute();
	}

	public Activity darPadre()
	{
		return getParent();
	}

	public void verMas(View v)
	{
		int a = numActualImagenes;
		int b = numImagenes;
		
		if(numActualImagenes<numImagenes)
		{
			DescargarInformacion tarea = new DescargarInformacion(darContexto(),this, false);
			tarea.execute(""+numActualImagenes);
		}
		else
		{
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
			alertBuilder.setMessage("Ha llegado al limite de imagenes.");
			alertBuilder.setCancelable(false);
			alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					
				}
			});
			AlertDialog alerta = alertBuilder.create();
			alerta.show();
		}

	}




}
