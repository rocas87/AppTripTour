package com.example.triptour;

import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorRoute extends ArrayAdapter {

	Activity context;
	ArrayList<String> nombre = new ArrayList<String>();
	ArrayList<String> promedio = new ArrayList<String>();
	ArrayList<String> distancia = new ArrayList<String>();
	ArrayList<String> duracion = new ArrayList<String>();
	String nom, prom, dist, dur;
	TextView name, average, distance, duration;
	ImageView imagen, est1, est2, est3, est4, est5;	
	
	//@SuppressWarnings("unchecked")
	public AdaptadorRoute(Activity context, ArrayList<String> nombre, ArrayList<String> promedio, ArrayList<String> distancia,
			ArrayList<String> duracion) {
		super(context, R.layout.adaptador_route, nombre);
		// TODO Auto-generated constructor stub
		this.nombre = nombre;
		this.promedio = promedio;
		this.distancia = distancia;
		this.duracion = duracion;
		this.context = context;
	}


	public View getView(int position, View convertView, ViewGroup paretn)
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.adaptador_route, null);
		
		nom = nombre.get(position).toString();
		prom = promedio.get(position).toString();
		dist = distancia.get(position).toString();
		dur = duracion.get(position).toString();
		
		name = (TextView)item.findViewById(R.id.nombre);
		name.setText(nom);
						
		distance = (TextView)item.findViewById(R.id.distance);
		distance.setText(dist);
		
		duration = (TextView)item.findViewById(R.id.duration);
		duration.setText(dur);
		
		est1 = (ImageView)item.findViewById(R.id.est1);
		est2 = (ImageView)item.findViewById(R.id.est2);
		est3 = (ImageView)item.findViewById(R.id.est3);
		est4 = (ImageView)item.findViewById(R.id.est4);
		est5 = (ImageView)item.findViewById(R.id.est5);
		
		if(prom.equals("4"))
		{
			est5.setImageURI(Uri.parse("drawable/preview.jpg"));
		}
		else if(prom.equals("3"))
		{
			est4.setImageURI(Uri.parse("drawable/preview.jpg"));
			est5.setImageURI(Uri.parse("drawable/preview.jpg"));
		}
		else if(prom.equals("2"))
		{
			est3.setImageURI(Uri.parse("drawable/preview.jpg"));
			est4.setImageURI(Uri.parse("drawable/preview.jpg"));
			est5.setImageURI(Uri.parse("drawable/preview.jpg"));
		}
		else if(prom.equals("1"))
		{
			est2.setImageURI(Uri.parse("drawable/preview.jpg"));
			est3.setImageURI(Uri.parse("drawable/preview.jpg"));
			est4.setImageURI(Uri.parse("drawable/preview.jpg"));
			est5.setImageURI(Uri.parse("drawable/preview.jpg"));
		}
		return item;
	}
}
