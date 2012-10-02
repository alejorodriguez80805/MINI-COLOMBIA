package com.mini_colombia.vitrinas;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.servicios.Resize;

public class VitrinasCiudad extends Activity
{
	private static final String ESPACIO =  " ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vitrinas_ciudad);
		
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		Resources res = getResources();
		float anchoImagen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 283, res.getDisplayMetrics());
		float anchoImagenMecanico = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 198, res.getDisplayMetrics());
		float margenIzquierdaImagenMecanico = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
		float margenArribaImagenMecanico = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, res.getDisplayMetrics());
		float margenArribaTexto = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,7, res.getDisplayMetrics());
		
		int ciudad = getIntent().getIntExtra("ciudad", 0);
		ImageView imagenConsecionario = (ImageView) findViewById(R.id.vitrinaCiudadImagen);
		
		TextView textoLoAtendera = (TextView) findViewById(R.id.textoVitrinaCiudadLoAtendera);
		textoLoAtendera.setTypeface(tipoMini);
		
		TextView tituloCiudad = (TextView) findViewById(R.id.textVitrinasCiudad);
		TextView direccionCiudad = (TextView) findViewById(R.id.vitrinaCiudadDireccion);
		TextView telefonoCiudad = (TextView) findViewById(R.id.vitrinaCiudadTelefono);
		ImageView imagenMecanico = (ImageView) findViewById(R.id.vitrinaCiudadImagenMecanico);
		TextView  nombreMecanico = (TextView) findViewById(R.id.vitrinasCiudadNombreMecanico);
		TextView mailMecanico = (TextView) findViewById(R.id.vitrinasCiudadMailMecanico);
		TextView celularMecanico = (TextView) findViewById(R.id.vitrinasCiudadCelularMecanico);
		
		Bitmap imagenPreliminar;
		Bitmap imagenFinal;
		
		Bitmap imagenPreliminarMecanico;
		Bitmap imagenFinalMecanico;
		
		switch (ciudad) 
		{
		case 1:
			tituloCiudad.setText("BARRANQUILLA.");
			
			imagenPreliminar = BitmapFactory.decodeResource(res, R.drawable.imagen_vitrina_ciudad_bquilla);
			imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*0.71), (int) anchoImagen);
			imagenConsecionario.setImageBitmap(imagenFinal);
			
			direccionCiudad.setText(R.string.TAG_VITRINAS_DIRECCION_BARRANQUILLA);
			telefonoCiudad.setText(getString(R.string.TAG_VITRINAS_TELEFONO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_TELEFONO_BARRANQUILLA));
			
			imagenPreliminarMecanico = BitmapFactory.decodeResource(res, R.drawable.vitrinas_imagen_mecanico_bquilla);
			imagenFinalMecanico = Resize.resizeBitmap(imagenPreliminarMecanico, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico.setImageBitmap(imagenFinalMecanico);
			
			nombreMecanico.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_BQUILLA));
			mailMecanico.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_BQUILLA));
			celularMecanico.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_BQUILLA));
			break;
		case 2:
			tituloCiudad.setText("MEDELLIN.");
			
			imagenPreliminar = BitmapFactory.decodeResource(res, R.drawable.imagen_vitrina_ciudad_medellin);
			imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*0.71), (int) anchoImagen);
			imagenConsecionario.setImageBitmap(imagenFinal);
			
			direccionCiudad.setText(R.string.TAG_VITRINAS_DIRECCION_MEDELLIN);
			telefonoCiudad.setText(getString(R.string.TAG_VITRINAS_TELEFONO_CONSTANTE) + ESPACIO+ getString(R.string.TAG_VITRINAS_TELEFONO_MEDELLIN));
			
			imagenPreliminarMecanico = BitmapFactory.decodeResource(res, R.drawable.vitrinas_imagen_mecanico_medellin);
			imagenFinalMecanico = Resize.resizeBitmap(imagenPreliminarMecanico, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico.setImageBitmap(imagenFinalMecanico);
			
			nombreMecanico.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_MEDELLIN));
			mailMecanico.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_MEDELLIN));
			celularMecanico.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_MEDELLIN));
			
			
			//Codigo para agregar la foto del segundo vendedor
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutVitrinasCiudad);
			
			ImageView imagenSeparador = new ImageView(this);
			imagenSeparador.setBackgroundResource(R.drawable.divisor_test_drive);
			LinearLayout.LayoutParams parametrosSeparador = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			imagenSeparador.setLayoutParams(parametrosSeparador);
			layout.addView(imagenSeparador);
			
			ImageView imagenMecanico2 = new ImageView(this);
			Bitmap imagenPreliminarMecanico2 = BitmapFactory.decodeResource(res,R.drawable.vitrinas_imagen_mecanico_medellin_2);
			Bitmap imagenFinalMecanico2 = Resize.resizeBitmap(imagenPreliminarMecanico2, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico2.setImageBitmap(imagenFinalMecanico2);
			LinearLayout.LayoutParams parametrosImagenMecanico = new LinearLayout.LayoutParams((int)Math.round(anchoImagenMecanico), LayoutParams.WRAP_CONTENT);
			parametrosImagenMecanico.setMargins((int)margenIzquierdaImagenMecanico, (int)margenArribaImagenMecanico, 0, (int)margenArribaTexto);
			imagenMecanico2.setLayoutParams(parametrosImagenMecanico);
			layout.addView(imagenMecanico2);
			
			TextView nombreMecanico2  = new TextView(this);
			LinearLayout.LayoutParams parametrosTextView = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			parametrosTextView.setMargins((int)margenIzquierdaImagenMecanico, 0, 0, 0);
			nombreMecanico2.setLayoutParams(parametrosTextView);
			nombreMecanico2.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_MEDELLIN2));
			layout.addView(nombreMecanico2);
			
			TextView mailMecanico2 = new TextView(this);
			mailMecanico2.setLayoutParams(parametrosTextView);
			mailMecanico2.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_MEDELLIN2));
			layout.addView(mailMecanico2);
			
			TextView celularMecanico2 = new TextView(this);
			celularMecanico2.setLayoutParams(parametrosTextView);
			celularMecanico2.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_MEDELLIN2));
			layout.addView(celularMecanico2);
			
			
			
			
			
			break;
		case 3:
			tituloCiudad.setText("PEREIRA.");
			
			imagenPreliminar = BitmapFactory.decodeResource(res, R.drawable.imagen_vitrina_ciudad_pereira);
			imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*0.71), (int) anchoImagen);
			imagenConsecionario.setImageBitmap(imagenFinal);
			
			direccionCiudad.setText(R.string.TAG_VITRINAS_DIRECCION_PEREIRA);
			telefonoCiudad.setText(getString(R.string.TAG_VITRINAS_TELEFONO_CONSTANTE) +ESPACIO + getString(R.string.TAG_VITRINAS_TELEFONO_PEREIRA));
			
			imagenPreliminarMecanico = BitmapFactory.decodeResource(res, R.drawable.vitrinas_imagen_mecanico_pereira);
			imagenFinalMecanico = Resize.resizeBitmap(imagenPreliminarMecanico, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico.setImageBitmap(imagenFinalMecanico);
			
			nombreMecanico.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_PEREIRA));
			mailMecanico.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_PEREIRA));
			celularMecanico.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_PEREIRA));
			break;
		case 4:
			tituloCiudad.setText("BOGOTA.");
			
			imagenPreliminar = BitmapFactory.decodeResource(res, R.drawable.imagen_vitrina_ciudad_bogota);
			imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*0.71), (int) anchoImagen);
			imagenConsecionario.setImageBitmap(imagenFinal);
			
			direccionCiudad.setText(R.string.TAG_VITRINAS_DIRECCION_BOGOTA);
			telefonoCiudad.setText(getString(R.string.TAG_VITRINAS_TELEFONO_CONSTANTE) +ESPACIO + getString(R.string.TAG_VITRINAS_TELEFONO_BOGOTA));
			
			imagenPreliminarMecanico = BitmapFactory.decodeResource(res, R.drawable.vitrinas_imagen_mecanico_bogota);
			imagenFinalMecanico = Resize.resizeBitmap(imagenPreliminarMecanico, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico.setImageBitmap(imagenFinalMecanico);
			
			nombreMecanico.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_BOGOTA));
			mailMecanico.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_BOGOTA));
			celularMecanico.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_BOGOTA));
			break;
		case 5:
			tituloCiudad.setText("CALI.");
			
			imagenPreliminar = BitmapFactory.decodeResource(res, R.drawable.imagen_vitrina_ciudad_cali);
			imagenFinal = Resize.resizeBitmap(imagenPreliminar, (int)Math.round(anchoImagen*0.71), (int) anchoImagen);
			imagenConsecionario.setImageBitmap(imagenFinal);
			
			direccionCiudad.setText(R.string.TAG_VITRINAS_DIRECCION_CALI);
			telefonoCiudad.setText(getString(R.string.TAG_VITRINAS_TELEFONO_CONSTANTE) +ESPACIO+ getString(R.string.TAG_VITRINAS_TELEFONO_CALI));
			
			imagenPreliminarMecanico = BitmapFactory.decodeResource(res, R.drawable.vitrinas_imagen_mecanico_cali);
			imagenFinalMecanico = Resize.resizeBitmap(imagenPreliminarMecanico, (int)Math.round(anchoImagenMecanico*0.68), (int) anchoImagenMecanico);
			imagenMecanico.setImageBitmap(imagenFinalMecanico);
			
			nombreMecanico.setText(getString(R.string.TAG_VITRINAS_NOMBRE_MECANICO_CALI));
			mailMecanico.setText(getString(R.string.TAG_VITRINAS_MAIL_MECANICO_CALI));
			celularMecanico.setText(getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CONSTANTE) + ESPACIO + getString(R.string.TAG_VITRINAS_CELULAR_MECANICO_CALI));
			break;
		}
		tituloCiudad.setTypeface(tipoMini);
	}
	
	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}
}
 