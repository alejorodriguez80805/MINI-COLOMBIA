package com.mini_colombia.familia;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Edicion;

public class FamiliaEdicion extends Activity
{
	private static final String EDICION = "edicion";
	
	private static final String MODELO = "MODELO";
	
	
	private String nombreModelo;
	
	
	private Edicion e;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_edicion);
		
		nombreModelo = getIntent().getStringExtra(MODELO);
		e = (Edicion)getIntent().getSerializableExtra("objeto");
		
		final String nombreEdicion = e.getNombre();
		
		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		
		//Creacion del titulo
		TextView textoTitulo = (TextView) findViewById(R.id.TituloEdicion);
		textoTitulo.setText(nombreModelo);
		textoTitulo.setTypeface(tipoMini);
		
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutFamiliaModelo);
		
		Resources res = getResources();
		float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,296 , res.getDisplayMetrics());
		float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1 , res.getDisplayMetrics());
		
		//Agregar la imagen de la edicion
		String nombreImagen = e.getImagen();
		ImageView iv = new ImageView(this);
		LayoutParams parametrosImagen = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int  idImagen = getResources().getIdentifier(nombreImagen, "drawable", getApplicationContext().getPackageName());
		Bitmap imagenPreliminar = BitmapFactory.decodeResource(res, idImagen);
		Bitmap imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int) Math.round(anchoImagen*0.66),(int) anchoImagen);
		iv.setImageBitmap(imagenFinal);
		iv.setBackgroundColor(color.transparent);
//		iv.setBackgroundResource(getResources().getIdentifier(nombreImagen, "drawable", getApplicationContext().getPackageName()));
		iv.setLayoutParams(parametrosImagen);
		frameLayout.addView(iv);
		
		String[] colores = e.getTemplateColor().split(",");
		
		//Verificacion de test drive
		if(e.isTest_drive())
		{
			
			
			//Agregar el boton de test drive
			LinearLayout.LayoutParams parametros = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Button b = new Button(this);
			b.setBackgroundColor(Color.rgb(Integer.parseInt(colores[0]),Integer.parseInt(colores[1]),Integer.parseInt(colores[2])));
			b.setText(R.string.solicitar_test_drive);
			b.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			b.setPadding((int) Math.round(pixel*3), (int) Math.round(pixel*3), (int) Math.round(pixel*3),(int) Math.round(pixel*3));
			b.setLayoutParams(parametros);
			b.setTypeface(tipoMini);
			
			//Verificacion del nivel de rojo para escoger el color de la letra
			double rojo =(double)(Integer.parseInt(colores[0]));
			double constante = 256*0.79;
			if(rojo>constante)
			{
				b.setTextColor(Color.WHITE);
			}
			else
				b.setTextColor(Color.BLACK);
			frameLayout.addView(b);
			b.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i = new Intent(FamiliaEdicion.this, FamiliaTestDrive.class);
					i.putExtra(EDICION, nombreEdicion);
					View v1 = FamiliaInicio.grupoFamilia.getLocalActivityManager().startActivity("", i).getDecorView();
					FamiliaInicio actividadPadre =(FamiliaInicio) getParent(); 
					actividadPadre.reemplazarView(v1);
				}
			});
		}
		
		//Agregar el layout para poner el nombre de la edicion
		LinearLayout layoutNombreEdicion = (LinearLayout) findViewById(R.id.layoutFamiliaNombreEdicion);
		layoutNombreEdicion.setBackgroundColor(Color.rgb(Integer.parseInt(colores[0]),Integer.parseInt(colores[1]),Integer.parseInt(colores[2])));
		
		TextView tNombreEdicion = (TextView) findViewById(R.id.textEdicionNombreEdicion);
		tNombreEdicion.setText(e.getNombre());
		tNombreEdicion.setTypeface(tipoMini);
		tNombreEdicion.setTextColor(Color.rgb(Integer.parseInt(colores[0]),Integer.parseInt(colores[1]),Integer.parseInt(colores[2])));
		tNombreEdicion.setPadding((int) Math.round(pixel*7), (int) Math.round(pixel*4), 0, 0);
		
		
		//Manejo del texto
		WebView webView = (WebView)findViewById(R.id.webFamiliaModelo);
		String html = e.getDescripcion();
		webView.loadData(html, "text/html", "charset=UTF-8");

		
	}
	
	
	
	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}

	
	
}
