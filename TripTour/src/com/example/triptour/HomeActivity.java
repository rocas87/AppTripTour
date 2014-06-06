package com.example.triptour;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeActivity extends android.support.v4.app.FragmentActivity 
implements android.location.LocationListener, OnClickListener
{
	String user;
	int categoria;
	Location loc;
	LocationClient mLocationClient;
	LocationManager handle;
	private String provider;
	ProgressDialog pd;
	GoogleMap mapa;
	LatLng MiUbicacion;
	ArrayAdapter<String> adaptadorCategoria;
	private Spinner spCategoria;
	private List<String> categorias = new ArrayList<String>();
	LayoutInflater liFind;
	View promptFind;
	
	// TODO Auto-generated method stub
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
			
		Bundle home = getIntent().getExtras();
		user = home.getString("usr_nick");
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		//Boton para sentra ubicaion en el mapa
		mapa.setMyLocationEnabled(true);
		
		centrarMapa();
		
		categorias.add("Bar");
		categorias.add("Zoologico");
		categorias.add("Museo");
		categorias.add("Parques");
		categorias.add("Parque de diversiones");
		categorias.add("Deportes");
		categorias.add("Restorant");
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.find:	    		
	        	pomptFind();
	            return true;
	            
	        case R.id.recomendation:
	        	Intent recomendation = new Intent(this,RecomendationActivity.class);
				recomendation.putExtra("user", user);
				startActivity(recomendation);
	            return true;
	            
	        case R.id.recomendation_route:
				Intent recomendationRoute = new Intent(this,RecomendationRouteActivity.class);
				recomendationRoute.putExtra("user", user);
				startActivity(recomendationRoute);
				return true;

	        case R.id.evaluacion:
	        	return true;

	        case R.id.alojamiento:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.booking.com/"));
				startActivity(intent);
				return true;

	        case R.id.clima:
	        	pd = new ProgressDialog(this);
	        	pd.setMessage("Buscando ubicacion para pronostico por zona....");
				pd.show();
	        	loc = getMiUbicacion();
				Climate cl = new Climate();
				pd.dismiss();
				Intent clima = new Intent(Intent.ACTION_VIEW);
				clima.setData(Uri.parse(cl.climaURL(String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()))));
				startActivity(clima);
				return true;

	        case R.id.cambio_moneda:
				Intent changeMoney = new Intent(this,ChangeMoneyActivity.class);
				startActivity(changeMoney);
				return true;

			case R.id.preferencias:
				return true;

	    }
	    return false;
	}

	private void pomptFind() 
	{
		// TODO Auto-generated method stub
		liFind = LayoutInflater.from(this);
		promptFind = liFind.inflate(R.layout.prompt_find_activity, null);

		spCategoria = (Spinner)findViewById(R.id.spCategoria);
		spCategoria = (Spinner)promptFind.findViewById(R.id.spCategoria);
		adaptadorCategoria = new ArrayAdapter<String>
			(this,android.R.layout.simple_spinner_item, categorias);
		adaptadorCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCategoria.setAdapter(adaptadorCategoria);
		
		spCategoria.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				categoria = arg2+1;
				Log.e("token", String.valueOf(categoria));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptFind);
		
		// Mostramos el mensaje del cuadro de dialogo
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int id) {
		// Rescatamos el nombre del EditText y lo mostramos por pantalla
			find();
		}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int id) {
		// Cancelamos el cuadro de dialogo
		dialog.cancel();
		}
		});
		// Creamos un AlertDialog y lo mostramos
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
	}

	public void find()
	{
		Intent find = new Intent(this,FindActivity.class);
		find.putExtra("user", user);
		find.putExtra("categoria", String.valueOf(categoria));
		startActivity(find);
	}
	public void centrarMapa()
	{
		//Llamo al servico de localizacion	        
	    handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    //Clase criteria permite decidir mejor poveedor de posicion
	    Criteria c = new Criteria();
	    //obtiene el mejor proveedor en funci�n del criterio asignado
	    //ACCURACY_FINE(La mejor presicion)--ACCURACY_COARSE(PRESISION MEDIA)
	    c.setAccuracy(Criteria.ACCURACY_COARSE);
	    //Indica si es necesaria la altura por parte del proveedor
	    c.setAltitudeRequired(false);
	    provider = handle.getBestProvider(c, true);
	    //Se activan las notificaciones de localizaci�n con los par�metros: 
	    //proveedor, tiempo m�nimo de actualizaci�n, distancia m�nima, Locationlistener
	    handle.requestLocationUpdates(provider, 60000, 5, this);
	    //Obtiene la ultima posicion conocida por el proveedor
	    loc = handle.getLastKnownLocation(provider);
	    MiUbicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
		configGPS(MiUbicacion);
	}
	
	private void configGPS(LatLng latlong) 
	{
		// TODO Auto-generated method stub
		//Se indica el latitud y longitud y zoom
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latlong).zoom(12).build();	
		//Se mueve la camara a lo indicado anteriormente
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	public Location getMiUbicacion()
	{
		//Llamo al servico de localizacion	        
	    handle = (LocationManager)getSystemService(LOCATION_SERVICE);
	    //Clase criteria permite decidir mejor poveedor de posicion
	    Criteria c = new Criteria();
	    //obtiene el mejor proveedor en funci�n del criterio asignado
	    //ACCURACY_FINE(La mejor presicion)--ACCURACY_COARSE(PRESISION MEDIA)
	    c.setAccuracy(Criteria.ACCURACY_COARSE);
	    //Indica si es necesaria la altura por parte del proveedor
	    c.setAltitudeRequired(false);
	    provider = handle.getBestProvider(c, true);
	    //Se activan las notificaciones de localizaci�n con los par�metros: 
	    //proveedor, tiempo m�nimo de actualizaci�n, distancia m�nima, Locationlistener
	    handle.requestLocationUpdates(provider, 60000, 5,this);
	    //Obtiene la ultima posicion conocida por el proveedor
	    loc = handle.getLastKnownLocation(provider);
	  
	    return loc;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
