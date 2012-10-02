package com.mini_colombia.comunidad;

import java.util.ArrayList;

import com.mini_colombia.R;
import com.mini_colombia.auxiliares.Evento;
import com.mini_colombia.servicios.AsyncTaskListener;
import com.mini_colombia.servicios.DescargarImagenOnline;
import com.mini_colombia.servicios.Resize;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComunidadEventosT5 extends Activity implements AsyncTaskListener<ArrayList<Bitmap>>
{
	private String contenido;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comunidad_eventos_t5);
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView) findViewById(R.id.tituloT2);
		titulo.setTypeface(tipoMini);
		
		Evento e = (Evento)getIntent().getSerializableExtra("objeto");
		
		String tituloImagen = e.getTitulo();
		TextView tTituloImagen = (TextView)findViewById(R.id.tituloImagenT5);
		tTituloImagen.setText(tituloImagen);
		
		LinearLayout layoutFondoTitulo =(LinearLayout) findViewById(R.id.linearFondoTituloT5);
		String[] rgbFondo =e.getTemplateColor().split(",");
		layoutFondoTitulo.setBackgroundColor(Color.rgb(Integer.parseInt(rgbFondo[0]), Integer.parseInt(rgbFondo[1]), Integer.parseInt(rgbFondo[2])));
		
		contenido = e.getContenido();
		String urlImagen1 = e.getUrlImagenes().get(0);
		String urlImagen2 = e.getUrlImagenes().get(1);
		String[] url = {urlImagen1,urlImagen2};
		DescargarImagen tarea = new DescargarImagen(darContexto(), this);
		tarea.execute(url);
	}
	
	private class DescargarImagen extends AsyncTask<String, Void, ArrayList<Bitmap>>
	{

		private Context context;

		private AsyncTaskListener<ArrayList<Bitmap>> callback;

		private ProgressDialog progress;
		
		private ArrayList<Bitmap> imagenes;

		public DescargarImagen(Context context, AsyncTaskListener<ArrayList<Bitmap>> callback)
		{
			this.context = context;
			this.callback = callback;
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(context,"","Cargando imagen...",false);
		}

		@Override
		protected ArrayList<Bitmap> doInBackground(String... params) 
		{
			imagenes = new ArrayList<Bitmap>();
			
			Bitmap imagen1Preliminar = DescargarImagenOnline.descargarImagen(params[0]);
			Bitmap imagen1Final = Resize.resizeBitmap(imagen1Preliminar,  345, 450);
			imagenes.add(imagen1Final);
			
			Bitmap imagen2Preliminar = DescargarImagenOnline.descargarImagen(params[1]);
			Bitmap imagen2Final = Resize.resizeBitmap(imagen2Preliminar,293, 203);
			imagenes.add(imagen2Final);
			
			return imagenes;
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) 
		{
			progress.dismiss();
			callback.onTaskComplete(result);
		}

	}

	@Override
	public void onTaskComplete(ArrayList<Bitmap> result) 
	{
		ImageView imagen1 = (ImageView) findViewById(R.id.imagenT51);
		imagen1.setImageBitmap(result.get(1));
		
		ImageView imagen2 = (ImageView) findViewById(R.id.imagenT52);
		imagen2.setImageBitmap(result.get(0));
		

		
		WebView wv = (WebView) findViewById(R.id.webViewT5);
		wv.loadData(contenido, "text/html", "charset=UTF-8");
		
		
	}
	
	public Context darContexto()
	{
		Context context = null;
		if (getParent() != null) 
			context = getParent();
		return context;
	}
	
	public void abrirFacebook(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/MINI"));
		startActivity(i);
	}

	public void abrirTwitter(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MINI"));
		startActivity(i);
	}


	

}
