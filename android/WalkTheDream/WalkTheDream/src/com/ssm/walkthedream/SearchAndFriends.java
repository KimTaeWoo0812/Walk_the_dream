package com.ssm.walkthedream;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class SearchAndFriends extends Activity implements OnInitListener{
	private AndroidSocket soccket ;
	MyLocationListener gps;
	
	String id;
	CSharedPreferences sharedPreferences;
	TextToSpeech tts;
	static int isFirst=3;
	private final int GOOGLE_STT = 1000, MY_UI=1001;				//requestCode. 구글음성인식
	private ArrayList<String> mResult;									//음성인식 결과 저장할 list
	private String mSelectedString;										//결과 list 중 사용자가 선택한 텍스트
	private TextView mResultTextView;									//최종 결과 출력하는 텍스트 뷰
	private boolean stopThread=true;
	private String shoesName="ToM";
	private boolean isWorkingBluetooth=false;
	Handler hStairs;
	Intent ii;
	StairsTTSThread stairsTTS;
	// 사용자 정의 함수로 블루투스 활성 상태의 변경 결과를 App으로 알려줄때 식별자로 사용됨 (0보다 커야함) 
    static final int REQUEST_ENABLE_BT = 10;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    Handler handler;
    // 폰의 블루투스 모듈을 사용하기 위한 오브젝트.
    BluetoothAdapter mBluetoothAdapter;

    BluetoothDevice mRemoteDevie;
    // 스마트폰과 페어링 된 디바이스간 통신 채널에 대응 하는 BluetoothSocket
    
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';
    
 
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			Log.i("KEYDOWN", "KEYCODE_VOLUME_DOWN");

			startActivity(new Intent(getApplicationContext(), CheckVideo.class));

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			Log.i("KEYDOWN", "KEYCODE_VOLUME_UP");
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("KEYDOWN", "KEYCODE_BACK");
			onBackPressed();
		}

		return true;
	}
    
    
	@Override
	protected void onPause() {
		
		gps.endGps();
		stopThread=false;
		super.onPause();
	}
	@Override
	protected void onResume() {
		soccket=AndroidSocket.shared();
		soccket.id=this.id;
		
			 hStairs = new Handler();

			 stairsTTS=new StairsTTSThread();
			 stairsTTS.start();
			 if(!isWorkingBluetooth)
				 checkBluetooth();// 블루투스 활성화
		 
		super.onResume();
	}
	@Override
    public void onDestroy()
    {
		super.onDestroy();
		stopThread=false;
		tts.shutdown();
		
        try {
        	if(soccket.OPTION_Bluetooth&&soccket.BluetoothConnected){
        		soccket.mInputStream.close(); 
        		soccket.mSocket.close();
        	}
        	try {
				soccket.CloseSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
	@Override
	public void onInit(int status) {
		//if(isFirst==0&&AndroidSocket.OPTION_TTS){
		//String myText = "안녕하세요. 워크 더 드림 앱에 온신걸 환영합니다."
		//		+ "이 앱은 시각장애인을 위한 네비게이션 앱 입니다. 목적지를 설정하고 길찾기를 누르면 안내가 시작됩니다."
		//		+ "그리고 친구 버튼을 눌러서 친구를 추가하면 친구의 실시간 위치도 확인이 가능합니다."
		//		+ "자주가는 길은 자주가는 길에 저장해서 편하게 사용하세요.";
	// tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
	//}
	}
	
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
 
        alertDialog.setTitle("첫 접속입니다.");
        alertDialog.setMessage("앱 사용법 설명을 들으시겠습니까?");
        final Intent intent = new Intent(this, IntroSpeech.class);
        
        // OK 를 누르게 되면 설정창으로 이동합니다. 
        alertDialog.setPositiveButton("설정", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                startActivity(intent);
            }
        });
        // Cancle 하면 종료 합니다. 
        alertDialog.setNegativeButton("취소", 
                              new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        alertDialog.show();
    }
    LinearLayout Btn1;
    LinearLayout Btn2;
    LinearLayout Btn3;
    LinearLayout ll_search_setting;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        Intent intent=getIntent();
        this.id=intent.getStringExtra("id");
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
       
        
        Btn1 = (LinearLayout)findViewById(R.id.Btn1);
        Btn2= (LinearLayout)findViewById(R.id.Btn2);
        Btn3= (LinearLayout)findViewById(R.id.Btn3);
        ll_search_setting= (LinearLayout)findViewById(R.id.ll_search_setting);
       
        Btn1.setContentDescription("길 찾기");
        Btn2.setContentDescription("자주가는 길");
        Btn3.setContentDescription("친구 기능");
        ll_search_setting.setContentDescription("설정");

        
        sharedPreferences = new CSharedPreferences(this);
        isFirst=sharedPreferences.IsFirst();
        tts = new TextToSpeech(this, this);
        gps = new MyLocationListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면항상밝게
        {
            if(isFirst==0)
            {
            	sharedPreferences.savePreferences("naviType","1");
               showSettingsAlert();
               sharedPreferences.savePreferences("isFirst", "1");
            }
            
         }
        String strNavi=sharedPreferences.getPreferences("naviType");
		{
			if (strNavi == "2")
				soccket.OPTION_NAVI_TYPE = false;
			else
				soccket.OPTION_NAVI_TYPE = true;
		}
        
		 //STT 부분
		 mResultTextView = (TextView)findViewById(R.id.Text2);		//결과 출력 뷰
			
		 ii = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			//intent 생성
		 ii.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	//음성인식을 호출한 패키지
		 ii.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							//음성인식 언어 설정
		 ii.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요.");						//사용자에게 보여 줄 글자
			
			//startActivityForResult(i, GOOGLE_STT);		
		    
		 
		 
		 
		 double lat=AndroidSocket.latitude;
		 double lon=AndroidSocket.longitude;
		 
		 
		 
		 
		 
		 
		 
		 //if(lat==0)
		//	 gps.showSettingsAlert();
		 
    }
    
    
    
    
    
    
    
    
    
    public void onClick(View view) {//버튼 눌렀을때
        switch (view.getId()) {
        	case R.id.Btn1://길 찾기 버튼
        		startActivity(new Intent(getApplicationContext(),SearchDestination.class));
        		break;
        	case R.id.Btn2://자주가는 위치 버튼
        		Intent intent2 = new Intent(this, OftenWay.class);
            	intent2.putExtra("id", id);
            	startActivity(intent2);
        		break;
        	case R.id.Btn3://친구 버튼
        		Intent intent = new Intent(this, FriendsManage.class);
            	intent.putExtra("id", id);
            	startActivity(intent);
        		break;
        	case R.id.ll_search_setting:
        		Intent intent5 = new Intent(this, Option.class);
            	intent5.putExtra("id", id);
            	startActivity(intent5);
        		
//        		String myText2 = "목적지를 말하세요.";
//    			tts.speak(myText2, TextToSpeech.QUEUE_FLUSH, null);
//        		startActivityForResult(ii, GOOGLE_STT);	
        		break;
        }
    
    }
    

//STT 함수
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if( resultCode == RESULT_OK  && (requestCode == GOOGLE_STT || requestCode == MY_UI) ){		//결과가 있으면
			showSelectDialog(requestCode, data);				//결과를 다이얼로그로 출력.
		}
		else{															//결과가 없으면 에러 메시지 출력
			String msg = null;
			
			//내가 만든 activity에서 넘어오는 오류 코드를 분류
			switch(resultCode){
				case SpeechRecognizer.ERROR_AUDIO:
					msg = "오디오 입력 중 오류가 발생했습니다.";
					break;
				case SpeechRecognizer.ERROR_CLIENT:
					msg = "단말에서 오류가 발생했습니다.";
					break;
				case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
					msg = "권한이 없습니다.";
					break;
				case SpeechRecognizer.ERROR_NETWORK:
				case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
					msg = "네트워크 오류가 발생했습니다.";
					break;
				case SpeechRecognizer.ERROR_NO_MATCH:
					msg = "일치하는 항목이 없습니다.";
					break;
				case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
					msg = "음성인식 서비스가 과부하 되었습니다.";
					break;
				case SpeechRecognizer.ERROR_SERVER:
					msg = "서버에서 오류가 발생했습니다.";
					break;
				case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
					msg = "입력이 없습니다.";
					break;
			}
			
			if(msg != null)		//오류 메시지가 null이 아니면 메시지 출력
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
		
		 switch(requestCode) {
	        case REQUEST_ENABLE_BT:
	            if(resultCode == RESULT_OK) { // 블루투스 활성화 상태
	                selectDevice();
	            }
	            else if(resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
	                //Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
	                //finish();
	            }
	            break;
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	}
	
	//결과 list 출력하는 다이얼로그 생성
	private void showSelectDialog(int requestCode, Intent data){
		String key = "";
		if(requestCode == GOOGLE_STT)					//구글음성인식이면
			key = RecognizerIntent.EXTRA_RESULTS;	//키값 설정
		
		mResult = data.getStringArrayListExtra(key);		//인식된 데이터 list 받아옴.
		String[] result = new String[mResult.size()];			//배열생성. 다이얼로그에서 출력하기 위해
		mResult.toArray(result);									//	list 배열로 변환
		if(AndroidSocket.OPTION_TTS)
		{
			for(int i=0;i<mResult.size();i++)
			{
				String myText = result[i];
				tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
			}
		tts.speak("중에 하나를 고르세요.", TextToSpeech.QUEUE_ADD, null);
		}
		//1개 선택하는 다이얼로그 생성
		AlertDialog ad = new AlertDialog.Builder(this).setTitle("선택하세요.")
							.setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
										mSelectedString = mResult.get(which);		//선택하면 해당 글자 저장
										String myText = mSelectedString;
										if(AndroidSocket.OPTION_TTS){
											tts.stop();
											tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
										}
								}
							})
							.setPositiveButton("확인", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText(mSelectedString);		//확인 버튼 누르면 결과 출력
									String myText = mSelectedString+"로 검색합니다.";
									if(AndroidSocket.OPTION_TTS){
										tts.stop();
										tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
									}
								}
							})
							.setNegativeButton("취소", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText("");		//취소버튼 누르면 초기화
									mSelectedString = null;
								}
							}).create();
		ad.show();
	}
    
	
	
	
//	public class StairsTTSThread extends Thread{
//		//int temp=1;
//		public StairsTTSThread(){
//			
//		}
//		public void run(){
//			Log.i("111","들어옴2");
//			for(;stopThread;)
//			 {
//				 try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				 
//				 //soccket.sendData(temp+"\n\n");
//				 //temp=(temp+1)%3;
//				 
//			       	if(soccket.hTop!=soccket.hRear)
//			       	{
//			       		hStairs.post(new Runnable() {
//			       			String myText;
//			       			@Override
//			       			public void run() {;
//			       			Log.i("#111",Integer.toString(soccket.hQueue[soccket.hRear%20]));
//			       			Log.i("#111",Integer.toString(soccket.hRear));
//			       			Log.i("#111",Integer.toString(soccket.hTop));
//			       				switch(soccket.hQueue[soccket.hRear%20])
//			       				{
//			       				case 10:
//			       					myText = "전방에 올라가는 계단이 있습니다.";
//			       					//if(soccket.BluetoothConnected)
//			       					//	soccket.sendData("1\n\n");
//			       					if(AndroidSocket.OPTION_TTS)
//				       					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//			       					break;
//			       				case 11:
//			       					myText = "전방에 내려가는 계단이 있습니다.";
//			       					//if(soccket.BluetoothConnected)
//			       					//	soccket.sendData("2\n\n");
//			       					if(AndroidSocket.OPTION_TTS)
//				       					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//			       					break;
//			       				case 1:
//			       					//SendStairsData('1');
//									//if (AndroidSocket.OPTION_TTS)
//									//	myText = "올라가는 계단을 저장하였습니다.";
//			       					break;
//			       				case 2:
//			       					//SendStairsData('2');
//									//if (AndroidSocket.OPTION_TTS)
//									//	myText = "내려가는 계단을 저장하였습니다.";
//									break;
//			       				}
//			       				//if(AndroidSocket.OPTION_TTS)
//			       					//tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//			       				
//			       				soccket.hRear=soccket.hTop;
//			       			}
//			       		});
//			       	}
//			 }
//	
//		}
	public class StairsTTSThread extends Thread {
		private long time[] = new long[20];// 0 시간 1 lat 2 lon
		private int top;
		private final int TIMESIZE = 20;

		public StairsTTSThread() {
			Log.i("#들어옴", "1");
			top = 3;
			time[0] = 0;
			time[1] = 0;
			time[2] = 0;
		}

		public void run() {
			Log.i("#111", "들어옴2");
			for (; stopThread;) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (soccket.hTop != soccket.hRear) {
					if(soccket.hQueue[soccket.hRear % 20]==1||soccket.hQueue[soccket.hRear % 20]==2)
 {
						time[top % TIMESIZE] = System.currentTimeMillis();
						top++;
					}
					Log.i("#333", "3333");
					{
						if (soccket.hQueue[soccket.hRear % 20] == 10) {
							hStairs.post(new Runnable() {
								String myText;

								@Override
								public void run() {
									myText = "전방에 올라가는 계단이 있습니다.";
									// if(soccket.BluetoothConnected)
									// soccket.sendData("1\n\n");
									if (AndroidSocket.OPTION_TTS)
										tts.speak(myText,
												TextToSpeech.QUEUE_FLUSH, null);
									soccket.hRear++;
								}
							});
						}
						if (soccket.hQueue[soccket.hRear % 20] == 11) {
							hStairs.post(new Runnable() {
								String myText;

								@Override
								public void run() {
									myText = "전방에 내려가는 계단이 있습니다.";
									// if(soccket.BluetoothConnected)
									// soccket.sendData("2\n\n");
									if (AndroidSocket.OPTION_TTS)
										tts.speak(myText,
												TextToSpeech.QUEUE_FLUSH, null);
									soccket.hRear++;
								}
							});
						}

						if (time[top % TIMESIZE] - time[(top - 3) % TIMESIZE] < 150000
								&& time[(top - 3) % TIMESIZE] != 0)// 15초 이내에 3번 이상
						{
							Log.i("#444", "444");
							hStairs.post(new Runnable() {
								String myText;

								@Override
								public void run() {
									switch (soccket.hQueue[soccket.hRear % 20]) {
									// case 10:
									// myText = "전방에 올라가는 계단이 있습니다.";
									// // if(soccket.BluetoothConnected)
									// // soccket.sendData("1\n\n");
									// if (AndroidSocket.OPTION_TTS)
									// myTTS.speak(myText,
									// TextToSpeech.QUEUE_FLUSH, null);
									// break;
									// case 11:
									// myText = "전방에 내려가는 계단이 있습니다.";
									// // if(soccket.BluetoothConnected)
									// // soccket.sendData("2\n\n");
									// if (AndroidSocket.OPTION_TTS)
									// myTTS.speak(myText,
									// TextToSpeech.QUEUE_FLUSH, null);
									// break;
									case 1:
										SendStairsData('1');
										if (AndroidSocket.OPTION_TTS)
											myText = "올라가는 계단을 저장하였습니다.";
										break;
									case 2:
										SendStairsData('2');
										if (AndroidSocket.OPTION_TTS)
											myText = "내려가는 계단을 저장하였습니다.";
										break;
									}
									if (AndroidSocket.OPTION_TTS)
										tts.speak(myText,
												TextToSpeech.QUEUE_FLUSH, null);

									soccket.hRear++;
									top = 3;
									time[0] = 0;
									time[1] = 0;
									time[2] = 0;
								}
							});
						} else
							soccket.hRear++;

					}
				}
			}

		}
		public void SendStairsData(char type)
		{
			String strTemp="A_SAVESTA#";
			char cMsg[]=new char[128];
			String lat = Double.toString( soccket.latitude);//gps.loc.getLatitude());
			String lon = Double.toString(soccket.longitude);//gps.loc.getLongitude());
			int i;
			
			for(i=0;strTemp.charAt(i)!='#';i++)
				cMsg[i]=strTemp.charAt(i);
		 
			for(i=21;i<lat.length()+21;i++)
				cMsg[i]=lat.charAt(i-21);
			cMsg[i]='#';
			
			for(i=41;i<lon.length()+41;i++)
				cMsg[i]=lon.charAt(i-41);
			cMsg[i]='#';
			
			cMsg[61]=type;
			cMsg[62]='#';
			
			strTemp="";
			for(i=0;i<128;i++)
				strTemp+=cMsg[i];
			soccket.SendMessage(strTemp);
		}
		
		
	}
	
    //////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	//블루투스
	////////////////////////////////////////////////
    // 블루투스 장치의 이름이 주어졌을때 해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내는 코드. 
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴. 
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며, 
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다. 
        for(BluetoothDevice deivce : mDevices) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if(name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }
    
    
 
    //  connectToSelectedDevice() : 원격 장치와 연결하는 과정을 나타냄. 
    //        실제 데이터 송수신을 위해서는 소켓으로부터 입출력 스트림을 얻고 입출력 스트림을 이용하여 이루어 진다. 
    void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);



       // mRemoteDevie = mBluetoothAdapter.getRemoteDevice(selectedDeviceName);
     
        
        
        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        //UUID uuid = java.util.UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");
        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결. 
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와 통신할 수 있는 소켓을 생성함. 
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는 BluetoothSocket 오브젝트를 리턴함. 
        	

        	soccket.mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            //Toast.makeText(getApplicationContext(), "222222222222", Toast.LENGTH_LONG).show();
        	soccket.mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.
        	Toast.makeText(getApplicationContext(), "신발과 연결이 되었습니다.", Toast.LENGTH_LONG).show();
        	if(AndroidSocket.OPTION_TTS)
        		tts.speak("신발과 연결이 되었습니다. 장애물 정보를 받습니다.", TextToSpeech.QUEUE_ADD, null);
            // 데이터 송수신을 위한 스트림 얻기. 
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다. 
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
        	Log.i("#888","888");
            soccket.mOutputStream = soccket.mSocket.getOutputStream();
            soccket.mInputStream = soccket.mSocket.getInputStream();
            soccket.BluetoothConnected=true;
            // 데이터 수신 준비. 
            beginListenForData();
            
        }catch(Exception e) { // 블루투스 연결 중 오류 발생
//            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            finish();  // App 종료
        	e.printStackTrace();
        }
    }
    
    
    //이 시작부분을 건물 내 계단 취합을 방지하기위해 네비 시작하면 하는걸로 해야된다.
    // 블루투스 수신 스레드 시작
    void beginListenForData() {
    	Log.i("#777","777");
    	soccket.StartBluetoothReceiveThread();
    }
    
    // 블루투스 지원하며 활성 상태인 경우.
    void selectDevice() {
        // 블루투스 디바이스는 연결해서 사용하기 전에 먼저 페어링 되어야만 한다
        // getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.  
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();
        
        if(mPariedDeviceCount == 0 ) { // 페어링된 장치가 없는 경우. 
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            //finish(); // App 종료. 
        }
        int nTemp=0;
        for(BluetoothDevice device : mDevices) {
        	nTemp++;
        	if(device.getName().equals(shoesName))//이거 이름 신발이름으로 바꿔야댐
        	{
        		//이미 페어링된 신발이 있을 경우 자동 연결
                connectToSelectedDevice(shoesName);
                return;
        	}
        	
        	
        }
        if(nTemp==0){
        	Toast.makeText(getApplicationContext(), "먼저 신발과 페어링을 해주세요.", Toast.LENGTH_LONG).show();
        }
        
        
        
        // 페어링된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");
        
        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다. 
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환. 
            listItems.add(device.getName());
        }
        listItems.add("취소");  // 취소 항목 추가. 
        
        
        // CharSequence : 변경 가능한 문자열. 
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
          // toArray 함수를 이용해서 size만큼 배열이 생성 되었다. 
          listItems.toArray(new CharSequence[listItems.size()]);
          
          builder.setItems(items, new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if(item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우. 
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    
                }
                else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함. 
                    connectToSelectedDevice(items[item].toString());
                }
            }
              
          });
          
          builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지. 
          AlertDialog alert = builder.create();
          alert.show();
          
    }
 
    
    void checkBluetooth() {
    	isWorkingBluetooth=true;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null ) {  // 블루투스 미지원
            Toast.makeText(getApplicationContext(), "기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
           //finish();  // 앱종료
        }
        else { // 블루투스 지원
            if(!mBluetoothAdapter.isEnabled()) { // 블루투스 지원하며 비활성 상태인 경우.
                Toast.makeText(getApplicationContext(), "현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
                // REQUEST_ENABLE_BT : 블루투스 활성 상태의 변경 결과를 App 으로 알려줄 때 식별자로 사용(0이상)
              
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else // 블루투스 지원하며 활성 상태인 경우.
                selectDevice();
        }
    }
    
}
