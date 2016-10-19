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
        if( googlePlayServiceResult !=   ConnectionResult.SUCCESS){ //���� �÷��� ���񽺸� Ȱ������ ���Ұ�� <������ ������ �ȵǾ� �ִ� ���
            //����
            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    finish();
                }
            }).show();
        }else { //���� �÷��̰� Ȱ��ȭ �� ���
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
 
            if (provider == null) {  //��ġ���� ������ �ȵǾ� ������ �����ϴ� ��Ƽ��Ƽ�� �̵��մϴ�
                new AlertDialog.Builder(temp_Location.this)
                        .setTitle("��ġ���� ����")
                        .setNeutralButton("�̵�", new DialogInterface.OnClickListener() {
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
            } else {   //��ġ ���� ������ �Ǿ� ������ ������ġ�� �޾ƿɴϴ�
 
                locationManager.requestLocationUpdates(provider, 1, 1, temp_Location.this); //�⺻ ��ġ �� ����
                setUpMapIfNeeded(); //Map ReDraw
            }
 
            setMyLocation(); //����ġ ���ϴ� �Լ�
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//��ġ���� ��Ƽ��Ƽ ���� ��
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, true);
                if(provider==null){//����ڰ� ��ġ�������� �������� ����
                    finish();
                }else{//����ڰ� ��ġ���� ���� ������
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
        if(locationTag){//�ѹ��� ��ġ�� �������� ���ؼ� tag�� �־����ϴ�
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();
            double lng = location.getLongitude();
 
            Toast.makeText(temp_Location.this, "����  : " + lat +  " �浵: "  + lng ,  Toast.LENGTH_SHORT).show();
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