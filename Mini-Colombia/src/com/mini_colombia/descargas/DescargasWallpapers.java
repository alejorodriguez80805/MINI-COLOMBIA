package com.mini_colombia.descargas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.ImagenGaleria;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.AsyncTaskListener;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.Resize;

public class DescargasWallpapers extends Activity implements AsyncTaskListener<ArrayList<ImagenGaleria>>
{



	private static final String NOMBRE_CARPETA = "MINI";

	private static final String SEPARADOR = "/";

	private static final String EXTENSION = ".jpg";

	private static final int AVANCE_IMAGENES = 3;


	private int numActualImagenes;


	private ArrayList<Bitmap> thumbnails;
//	private ArrayList<Bitmap> imagenes;

	private int numWallpapers;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		thumbnails = new ArrayList<Bitmap>();
//		imagenes = new ArrayList<Bitmap>();
		setContentView(R.layout.activity_descargas_wallpapers);
		//Se pone true dado que es la primera vez que se hace fetch de los wallpapers
		DescargarThumbnails tarea = new DescargarThumbnails(darContexto(), this, true);
		tarea.execute();

		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView)findViewById(R.id.tituloDescargasWallpapers);
		titulo.setTypeface(tipoMini);

		numActualImagenes = 0;


	}

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}


	private class DescargarThumbnails extends AsyncTask<String, Integer, ArrayList<ImagenGaleria>>
	{

		private ProgressDialog progress;

		private ArrayList<ImagenGaleria> arregloImagenes;

		private Context context;

		private AsyncTaskListener<ArrayList<ImagenGaleria>> callback;

		private boolean primeraVez;

		public DescargarThumbnails(Context context, AsyncTaskListener<ArrayList<ImagenGaleria>> callback, boolean primeraVez)
		{
			this.context = context;
			this.callback = callback;
			this.primeraVez = primeraVez;
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			progress = ProgressDialog.show(darContexto(),"","Cargando...",false);
		}


		/**
		 * Metodo que descarga tanto 
		 */
		protected ArrayList<ImagenGaleria> doInBackground(String... params)
		{

			Parser jparser = new Parser();
			JSONObject jsonObject;
			arregloImagenes = new ArrayList<ImagenGaleria>();
			Resources res = getResources();
			float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 296, res.getDisplayMetrics());
			if(primeraVez)
			{
				jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_DESCARGAS_WALLPAPERS));
				try 
				{
					numWallpapers = Integer.parseInt(jsonObject.getString(getString(R.string.TAG_WALLPAPERS_NUMERO)));
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
				jsonObject = jparser.getJSONFromUrl(getString(R.string.CONSTANTE_DESCARGAS_SIGUIENTES_WALLPAPERS)+params[0]);

			try 
			{
				JSONArray wallpapers = jsonObject.getJSONArray(getString(R.string.TAG_WALLPAPERS));
				for(int i=0;i<wallpapers.length();i++)
				{
					JSONObject wallpaper = wallpapers.getJSONObject(i);

					String nombre = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_NOMBRE));

					//Manejo thumbnail
					String urlThumbnail = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_THUMBNAIL));
					Bitmap thumbnailPreliminar = DescargarImagenOnline.descargarImagen(urlThumbnail);
					Bitmap thumbnailPostResize= Resize.resizeBitmap(thumbnailPreliminar, (int) Math.round(anchoImagen*0.66),(int) anchoImagen);
					thumbnails.add(thumbnailPostResize);

					//Manejo imagen
					String urlImagen = wallpaper.getString(getString(R.string.TAG_WALLPAPERS_IMAGEN));
					//				Bitmap imagen = DescargarImagenOnline.descargarImagen(urlImagen));
					//				imagenes.add(DescargarImagenOnline.descargarImagen(urlImagen));
					ImagenGaleria elemento = new ImagenGaleria(nombre, thumbnailPostResize, urlImagen);
					arregloImagenes.add(elemento);
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return arregloImagenes;




		}

		@Override
		protected void onPostExecute(ArrayList<ImagenGaleria> result) 
		{
			if(progress.isShowing())
				progress.dismiss();
			callback.onTaskComplete(result);
		}



	}

	public void onTaskComplete(ArrayList<ImagenGaleria> result)
	{
		numActualImagenes+=AVANCE_IMAGENES;

		LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.linearLayoutWallpapers);

		for(int i=0;i<result.size();i++)
		{
			final int j =i;
			final int k = result.size();
			final String url= result.get(i).getImagen();
			final String titulo = result.get(i).getNombre();
			View v = new View(this);
			LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(0,10);
			v.setLayoutParams(vParams);
			layoutPrincipal.addView(v);

			RelativeLayout relLayout = new RelativeLayout(this);
			relLayout.setBackgroundColor(Color.TRANSPARENT);
			relLayout.setBackgroundDrawable(new BitmapDrawable(result.get(i).getThumbnail()));
			
			int x = result.get(i).getThumbnail().getWidth();
			int y = result.get(i).getThumbnail().getHeight();
			
			RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(x,y);




			Button thumbnail = new Button(this);
			RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			thumbnail.setLayoutParams(bParams);
			thumbnail.setBackgroundColor(Color.TRANSPARENT);
			bParams.setMargins(0, 7, 0, 0);
			thumbnail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) 
				{
					Intent i1 = new Intent(DescargasWallpapers.this, com.mini_colombia.comunidad.ComunidadImagen.class);
					i1.putExtra("posActual", j+1);
					i1.putExtra("size", k);
					i1.putExtra("url",url);
					i1.putExtra("titulo", titulo);
					i1.putExtra("tieneTitulo",false);
					i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					View v1 = DescargasInicio.grupoDescargas.getLocalActivityManager().startActivity("", i1).getDecorView();
					DescargasInicio actividadPadre = (DescargasInicio) getParent();
					actividadPadre.reemplazarView(v1);




				}
			});
			relLayout.addView(thumbnail);

			Button bDescargar = new Button(this);
			bDescargar.setBackgroundColor(Color.TRANSPARENT);
			final String urlImagen = result.get(i).getImagen();
			final String nombreImagen = result.get(i).getNombre();
			bDescargar.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					Bitmap b = null;
					String path = Environment.getExternalStorageDirectory().toString() + SEPARADOR + NOMBRE_CARPETA;
					File directorio = new File(path);
					if(!directorio.exists())
						directorio.mkdir();

					File f = new File(path,nombreImagen+ EXTENSION);
					if(!f.exists())
					{
						try 
						{
							b = new DescargarImagen().execute(urlImagen).get();
						} 
						catch (InterruptedException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						catch (ExecutionException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						OutputStream fos;
						try 
						{
							fos = new FileOutputStream(f);
							b.compress(Bitmap.CompressFormat.JPEG, 85, fos);
							fos.flush();
							fos.close();
							MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), nombreImagen+ EXTENSION, f.getName());
							AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
							alertBuilder.setMessage("La imagen ha sido descargada.");
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
						catch (FileNotFoundException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else
					{
						AlertDialog.Builder alertBuilder = new AlertDialog.Builder(darContexto());
						alertBuilder.setMessage("La imagen ya ha sido descargada.");
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
			});
			RelativeLayout.LayoutParams paramsbutton = new RelativeLayout.LayoutParams( (int) Math.round((float)x*0.3) , (int) Math.round((float) y*0.2));
			paramsbutton.setMargins( (int) Math.round((float)x*0.65) , (int) Math.round((float) y*0.8), 0, 0);
			bDescargar.setLayoutParams(paramsbutton);
			relLayout.addView(bDescargar);

			layoutPrincipal.addView(relLayout, relParams);
		}

		Button verMas = new Button(this);
		verMas.setBackgroundColor(Color.BLACK);
		LinearLayout.LayoutParams paramsVerMas = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		paramsVerMas.setMargins(0, 7, 0, 0);
		verMas.setLayoutParams(paramsVerMas);
		verMas.setText("VER MAS");
		verMas.setTextSize(12);
		verMas.setTextColor(Color.WHITE);
		verMas.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		verMas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				if(numActualImagenes<numWallpapers)
				{
					ejecutarTareaDescargarThumbnails();
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
		});
		layoutPrincipal.addView(verMas);





	}

	private class DescargarImagen extends AsyncTask<String, Void, Bitmap>
	{

		private ProgressDialog progress;

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(darContexto(),"","Descargando imagen...",false);
		}

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			return DescargarImagenOnline.descargarImagen(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) 
		{
			progress.dismiss();
			super.onPostExecute(result);
		}

	}


	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}

	/**
	 * Metodo que es utilizado por metodo onclick del boton verMas para bajar mas wallpapers, es decir llamar la tarea asincronica.
	 */
	public void ejecutarTareaDescargarThumbnails()
	{
		new DescargarThumbnails(darContexto(), this, false).execute(""+numWallpapers);

	}

	public boolean hayConexionInternet()
	{
		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo red  = conMgr.getActiveNetworkInfo();
		boolean conexionInternet = red!=null && red.getState() == NetworkInfo.State.CONNECTED;
		return conexionInternet;
	}

	public void lanzarDialogoInternet()
	{
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setMessage("Debes tener accesso a internet para esta secci—n de la aplicacion");
		alertBuilder.setCancelable(false);
		alertBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				onCreate(null);
			}
		});
		AlertDialog alerta = alertBuilder.create();
		alerta.show();
	}

	public Context darContextoDialogo()
	{
		return this;
	}


}
