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
	        
	        
	        // GPS ������ �����ֱ� ���� �̺�Ʈ Ŭ���� ���
	        btnShowLocation.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View arg0) {
	                
	                // GPS ������� ��������
	                    double latitude = AndroidSocket.latitude;
	                    double longitude = AndroidSocket.longitude;
	                     
	                    txtLat.setText(String.valueOf(latitude));
	                    txtLon.setText(String.valueOf(longitude));
	                     
	                    Toast.makeText(
	                            getApplicationContext(),
	                            "����� ��ġ - \n����: " + latitude + "\n�浵: " + longitude,
	                            Toast.LENGTH_LONG).show();
	            }
	        });
	    }
	}


