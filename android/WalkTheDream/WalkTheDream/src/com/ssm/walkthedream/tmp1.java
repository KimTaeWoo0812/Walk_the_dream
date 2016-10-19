package com.ssm.walkthedream;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class tmp1 extends FragmentActivity {

	private static double lat;
	private static double lon;
	LatLng startingPoint;
	GoogleMap gmap;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SC.nCount = 0;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tmp2_map);

		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		startingPoint = new LatLng(lat, lon);

		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));

		//gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
		// lat = AndroidSocket.latitude;
		// lon = AndroidSocket.longitude;

		gmap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				lat = latLng.latitude;
				lon = latLng.longitude;
				if (AddArr()) {
					Toast.makeText(tmp1.this, "위도  : " + latLng.latitude +  " 경도: "  + latLng.longitude ,  Toast.LENGTH_SHORT).show();
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon01));
					markerOptions.position(latLng);
					// gmap.clear();
					gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
					gmap.addMarker(markerOptions);
				}
			}
		});
		gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.BtnUp:// 위
			lat += 0.0001;
			if (AddArr()) {
				gmap.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lon)));
				startingPoint = new LatLng(lat, lon);
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						startingPoint, 19));
			}
			break;
		case R.id.BtnDo:// 아래
			lat -= 0.0001;
			if (AddArr()) {
				gmap.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lon)));
				startingPoint = new LatLng(lat, lon);
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						startingPoint, 19));
			}
			break;
		case R.id.BtnL:// 좌
			lon -= 0.0001;
			if (AddArr()) {
				gmap.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lon)));
				startingPoint = new LatLng(lat, lon);
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						startingPoint, 19));
			}
			break;
		case R.id.BtnR:// 우0.002
			lon += 0.0001;
			if (AddArr()) {
				gmap.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lon)));
				startingPoint = new LatLng(lat, lon);
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						startingPoint, 19));
			}
			break;

		// case R.id.BtnCen://삭제
		// count--;
		// gmap.clear();
		// for(int i=0;i<count;i++)
		// {
		// gmap.addMarker(new MarkerOptions().position(new LatLng(arr[i][0],
		// arr[i][1])));
		// }
		// break;
		case R.id.BtnGo:// 안내 시작
			Intent intent5 = new Intent(this, tmp2.class);
			startActivity(intent5);
			break;
		}
	}

	public boolean AddArr() {
		if (SC.nCount < 201) {
			SC.nArr[SC.nCount][0] = lat;
			SC.nArr[SC.nCount][1] = lon;
			SC.nCount++;
			return true;
		} else
			Toast.makeText(this, "최대 이백개까지 저장 가능합니다!", Toast.LENGTH_SHORT)
					.show();
		return false;
	}

}
// 37.56647 126.97796 