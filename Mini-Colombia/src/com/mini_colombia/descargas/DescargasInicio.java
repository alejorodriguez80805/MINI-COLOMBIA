package com.mini_colombia.descargas;


import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.Resize;

public class DescargasInicio  extends ActivityGroup
{
	private static final int VERSION_ICE_CREAM_SANDWICH=14;
	
	public static DescargasInicio grupoDescargas;
	
	public ArrayList<View> historialViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descargas_inicio);
		this.grupoDescargas = this;
		
		historialViews = new ArrayList<View>();
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView)findViewById(R.id.tituloDescargasInicio);
		titulo.setTypeface(tipoMini);
		
		Resources res = getResources();
		ImageButton botonWallpapers = (ImageButton) findViewById(R.id.descargasImagenWallpapers);
		float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 296, res.getDisplayMetrics());
		Bitmap wallpapersPreliminar = BitmapFactory.decodeResource(res, R.drawable.thumbnail_wallpaper);
		Bitmap wallpapersFinal = Resize.resizeBitmap(wallpapersPreliminar, (int)Math.round(anchoImagen*0.45),(int)anchoImagen);
		botonWallpapers.setBackgroundDrawable(null);
		botonWallpapers.setImageBitmap(wallpapersFinal);
		
		ImageButton botonRingtones = (ImageButton) findViewById(R.id.descargasImagenRingtones);
		Bitmap ringtonesPreliminar = BitmapFactory.decodeResource(res, R.drawable.thumbnail_ringtones);
		Bitmap ringtonesFinal = Resize.resizeBitmap(ringtonesPreliminar, (int)Math.round(anchoImagen*0.45), (int)anchoImagen);
		botonRingtones.setBackgroundDrawable(null);
		botonRingtones.setImageBitmap(ringtonesFinal);
		
		
		
	}
	
	
	public void inicioWallpapers(View v)
	{
		if(hayConexionInternet())
		{
			Intent i = new Intent(DescargasInicio.this, DescargasWallpapers.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
			reemplazarView(v1);
		}
		else
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

	}
	
	
	public void inicioRingtones(View v)
	{
		if(hayConexionInternet())
		{
			if(Integer.valueOf(android.os.Build.VERSION.SDK_INT)>=VERSION_ICE_CREAM_SANDWICH)
			{
				Intent i = new Intent(DescargasInicio.this, DescargasRingtones.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
				reemplazarView(v1);
			}
			else
			{
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
				alertBuilder.setMessage("Necesitas una version mas avanzada de Android para acceder a esta seccion.");
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
			

		}
		else
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

	}
	
	
	public void reemplazarView(View v)
	{
		historialViews.add(v);
		setContentView(v);
	}
	
	public void back()
	{
		if(historialViews.size()>0)
		{
			
			historialViews.remove(historialViews.size() -1);
			if(historialViews.size()>0)
			{
				setContentView(historialViews.get(historialViews.size()-1));
			}
			else
				onCreate(null);
		}

		else
		{

			finish();
		}

	}
	
	@Override
	public void onBackPressed() 
	{
		DescargasInicio.grupoDescargas.back();
		return;
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
}
