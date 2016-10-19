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
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class tmp2 extends FragmentActivity implements OnInitListener, LocationListener {

	private static double lat;
	private static double lon;

	private volatile static double blat[] = new double[12];
	private volatile static double blon[] = new double[12];
	private volatile static int btop = 0;
	LatLng startingPoint;
	TextToSpeech tts;
	int temp = 0;
	GoogleMap gmap;
	
	    private LocationManager locationManager;
	    private String provider;
	
	private int check[] = new int[5];
	private double check2[] = new double[5];
	private int way;
	private static int count = 0, size;
	LatLng aaa;
	_Thread _Thread;
	double a=0;
	double b=0;
	String stemp="";
	EditText e;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tts = new TextToSpeech(this, this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tmp1_map);
		e= (EditText) findViewById(R.id.Text1);
		size = SC.nCount;

		// lat = SC.nArr[count][0];
		// lon = SC.nArr[count][1];
		int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(tmp2.this);
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
                new AlertDialog.Builder(tmp2.this)
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
 
                locationManager.requestLocationUpdates(provider, 1, 1, tmp2.this); //기본 위치 값 설정
                setUpMapIfNeeded(); //Map ReDraw
            }
 
            setMyLocation(); //내위치 정하는 함수
        }
        
        
        
		for (int i = 0; i < 5; i++)
			check[i] = 0;

		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		startingPoint = new LatLng(lat, lon);

		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));

		//gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
		
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_icon02));
		markerOptions.position(startingPoint);
		// gmap.clear();
		gmap.animateCamera(CameraUpdateFactory.newLatLng(startingPoint));
		gmap.addMarker(markerOptions);
		// lat = AndroidSocket.latitude;
		// lon = AndroidSocket.longitude;

		count = 0;
		btop = 0;

		int i;
		Marker mMarker;
		for (i = 0; i < size; i++) {
			/*
			LatLng latLng = new LatLng(SC.nArr[i][0], SC.nArr[i][1]);

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.map_icon02));
			markerOptions.position(latLng);
			// gmap.clear();
			gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			gmap.addMarker(markerOptions);
			*/
			
			LatLng loc = new LatLng(SC.nArr[i][0], SC.nArr[i][1]);
            mMarker = gmap.addMarker(new MarkerOptions().position(loc));
           

		}

	}
	private LatLng myLocation;
    double[] myGps;
 
    private void setMyLocation(){
        gmap.setOnMyLocationChangeListener(myLocationChangeListener);
 
    }//!@
    Marker mMarker;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.map_icon01));
			markerOptions.position(loc);
			// gmap.clear();
			gmap.animateCamera(CameraUpdateFactory.newLatLng(loc));
			gmap.addMarker(markerOptions);
			
            lat=loc.latitude;
            lon=loc.longitude;
            if(gmap != null){
            	gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.BtnUp:// 위
			 _Thread= new _Thread();
			 _Thread.start();
		
		case R.id.Btn2:
			
			Log.i("#dmdmdmmdmdmdm "+Double.toString(lat),Double.toString(lon));
			
			if(a!=lat||b!=lon)
			{
				Log.i("#dmdmdmmdmdmdm "+Double.toString(lat),Double.toString(lon));
				a=lat;
				b=lon;
				stemp+="\n"+Double.toString(lat)+"  "+Double.toString(lon);
				e.setText(stemp);
				
			}
			break;
		}
	}

	public class _Thread extends Thread {
		Handler handler;
		public _Thread() {
			handler = new Handler();
			
		}

		public void TTS_S(final String msg) {
			Log.i("#", msg);
			Log.i("#", msg);
			handler.post(new Runnable() {
				public void run() {
					Log.i("#", msg);
					tts.speak(msg, TextToSpeech.QUEUE_ADD, null);

				}
			});
		}
	
		

		public void run() {
			blat[(btop % 10)] = lat;
			blon[(btop % 10)] = lon;
			for (;;) {

				blat[(btop % 10)+1] = lat;
				blon[(btop % 10)+1] = lon;

				way = 5;
				TTS_S("처음 방향을 잡을때까지 이동 하세요");
				//tts.speak("처음 방향을 잡을때까지 이동 하세요.", TextToSpeech.QUEUE_FLUSH,
						//null);
				for (; count < size - 1;) {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (way == 5)
						TTS_S("방향을 잡는 중입니다.");
						//tts.speak("방향을 잡는 중입니다.", TextToSpeech.QUEUE_FLUSH,
							//	null);
					Log.i("#way",Integer.toString(way));
					Log.i("#lat",Double.toString(lat));
					Log.i("#lon",Double.toString(lon));
					Log.i("#SC.lat_", Double.toString(SC.lat_));
					Log.i("#SC.lon_", Double.toString(SC.lon_));
					Direction();
					// way=3;
					// lat=35.8702205;
					// lon = 128.6047965;
					Define();
					if (way == 0)
						nive1();
					else if (way == 1)
						nive2();
					else if (way == 2)
						nive3();
					else if (way == 3)
						nive4();

					// ViewMap(lat, lon);

					// lat - SC.nArr[count + 1][0] <
					if (lat - SC.nArr[count + 1][0] < 0.0002
							&& lat - SC.nArr[count + 1][0] > -0.0002
							&& lon - SC.nArr[count + 1][1] < 0.0002
							&& lon - SC.nArr[count + 1][0] > -0.0002) {
						TTS_S((count + 1) + "번째 분기에 도착했습니다.");
						count++;
					}
				}
				//tts.speak("목적지에 도작하였습니다", TextToSpeech.QUEUE_ADD, null);
				TTS_S("목적지에 도작하였습니다");
			}
		}
		
		
		public void Direction()// 방향
		{
			btop++;
			blat[(btop % 10)+1] = lat;
			blon[(btop % 10)+1] = lon;
			if (blat[(btop % 10)+1] != blat[(btop % 10)] || blon[(btop % 10)+1] != blon[(btop % 10)]) {

				if (blon[(btop % 10)] - blon[(btop % 10)+1] > 0)// 양수면 좌
				{
					Log.i("좌", "11");
					check[3] = 1;
					check2[3] = blon[(btop % 10)] - blon[(btop % 10)+1];
				}
				if (blon[(btop % 10)] - blon[(btop % 10)+1] < 0)// 우
				{
					Log.i("우", "11");
					check[2] = 1;
					check2[2] = blon[(btop % 10)] - blon[(btop % 10)+1];
				}
				if (blat[(btop % 10)] - blat[(btop % 10)+1] > 0)// 양수면 아래
				{
					Log.i("아래", "11");
					check[0] = 1;
					check2[0] = blat[(btop % 10)] - blat[(btop % 10)+1];
				}
				if (blat[(btop % 10)] - blat[(btop % 10)+1] < 0)// 위
				{
					Log.i("위", "11");
					check[1] = 1;
					check2[1] = blat[(btop % 10)] - blat[(btop % 10)+1];
				}

				int temp = 0;
				int i;
				for (i = 0; i < 4; i++)
					if (check[i] == 1)
						temp++;
				// if (temp == 0)// 다음께 그자리에 찍힐 경우.. 에러다
				// continue;
				if (temp == 1)// x든 y든 한쪽이다. 정확한 길안내 가능
				{
					Log.i("정확", "11");
					if (check[0] == 1)
						way = 0;
					if (check[1] == 1)
						way = 1;
					if (check[2] == 1)
						way = 2;
					if (check[3] == 1)// 0 위 1 아래 2 왼 3 오
						way = 3;
				}

				if (temp == 2)// 이게 문제다. gps가 튀어서 이상한 곳으로 잡았다 or 정확히 아래가 아니다. 이 경우
								// 크기 비교로 가능
				{
					Log.i("비정확", "11");
					if (check[0] == 1 && check[2] == 1)// 위와 왼쪽
					{
						Log.i("-위 왼-", "11");
						if (check2[0] + check2[2] > 0)// 위
							way = 2;
						else
							way = 0;
					}
					if (check[0] == 1 && check[3] == 1)// 위와 오른쪽
					{
						Log.i("-위 오-", "11");
						if (check2[3] - check2[0] > 0)// 위
							way = 0;
						else
							way = 3;
					}
					if (check[1] == 1 && check[2] == 1)// 아래와 왼쪽
					{
						Log.i("-아래 왼-", "11");
						if (check2[1] - check2[2] > 0)// 위
							way = 1;
						else
							way = 2;
					}
					if (check[1] == 1 && check[3] == 1)// 아래와 오른쪽
					{
						Log.i("-아래 오른-", "11");
						if (check2[1] + check2[3] > 0)// 위
							way = 1;
						else
							way = 3;
					}
				}

				for (i = 0; i < 5; i++)
					check[i] = 0;
			}
			// 여기서 way가 0이면 아래에서 위로
			// 1이면 위에서 아래로
			// 3면 오른쪽에서 왼쪽으로
			// 2이면 왼쪽에서 오른쪽으로 이동한다. 거기에 맞게 말해주면 된다.

		}

		public void Define() {
			// for (;count<SC.nCount;) {
			// 가진 데이터 비교
			// lat = AndroidSocket.latitude;
			// lon = AndroidSocket.longitude;
			Log.i("#" + Double.toString(SC.nArr[count][0]), "lat");
			Log.i("#" + Double.toString(SC.nArr[count][1]), "lon");
			Log.i("#" + Double.toString(SC.nArr[count + 1][0]), "lat+1");
			Log.i("#" + Double.toString(SC.nArr[count + 1][1]), "lon+1");
			Log.i("#" + Integer.toString(count), "count");
			if (lat - SC.nArr[count + 1][0] > 0)// 양수면 아래
			{
				Log.i("#" + "아래", "11");
				check[1] = 1;
				check2[1] = lat - SC.nArr[count + 1][0];
				temp = 1;

			}
			if (lat - SC.nArr[count + 1][0] < 0)// 위
			{
				Log.i("#" + "위", "22");
				check[0] = 1;
				check2[0] = lat - SC.nArr[count + 1][0];
				temp = 1;

			}
			if (lon - SC.nArr[count + 1][1] > 0)// 양수면 좌
			{
				Log.i("#" + "좌", "33");
				check[2] = 1;
				check2[2] = lon - SC.nArr[count + 1][1];
				temp = 2;

			}
			if (lon - SC.nArr[count + 1][1] < 0)// 우
			{

				Log.i("#" + lon + "  " + SC.nArr[count + 1][1], "44");

				Log.i("#" + "우", "44");
				check[3] = 1;
				check2[3] = lon - SC.nArr[count + 1][1];
				temp = 2;

			}

		}

		public void nive1()// check = 위 아래 좌 우
		{
			int temp = 0;
			int i;
			for (i = 0; i < 4; i++)
				if (check[i] == 1)
					temp++;
			// if (temp == 0)// 다음께 그자리에 찍힐 경우.. 에러다
			// continue;
			if (temp == 1)// x든 y든 한쪽이다. 정확한 길안내 가능
			{
				Log.i("#" + "정확", "11");
				if (check[0] == 1)
					TTS_S("직진 입니다.");
					//tts.speak("직진 입니다.", TextToSpeech.QUEUE_ADD, null);
				if (check[1] == 1)
					TTS_S("뒤로 돌아가세요");
					//tts.speak("뒤로 돌아가세요", TextToSpeech.QUEUE_ADD, null);
				if (check[2] == 1) {
					TTS_S("왼쪽으로 가세요");
					//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
				}
				if (check[3] == 1) {
					TTS_S("오른쪽으로 가세요");
					//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
				}
			}

			if (temp == 2)// 이게 문제다. gps가 튀어서 이상한 곳으로 잡았다 or 정확히 아래가 아니다. 이 경우
							// 크기 비교로 가능
			{
				Log.i("#" + "비정확", "11");
				Log.i("#" + Double.toString(lat) + "  "
						+ Double.toString(SC.nArr[count + 1][0]), "11");
				Log.i("#" + Double.toString(lon) + "  "
						+ Double.toString(SC.nArr[count + 1][1]), "11");
				Log.i("##" + Double.toString(check2[0]) + "  "
						+ Double.toString(check2[1]), "11");
				Log.i("##" + Double.toString(check2[2]) + "  "
						+ Double.toString(check2[3]), "11");
				Log.i("##"
						+ (Double.toString(check2[2]) + Double.toString(check2[3])),
						"11");
				// 1= lat-SC.nArr[count + 1][0]
				// 2=lon-SC.nArr[count + 1][1]
				if (check[0] == 1 && check[2] == 1)// 위와 왼쪽
				{
					Log.i("#" + "-위 왼-", "11");
					if (check2[0] + check2[2] > 0)// 위
					{
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					} else
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
				}
				if (check[0] == 1 && check[3] == 1)// 위와 오른쪽
				{
					Log.i("#" + "-위 오-", "11");
					Log.i("#" + Double.toString(check2[3]) + "  "
							+ Double.toString(check2[0]), "11");

					if (check2[3] - check2[0] > 0)// 위
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[2] == 1)// 아래와 왼쪽
				{
					Log.i("#" + "-아래 왼-", "11");

					if (check2[1] - check2[2] > 0)// 위
					{
						
					}
						//tts.speak("", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[3] == 1)// 아래와 오른쪽
				{
					Log.i("#" + "-아래 오른-", "11");
					Log.i("#" + Double.toString(lon) + "  "
							+ Double.toString(SC.nArr[count + 1][1]), "11");

					if (check2[1] + check2[3] > 0)// 위
					{
						
					}
						//tts.speak("", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
			}

			for (i = 0; i < 5; i++)
				check[i] = 0;

			// 0.0001

			// }
		}

		public void nive2()// 위에서 아래로 check = 위 아래 좌 우
		{

			int temp = 0;
			int i;
			for (i = 0; i < 4; i++)
				if (check[i] == 1)
					temp++;
			// if (temp == 0)// 다음께 그자리에 찍힐 경우.. 에러다
			// continue;
			if (temp == 1)// x든 y든 한쪽이다. 정확한 길안내 가능
			{
				Log.i("#" + "정확", "11");
				if (check[0] == 1)

					if (check[1] == 1)

						if (check[2] == 1) {

						}
				if (check[3] == 1) {

				}
			}

			if (temp == 2)// 이게 문제다. gps가 튀어서 이상한 곳으로 잡았다 or 정확히 아래가 아니다. 이 경우
							// 크기 비교로 가능
			{
				Log.i("#" + "비정확", "11");
				Log.i("#" + Double.toString(lat) + "  "
						+ Double.toString(SC.nArr[count + 1][0]), "11");
				Log.i("#" + Double.toString(lon) + "  "
						+ Double.toString(SC.nArr[count + 1][1]), "11");
				Log.i("##" + Double.toString(check2[0]) + "  "
						+ Double.toString(check2[1]), "11");
				Log.i("##" + Double.toString(check2[2]) + "  "
						+ Double.toString(check2[3]), "11");
				Log.i("##"
						+ (Double.toString(check2[2]) + Double.toString(check2[3])),
						"11");
				// 1= lat-SC.nArr[count + 1][0]
				// 2=lon-SC.nArr[count + 1][1]
				if (check[0] == 1 && check[2] == 1)// 위와 왼쪽
				{
					Log.i("#" + "-위 왼-", "11");
					if (check2[0] + check2[2] > 0)// 위
					{
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					} else
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
				}
				if (check[0] == 1 && check[3] == 1)// 위와 오른쪽
				{
					Log.i("#" + "-위 오-", "11");
					Log.i("#" + Double.toString(check2[3]) + "  "
							+ Double.toString(check2[0]), "11");

					if (check2[3] - check2[0] > 0)// 위
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[2] == 1)// 아래와 왼쪽
				{
					Log.i("#" + "-아래 왼-", "11");

					if (check2[1] - check2[2] > 0)// 위
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("뒤로 돌아 가세요");
						//tts.speak("뒤로 돌아 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[3] == 1)// 아래와 오른쪽
				{
					Log.i("#" + "-아래 오른-", "11");
					Log.i("#" + Double.toString(lon) + "  "
							+ Double.toString(SC.nArr[count + 1][1]), "11");

					if (check2[1] + check2[3] > 0)// 위
						TTS_S("오른쪽으로 가세요");
					//	tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("뒤로 돌아 가세요");
					//	tts.speak("뒤로 돌아 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
			}

			for (i = 0; i < 5; i++)
				check[i] = 0;

			// 0.0001

			// }
		}

		public void nive3()// 좌에서 우로 갈때. check = 위 아래 좌 우
		{

			int temp = 0;
			int i;
			for (i = 0; i < 4; i++)
				if (check[i] == 1)
					temp++;
			// if (temp == 0)// 다음께 그자리에 찍힐 경우.. 에러다
			// continue;
			if (temp == 1)// x든 y든 한쪽이다. 정확한 길안내 가능
			{
				Log.i("#" + "정확", "11");
				if (check[0] == 1)

					if (check[1] == 1)

						if (check[2] == 1) {

						}
				if (check[3] == 1) {

				}
			}

			if (temp == 2)// 이게 문제다. gps가 튀어서 이상한 곳으로 잡았다 or 정확히 아래가 아니다. 이 경우
							// 크기 비교로 가능
			{
				Log.i("#" + "비정확", "11");
				Log.i("#" + Double.toString(lat) + "  "
						+ Double.toString(SC.nArr[count + 1][0]), "11");
				Log.i("#" + Double.toString(lon) + "  "
						+ Double.toString(SC.nArr[count + 1][1]), "11");
				Log.i("##" + Double.toString(check2[0]) + "  "
						+ Double.toString(check2[1]), "11");
				Log.i("##" + Double.toString(check2[2]) + "  "
						+ Double.toString(check2[3]), "11");
				Log.i("##"
						+ (Double.toString(check2[2]) + Double.toString(check2[3])),
						"11");
				// 1= lat-SC.nArr[count + 1][0]
				// 2=lon-SC.nArr[count + 1][1]
				if (check[0] == 1 && check[2] == 1)// 위와 왼쪽
				{
					Log.i("#" + "-위 왼-", "11");
					if (check2[0] + check2[2] > 0)// 위
					{
						TTS_S("뒤로 돌아가세요");
						//tts.speak("뒤로 돌아가세요", TextToSpeech.QUEUE_ADD, null);
					} else
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
				}
				if (check[0] == 1 && check[3] == 1)// 위와 오른쪽
				{
					Log.i("#" + "-위 오-", "11");
					Log.i("#" + Double.toString(check2[3]) + "  "
							+ Double.toString(check2[0]), "11");

					if (check2[3] - check2[0] > 0)// 위
						TTS_S("왼쪽으로 가세요");
					//	tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("직진 하세요");
					//	tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[2] == 1)// 아래와 왼쪽
				{
					Log.i("#" + "-아래 왼-", "11");

					if (check2[1] - check2[2] > 0)// 위
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("뒤로 돌아가세요");
						//tts.speak("뒤로 돌아가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[3] == 1)// 아래와 오른쪽
				{
					Log.i("#" + "-아래 오른-", "11");
					Log.i("#" + Double.toString(lon) + "  "
							+ Double.toString(SC.nArr[count + 1][1]), "11");

					if (check2[1] + check2[3] > 0)// 위
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
			}

			for (i = 0; i < 5; i++)
				check[i] = 0;

			// 0.0001

			// }
		}

		public void nive4()// 좌에서 우로 갈때. check = 위 아래 좌 우
		{

			int temp = 0;
			int i;
			for (i = 0; i < 4; i++)
				if (check[i] == 1)
					temp++;
			// if (temp == 0)// 다음께 그자리에 찍힐 경우.. 에러다
			// continue;
			if (temp == 1)// x든 y든 한쪽이다. 정확한 길안내 가능
			{
				Log.i("#" + "정확", "11");
				if (check[0] == 1)

					if (check[1] == 1)

						if (check[2] == 1) {

						}
				if (check[3] == 1) {

				}
			}

			if (temp == 2)// 이게 문제다. gps가 튀어서 이상한 곳으로 잡았다 or 정확히 아래가 아니다. 이 경우
							// 크기 비교로 가능
			{
				Log.i("#" + "비정확", "11");
				Log.i("#" + Double.toString(lat) + "  "
						+ Double.toString(SC.nArr[count + 1][0]), "11");
				Log.i("#" + Double.toString(lon) + "  "
						+ Double.toString(SC.nArr[count + 1][1]), "11");
				Log.i("##" + Double.toString(check2[0]) + "  "
						+ Double.toString(check2[1]), "11");
				Log.i("##" + Double.toString(check2[2]) + "  "
						+ Double.toString(check2[3]), "11");
				Log.i("##"
						+ (Double.toString(check2[2]) + Double.toString(check2[3])),
						"11");
				// 1= lat-SC.nArr[count + 1][0]
				// 2=lon-SC.nArr[count + 1][1]
				if (check[0] == 1 && check[2] == 1)// 위와 왼쪽
				{
					Log.i("#" + "-위 왼-", "11");
					if (check2[0] + check2[2] > 0)// 위
					{
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					} else
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
				}
				if (check[0] == 1 && check[3] == 1)// 위와 오른쪽
				{
					Log.i("#" + "-위 오-", "11");
					Log.i("#" + Double.toString(check2[3]) + "  "
							+ Double.toString(check2[0]), "11");

					if (check2[3] - check2[0] > 0)// 위
						TTS_S("직진 하세요");
						//tts.speak("직진 하세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[2] == 1)// 아래와 왼쪽
				{
					Log.i("#" + "-아래 왼-", "11");

					if (check2[1] - check2[2] > 0)// 위
						TTS_S("뒤로 돌아가세요");
						//tts.speak("뒤로 돌아가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("왼쪽으로 가세요");
						//tts.speak("왼쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
				if (check[1] == 1 && check[3] == 1)// 아래와 오른쪽
				{
					Log.i("#" + "-아래 오른-", "11");
					Log.i("#" + Double.toString(lon) + "  "
							+ Double.toString(SC.nArr[count + 1][1]), "11");

					if (check2[1] + check2[3] > 0)// 위
						TTS_S("뒤로 돌아가세요");
						//tts.speak("뒤로 돌아가세요", TextToSpeech.QUEUE_ADD, null);
					else {
						TTS_S("오른쪽으로 가세요");
						//tts.speak("오른쪽으로 가세요", TextToSpeech.QUEUE_ADD, null);
					}
				}
			}

			for (i = 0; i < 5; i++)
				check[i] = 0;

			// 0.0001

			// }
		}

		public void ViewMap(double latt, double lonn) {
			LatLng latLng = new LatLng(latt, lonn);

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.map_icon01));
			markerOptions.position(latLng);
			// gmap.clear();
			gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			gmap.addMarker(markerOptions);

		}

		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}
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
                    locationManager.requestLocationUpdates(provider, 1L, 2F, tmp2.this);
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
        if (gmap == null) {
        	gmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (gmap != null) {
                setUpMap();
            }
        }
    }
 
    private void setUpMap() {
    	gmap.setMyLocationEnabled(true);
    	gmap.getMyLocation();
 
    }
 
    boolean locationTag=true;
 
    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            lat =  location.getLatitude();
            lon = location.getLongitude();
 
            Toast.makeText(tmp2.this, "위도  : " + lat +  " 경도: "  + lon ,  Toast.LENGTH_SHORT).show();
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
// 37.56647 126.97796 