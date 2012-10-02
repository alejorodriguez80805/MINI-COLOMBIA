package com.mini_colombia.vitrinas;

import java.util.ArrayList;

import com.mini_colombia.R;
import com.mini_colombia.comunidad.ComunidadGaleria;
import com.mini_colombia.comunidad.ComunidadInicio;
import com.mini_colombia.servicios.Resize;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class VitrinasInicio extends ActivityGroup
{
	public static VitrinasInicio grupoVitrinas;
	
	public ArrayList<View> historialViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vitrinas_inicio);
		this.grupoVitrinas = this;
		
		historialViews = new ArrayList<View>();
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView titulo = (TextView)findViewById(R.id.tituloVitrinas);
		titulo.setTypeface(tipoMini);
		
		Resources res = getResources();
		ImageView imagenInicio = (ImageView) findViewById(R.id.imagenInicioVitrinas);
		float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 194, res.getDisplayMetrics());
		Bitmap imagenPreliminar = BitmapFactory.decodeResource(res,R.drawable.vitrinas_inicio);
		Bitmap imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*1.6), (int)anchoImagen);
		imagenInicio.setImageBitmap(imagenFinal);

		Button b1 = (Button) findViewById(R.id.button1Vitrinas);
		b1.setTextSize(12);
		b1.setTypeface(tipoMini);
		b1.setTextColor(Color.rgb(208, 208, 208));

		Button b2 = (Button) findViewById(R.id.button2Vitrinas);
		b2.setTextSize(12);
		b2.setTypeface(tipoMini);
		b2.setTextColor(Color.rgb(208, 208, 208));
		
		Button b3 = (Button) findViewById(R.id.button3Vitrinas);
		b3.setTextSize(12);
		b3.setTypeface(tipoMini);
		b3.setTextColor(Color.rgb(208, 208, 208));
		
		Button b4 = (Button) findViewById(R.id.button4Vitrinas);
		b4.setTextSize(12);
		b4.setTypeface(tipoMini);
		b4.setTextColor(Color.rgb(208, 208, 208));
		
		Button b5 = (Button) findViewById(R.id.button5Vitrinas);
		b5.setTextSize(12);
		b5.setTypeface(tipoMini);
		b5.setTextColor(Color.rgb(208, 208, 208));
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
		VitrinasInicio.grupoVitrinas.back();
		return;
	}
	
	public void barranquilla(View v)
	{
		Intent i = new Intent(VitrinasInicio.this, VitrinasCiudad.class);
		i.putExtra("ciudad", 1);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	public void medellin(View v)
	{
		Intent i = new Intent(VitrinasInicio.this, VitrinasCiudad.class);
		i.putExtra("ciudad", 2);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	public void pereira(View v)
	{
		Intent i = new Intent(VitrinasInicio.this, VitrinasCiudad.class);
		i.putExtra("ciudad", 3);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	public void bogota(View v)
	{
		Intent i = new Intent(VitrinasInicio.this, VitrinasCiudad.class);
		i.putExtra("ciudad", 4);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
	
	public void cali(View v)
	{
		Intent i = new Intent(VitrinasInicio.this, VitrinasCiudad.class);
		i.putExtra("ciudad", 5);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
		reemplazarView(v1);
	}
}
