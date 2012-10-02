package com.mini_colombia.familia;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.Resize;
import com.mini_colombia.values.Edicion;

public class FamiliaModelos extends Activity 
{
	private static final String MODELO = "modelo";

	private static final String EDICION = "edicion";

	//Numero del modelo que viene del activity anterior
	private int numModelo;

	private String nombreModelo;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_modelos);

		numModelo = getIntent().getIntExtra(MODELO,-1);

		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");

		AssetManager am = getAssets();

		Resources res = getResources();
		//Si quiero que llegue hasta abajo cambio 148 por 225
		float anchoImagenIzquierda = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,194 , res.getDisplayMetrics());
		float anchoImagenThumbnail = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, res.getDisplayMetrics());
		float altoImagenThumbnail = (float)(anchoImagenIzquierda*1.6)/3;

		InputStream is;
		try 
		{
			is = am.open("familia/modelos.xml");
			Parser parser = new Parser();
			JSONObject objeto = parser.getJsonFromInputStream(is);
			JSONArray modelos = objeto.getJSONArray(getString(R.string.TAG_MODELO));
			JSONObject modelo = modelos.getJSONObject(numModelo);

			final String nombre_modelo = modelo.getString(getString(R.string.TAG_NOMBRE_MODELO));
			String imagen_modelo = modelo.getString(getString(R.string.TAG_IMAGEN_MODELO));


			//Creacion del titulo
			TextView textoTitulo = (TextView)findViewById(R.id.TituloModelo);
			textoTitulo.setTypeface(tipoMini);
			textoTitulo.setText(nombre_modelo);

			//Creacion de la imagen de la izquierda
			//			ImageView iv = new ImageView(this);
			//			HorizontalScrollView horScroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollModelos);
			//			horScroll.setHorizontalFadingEdgeEnabled(false);
			//			horScroll.setHorizontalScrollBarEnabled(false);
			//			LayoutParams parametros = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//			int  idImagenIzquierda = getResources().getIdentifier(imagen_modelo, "drawable", getApplicationContext().getPackageName());
			//			Bitmap imagenIzquierdaPreliminar = BitmapFactory.decodeResource(res,idImagenIzquierda);
			//			Bitmap imagenIzquierdaFinal = Resize.resizeBitmap(imagenIzquierdaPreliminar, (int) Math.round(anchoImagenIzquierda*1.6), (int) anchoImagenIzquierda);
			//			iv.setImageBitmap(imagenIzquierdaFinal);
			//			iv.setBackgroundColor(color.transparent);
			//			iv.setLayoutParams(parametros);
			//			horScroll.addView(iv);




			//Creacion de los thumbnail de la derecha
			LinearLayout layout = (LinearLayout) findViewById(R.id.layoutDerModelos);
			ImageButton ib;
			JSONArray ediciones = modelo.getJSONArray(getString(R.string.TAG_EDICIONES));
			for(int i=0;i<ediciones.length();i++)
			{

				final int j=i;
				JSONObject edicion = ediciones.getJSONObject(i);
				String nombre_edicion = edicion.getString(getString(R.string.TAG_NOMBRE_EDICION));
				String imagen_edicion = edicion.getString(getString(R.string.TAG_IMAGEN_EDICION));
				String imagen_thumbnail_edicion = edicion.getString(getString(R.string.TAG_IMAGEN_THUMBNAIL_EDICION));
				boolean test_drive_edicion = Boolean.parseBoolean(edicion.getString(getString(R.string.TAG_TEST_DRIVE_EDICION)));
				String templateColor = edicion.getString(getString(R.string.TAG_TEMPLATE_EDICION));
				String descripcion_edicion = edicion.getString(getString(R.string.TAG_DESCRIPCION_EDICION));

				final Edicion e = new Edicion(imagen_edicion, nombre_edicion, descripcion_edicion, imagen_thumbnail_edicion, test_drive_edicion, templateColor);


				if(i==0)
				{
					ib = new ImageButton(this);
					HorizontalScrollView horScroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollModelos);
					horScroll.setHorizontalFadingEdgeEnabled(false);
					horScroll.setHorizontalScrollBarEnabled(false);
					LayoutParams parametros = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					int  idImagenIzquierda = getResources().getIdentifier(imagen_thumbnail_edicion, "drawable", getApplicationContext().getPackageName());
					Bitmap imagenIzquierdaPreliminar = BitmapFactory.decodeResource(res,idImagenIzquierda);
					Bitmap imagenIzquierdaFinal = Resize.resizeBitmap(imagenIzquierdaPreliminar, (int) Math.round(anchoImagenIzquierda*1.6), (int) anchoImagenIzquierda);
					ib.setImageBitmap(imagenIzquierdaFinal);
					ib.setBackgroundColor(color.transparent);
					ib.setLayoutParams(parametros);
					ib.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) 
						{
							Intent i = new Intent(FamiliaModelos.this, FamiliaEdicion.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("objeto", e);
							i.putExtras(bundle);
							i.putExtra("MODELO", nombre_modelo);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							View v1 = FamiliaInicio.grupoFamilia.getLocalActivityManager().startActivity("", i).getDecorView();
							FamiliaInicio actividadPadre =(FamiliaInicio) getParent(); 
							actividadPadre.reemplazarView(v1);	

						}
					});
					horScroll.addView(ib);
				}
				else
				{
					ib = new ImageButton(this);
					LayoutParams parametrosThumb = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					int idThumbnial = getResources().getIdentifier(imagen_thumbnail_edicion, "drawable", getApplicationContext().getPackageName());
					Bitmap thumbnailPreliminar = BitmapFactory.decodeResource(res, idThumbnial);
					Bitmap thumbnailFinal = Resize.resizeBitmap(thumbnailPreliminar, (int)altoImagenThumbnail, (int)Math.round(altoImagenThumbnail*1.2));
					ib.setImageBitmap(thumbnailFinal);
					ib.setPadding(0, 0, 0, 0);
					ib.setBackgroundColor(color.transparent);
					//ib.setBackgroundResource(getResources().getIdentifier(imagen_thumbnail_edicion,"drawable", getApplicationContext().getPackageName()));
					ib.setLayoutParams(parametrosThumb);
					ib.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) 
						{
							Intent i = new Intent(FamiliaModelos.this, FamiliaEdicion.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("objeto", e);
							i.putExtras(bundle);
							i.putExtra("MODELO", nombre_modelo);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							View v1 = FamiliaInicio.grupoFamilia.getLocalActivityManager().startActivity("", i).getDecorView();
							FamiliaInicio actividadPadre =(FamiliaInicio) getParent(); 
							actividadPadre.reemplazarView(v1);	
						}
					});
					layout.addView(ib);
				}


			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//		try 
		//		{
		//			List<Modelo> modelos = daoModelo.queryForAll();
		//			Modelo m = modelos.get(Integer.parseInt(modelo));
		//			nombreModelo=m.getNombre();
		//
		//			//Creacion del titulo
		//			TextView textoTitulo = (TextView)findViewById(R.id.TituloModelo);
		//			textoTitulo.setTypeface(tipoMini);
		//			textoTitulo.setText(m.getNombre());
		//
		//			ImageView iv = new ImageView(this);
		//			HorizontalScrollView horScroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollModelos);
		//			horScroll.setHorizontalFadingEdgeEnabled(false);
		//			horScroll.setHorizontalScrollBarEnabled(false);
		//
		//			//Seccion Carga Imagen
		//			String nombreImagen = m.getImagen();
		//			Bitmap bitmap = ObtenerImagen.darImagen(nombreImagen, getApplicationContext());
		//			//Bitmap bitmapEscala= Resize.resizeBitmap(bitmap, 600,899 );
		//			iv.setImageBitmap(bitmap);
		//			horScroll.addView(iv);
		//
		//
		//			//Metodos para hacer la consulta de las ediciones asociadas al modelos actual
		//			QueryBuilder<Edicion,String> queryBuilder = daoEdicion.queryBuilder();
		//			Where<Edicion, String> where = queryBuilder.where();
		//			SelectArg argumentosSeleccion = new SelectArg();
		//			where.eq("modelo_id", argumentosSeleccion);
		//			PreparedQuery<Edicion> query = queryBuilder.prepare();
		//			argumentosSeleccion.setValue(m);
		//			List<Edicion> ediciones = daoEdicion.query(query);
		//
		//			LinearLayout layout = (LinearLayout) findViewById(R.id.layoutDerModelos);
		//
		//			ImageButton ib;
		//			for(int i = 0; i<ediciones.size(); i++)
		//			{
		//				ib = new ImageButton(this);
		//				Edicion e = ediciones.get(i);
		//				String nombreImagenThumbnail = e.getThumbnail();
		//
		//
		//				bitmap = ObtenerImagen.darImagen(nombreImagenThumbnail, getApplicationContext());
		//				Bitmap bitmapEscala= Resize.resizeBitmap(bitmap, 147, 220);
		//				ib.setImageBitmap(bitmapEscala);
		//				ib.setBackgroundColor(Color.BLACK);
		//				ib.setOnClickListener(this);
		//				ib.setId(i);
		//
		//				layout.addView(ib);
		//			}
		//
		//
		//
		//
		//
		//		} 
		//		catch (SQLException e) 
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} 










	}


	///////////////////////////////
	//Metodos de la actividad
	///////////////////////////////

	@Override
	public void onBackPressed() 
	{
		getParent().onBackPressed();
	}




}
