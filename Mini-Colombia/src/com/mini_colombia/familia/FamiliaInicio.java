package com.mini_colombia.familia;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini_colombia.R;
import com.mini_colombia.parser.Parser;
import com.mini_colombia.servicios.Resize;

public class FamiliaInicio  extends ActivityGroup 
{
	private static final String MODELO = "modelo";

	public static FamiliaInicio grupoFamilia;

	public ArrayList<View> historialViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia_inicio);
		this.grupoFamilia = this;
		this.historialViews = new ArrayList<View>();


		//Creacion de las fuentes
		Typeface tipoMini = Typeface.createFromAsset(getAssets(), "fonts/mibd.ttf");
		TextView textoTitulo = (TextView)findViewById(R.id.TextViewInicioFamilia);
		textoTitulo.setTypeface(tipoMini);
		textoTitulo.setText(R.string.Mini);


		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutFamilia);

		AssetManager am = getAssets();
		Resources res = getResources();
		float anchoImagenThumbnail = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,296 , res.getDisplayMetrics());
		try 
		{
			InputStream is = am.open("familia/modelos.xml");
			Parser parser = new Parser();
			JSONObject objeto = parser.getJsonFromInputStream(is);
			try 
			{
				JSONArray modelos = objeto.getJSONArray(getString(R.string.TAG_MODELO));
				for(int i=0; i<modelos.length(); i++)
				{

					//Reemplazar las 2 filas de codigo siguientes
					final int j = i;
					JSONObject modelo = modelos.getJSONObject(i);
					ImageButton ib = new ImageButton(this);
					LayoutParams parametros = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					String thumbnail = modelo.getString(getString(R.string.TAG_THUMBNAIL_MODELO));
					int  idImagenThumbnail = getResources().getIdentifier(thumbnail, "drawable", getApplicationContext().getPackageName());
					Bitmap thumbnailPreliminar = BitmapFactory.decodeResource(res, idImagenThumbnail);
					Bitmap thumbnailFinal = Resize.resizeBitmap(thumbnailPreliminar, (int) Math.round(anchoImagenThumbnail*0.43), (int) anchoImagenThumbnail);
					ib.setImageBitmap(thumbnailFinal);
					ib.setLayoutParams(parametros);
//					ib.setImageResource(getResources().getIdentifier(thumbnail,"drawable",getApplicationContext().getPackageName()));
					ib.setBackgroundColor(Color.TRANSPARENT);
					ib.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) 
						{
							Intent i = new Intent(FamiliaInicio.this, FamiliaModelos.class);
							i.putExtra(MODELO, j);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							View v1 = getLocalActivityManager().startActivity("", i).getDecorView();
							reemplazarView(v1);

						}
					});
					layout.addView(ib);
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}





	@Override
	public void onBackPressed() 
	{
		FamiliaInicio.grupoFamilia.back();
		return;
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






}
