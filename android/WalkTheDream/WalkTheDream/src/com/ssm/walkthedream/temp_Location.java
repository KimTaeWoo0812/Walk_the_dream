package com.ssm.walkthedream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class temp_Location extends FragmentActivity implements LocationListener{
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmp2_map);
 
        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(temp_Location.this);
        if( googlePlayServiceResult !=   ConnectionResult.SUCCESS){ //구글 플레이 서비스를 활용하지 못할경우 <계정이 연결이 안되어 있는 경우
            //실패
            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    finish();
                }
            }).show();
        }else { //구글 플레이가 활성화 된 경우
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
 
            if (provider == null) {  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
                new AlertDialog.Builder(temp_Location.this)
                        .setTitle("위치서비스 동의")
                        .setNeutralButton("이동", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                        .show();
            } else {   //위치 정보 설정이 되어 있으면 현재위치를 받아옵니다
 
                locationManager.requestLocationUpdates(provider, 1, 1, temp_Location.this); //기본 위치 값 설정
                setUpMapIfNeeded(); //Map ReDraw
            }
 
            setMyLocation(); //내위치 정하는 함수
        }
    }//onCreate
 
 
    private LatLng myLocation;
    double[] myGps;
 
    private void setMyLocation(){
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
 
    }
    Marker mMarker;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = mMap.addMarker(new MarkerOptions().position(loc));
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, true);
                if(provider==null){//사용자가 위치설정동의 안했을때 종료
                    finish();
                }else{//사용자가 위치설정 동의 했을때
                    locationManager.requestLocationUpdates(provider, 1L, 2F, temp_Location.this);
                    Log.d("KTH","117 locationMaanger done");
                    setUpMapIfNeeded();
                }
                break;
        }
    }
 
    @Override
    public void onBackPressed() {
        this.finish();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
 
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
 
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
 
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
 
    }
 
    boolean locationTag=true;
 
    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();
            double lng = location.getLongitude();
 
            Toast.makeText(temp_Location.this, "위도  : " + lat +  " 경도: "  + lng ,  Toast.LENGTH_SHORT).show();
            locationTag=false;
        }
 
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
}