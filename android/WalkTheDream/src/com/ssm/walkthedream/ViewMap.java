package com.ssm.walkthedream;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
	

public class ViewMap extends FragmentActivity{
	private double lat;
	private double lon;
	public void onCreate(Bundle savedInstanceState) {         
		super.onCreate(savedInstanceState);         

        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_map); 
		GoogleMap gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		lat=AndroidSocket.latitude;
		lon=AndroidSocket.longitude;
		LatLng startingPoint = new LatLng(lat, lon);

		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,16));
		
		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
		} 
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}
}
//37.56647 126.97796 