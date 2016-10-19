package com.ssm.walkthedream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


class MyLocationListener implements LocationListener {
	private AndroidSocket soccket ;

    LocationManager manager;
    Location loc;
    
    boolean useChanger;
	
    Context mContext;
    
    public void endGps()
    {
    	manager.removeUpdates(this);
    }
    
    public MyLocationListener(Context c) {
    	useChanger=false;
		mContext = c;
		
		setOption();
	}
    
    /**
     * �浵���� �����ɴϴ�. 
     * */
    void setOption()
    {
    	
    	soccket=AndroidSocket.shared();
    	
		if (manager == null) {
            manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        // provider ������||GPS �� ���ؼ� �������� �˷��ִ� Stirng ����
        // minTime �ּ��� �󸶸��� �ð��� �帥�� ��ġ������ �������� �ð������� ���� �����ϴ� ����
        // minDistance �󸶸��� �Ÿ��� �������� ��ġ������ �������� �����ϴ� ����
        // manager.requestLocationUpdates(provider, minTime, minDistance, listener);
        // 10��
        long minTime = 2000;

        // �Ÿ��� 0���� ����
        // �׷��� �ð��� �Ÿ� ������ ���� ���������ʰ� 10�ʵڿ� �ٽ� ��ġ������ �޴´�
        float minDistance = 0;

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
		
        
    }
    
	public MyLocationListener(Context c, onChangedXYListener onChangedXYListener_) {

		useChanger=true;
		mContext = c;
		 
		setOnChangedXYListener(onChangedXYListener_);
		
		setOption();
		
	}
	
    // ��ġ������ �Ʒ� �޼��带 ���ؼ� ���޵ȴ�.
    @Override
    public void onLocationChanged(Location location) {

//        appendText("onLocationChanged()�� ȣ��Ǿ����ϴ�");

    	
    	
        double latitude = soccket.latitude = location.getLatitude();
        double longitude = soccket.longitude = location.getLongitude(); 
        
        SC.lat_=location.getLatitude();
        SC.lon_=location.getLongitude(); 
        if(useChanger==true)
        	OnChangeXYListener.onChangedXY(latitude,longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    onChangedXYListener OnChangeXYListener;
   
    public void setOnChangedXYListener(onChangedXYListener OnChangeXYListener_)
    {
    	OnChangeXYListener = OnChangeXYListener_;
    }
    
    interface onChangedXYListener{
    	
    	void onChangedXY(double lat_, double lon_);
    	
    }
    /**
     * GPS ������ �������� �������� 
     * ���������� ���� ����� alert â
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        alertDialog.setTitle("GPS �����������");
        alertDialog.setMessage("GPS ������ ���� ���ֽ��ϴ�.\n ����â���� ���ðڽ��ϱ�?");
   
        // OK �� ������ �Ǹ� ����â���� �̵��մϴ�. 
        alertDialog.setPositiveButton("����", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle �ϸ� ���� �մϴ�. 
        alertDialog.setNegativeButton("���", 
                              new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        alertDialog.show();
    }
}