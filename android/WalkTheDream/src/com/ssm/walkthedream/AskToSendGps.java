package com.ssm.walkthedream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AskToSendGps extends Activity{
	 private Button btnShowLocation;
	    private TextView txtLat;
	    private TextView txtLon;
	     
	    // GPSTracker class
	    private MyLocationListener gps;
	 
	    
	    
	    @Override
		protected void onPause() {

	    	gps.endGps();
	    	super.onPause();
		}



		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.test_view);
	 
	        btnShowLocation = (Button) findViewById(R.id.btn_start);
	        txtLat = (TextView) findViewById(R.id.Latitude);
	        txtLon = (TextView) findViewById(R.id.Longitude);
	        gps = new MyLocationListener(AskToSendGps.this);
	        
	        
	        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
	        btnShowLocation.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View arg0) {
	                
	                // GPS 사용유무 가져오기
	                    double latitude = AndroidSocket.latitude;
	                    double longitude = AndroidSocket.longitude;
	                     
	                    txtLat.setText(String.valueOf(latitude));
	                    txtLon.setText(String.valueOf(longitude));
	                     
	                    Toast.makeText(
	                            getApplicationContext(),
	                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
	                            Toast.LENGTH_LONG).show();
	            }
	        });
	    }
	}


