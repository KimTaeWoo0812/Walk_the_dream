package com.ssm.walkthedream;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class tmp1 extends FragmentActivity implements OnInitListener{
	
	private static double lat;
	private static double lon;
	LatLng startingPoint;
	private double arr[][]=new double[502][2];
	private int count=0;
	GoogleMap gmap;
	public void onCreate(Bundle savedInstanceState) {         
		super.onCreate(savedInstanceState);         

        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tmp1_map); 
		gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		startingPoint = new LatLng(AndroidSocket.latitude, AndroidSocket.longitude );

		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,16));
		
		gmap.addMarker(new MarkerOptions().position(new LatLng(AndroidSocket.latitude, AndroidSocket.longitude)));
		lat = AndroidSocket.latitude;
		lon = AndroidSocket.longitude;
		arr[count][0]=lat;
		arr[count][1]=lon;
		count++;
		} 
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}
	
	public void onClick(View view) {//��ư ��������
        switch (view.getId()) {
            case R.id.BtnUp://��
            	lat+=0.005;
            	if(AddArr()){
            		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            		startingPoint = new LatLng(lat, lon);
            		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,18));
            	}
            	break;
            case R.id.BtnDo://�Ʒ�
            	lat-=0.005;
            	if(AddArr()){
            		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            		startingPoint = new LatLng(lat, lon);
            		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,18));
            	}
            	break;
            case R.id.BtnL://��
            	lon+=0.005;
            	if(AddArr()){
            		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            		startingPoint = new LatLng(lat, lon);
            		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,18));
            	}
            	break;
            case R.id.BtnR://��0.002
            	lon-=0.005;
            	if(AddArr()){
            		gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            		startingPoint = new LatLng(lat, lon);
            		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,18));
            	}
            	break;
        }
	}
	public boolean AddArr()
	{
		if(count<501)
		{
			arr[count][0]=lat;
			arr[count][1]=lon;
			count++;
			return true;
		}
		Toast.makeText(this,"�ִ� 500������ ���� �����մϴ�!", Toast.LENGTH_SHORT).show();
		return false;
	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
}
//37.56647 126.97796 