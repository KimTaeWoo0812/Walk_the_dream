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
	private final int GOOGLE_STT = 1000, MY_UI=1001;				//requestCode. ���������ν�
	private ArrayList<String> mResult;									//�����ν� ��� ������ list
	private String mSelectedString;										//��� list �� ����ڰ� ������ �ؽ�Ʈ
	private TextView mResultTextView;									//���� ��� ����ϴ� �ؽ�Ʈ ��
	private boolean stopThread=true;
	private String shoesName="ToM";
	private boolean isWorkingBluetooth=false;
	Handler hStairs;
	Intent ii;
	StairsTTSThread stairsTTS;
	// ����� ���� �Լ��� ������� Ȱ�� ������ ���� ����� App���� �˷��ٶ� �ĺ��ڷ� ���� (0���� Ŀ����) 
    static final int REQUEST_ENABLE_BT = 10;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    Handler handler;
    // ���� ������� ����� ����ϱ� ���� ������Ʈ.
    BluetoothAdapter mBluetoothAdapter;

    BluetoothDevice mRemoteDevie;
    // ����Ʈ���� �� �� ����̽��� ��� ä�ο� ���� �ϴ� BluetoothSocket
    
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
				 checkBluetooth();// ������� Ȱ��ȭ
		 
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
		//String myText = "�ȳ��ϼ���. ��ũ �� �帲 �ۿ� �½Ű� ȯ���մϴ�."
		//		+ "�� ���� �ð�������� ���� �׺���̼� �� �Դϴ�. �������� �����ϰ� ��ã�⸦ ������ �ȳ��� ���۵˴ϴ�."
		//		+ "�׸��� ģ�� ��ư�� ������ ģ���� �߰��ϸ� ģ���� �ǽð� ��ġ�� Ȯ���� �����մϴ�."
		//		+ "���ְ��� ���� ���ְ��� �濡 �����ؼ� ���ϰ� ����ϼ���.";
	// tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
	//}
	}
	
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
 
        alertDialog.setTitle("ù �����Դϴ�.");
        alertDialog.setMessage("�� ���� ������ �����ðڽ��ϱ�?");
        final Intent intent = new Intent(this, IntroSpeech.class);
        
        // OK �� ������ �Ǹ� ����â���� �̵��մϴ�. 
        alertDialog.setPositiveButton("����", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                startActivity(intent);
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
       
        Btn1.setContentDescription("�� ã��");
        Btn2.setContentDescription("���ְ��� ��");
        Btn3.setContentDescription("ģ�� ���");
        ll_search_setting.setContentDescription("����");

        
        sharedPreferences = new CSharedPreferences(this);
        isFirst=sharedPreferences.IsFirst();
        tts = new TextToSpeech(this, this);
        gps = new MyLocationListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // ȭ���׻���
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
        
		 //STT �κ�
		 mResultTextView = (TextView)findViewById(R.id.Text2);		//��� ��� ��
			
		 ii = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			//intent ����
		 ii.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	//�����ν��� ȣ���� ��Ű��
		 ii.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							//�����ν� ��� ����
		 ii.putExtra(RecognizerIntent.EXTRA_PROMPT, "���� �ϼ���.");						//����ڿ��� ���� �� ����
			
			//startActivityForResult(i, GOOGLE_STT);		
		    
		 
		 
		 
		 double lat=AndroidSocket.latitude;
		 double lon=AndroidSocket.longitude;
		 
		 
		 
		 
		 
		 
		 
		 //if(lat==0)
		//	 gps.showSettingsAlert();
		 
    }
    
    
    
    
    
    
    
    
    
    public void onClick(View view) {//��ư ��������
        switch (view.getId()) {
        	case R.id.Btn1://�� ã�� ��ư
        		startActivity(new Intent(getApplicationContext(),SearchDestination.class));
        		break;
        	case R.id.Btn2://���ְ��� ��ġ ��ư
        		Intent intent2 = new Intent(this, OftenWay.class);
            	intent2.putExtra("id", id);
            	startActivity(intent2);
        		break;
        	case R.id.Btn3://ģ�� ��ư
        		Intent intent = new Intent(this, FriendsManage.class);
            	intent.putExtra("id", id);
            	startActivity(intent);
        		break;
        	case R.id.ll_search_setting:
        		Intent intent5 = new Intent(this, Option.class);
            	intent5.putExtra("id", id);
            	startActivity(intent5);
        		
//        		String myText2 = "�������� ���ϼ���.";
//    			tts.speak(myText2, TextToSpeech.QUEUE_FLUSH, null);
//        		startActivityForResult(ii, GOOGLE_STT);	
        		break;
        }
    
    }
    

//STT �Լ�
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if( resultCode == RESULT_OK  && (requestCode == GOOGLE_STT || requestCode == MY_UI) ){		//����� ������
			showSelectDialog(requestCode, data);				//����� ���̾�α׷� ���.
		}
		else{															//����� ������ ���� �޽��� ���
			String msg = null;
			
			//���� ���� activity���� �Ѿ���� ���� �ڵ带 �з�
			switch(resultCode){
				case SpeechRecognizer.ERROR_AUDIO:
					msg = "����� �Է� �� ������ �߻��߽��ϴ�.";
					break;
				case SpeechRecognizer.ERROR_CLIENT:
					msg = "�ܸ����� ������ �߻��߽��ϴ�.";
					break;
				case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
					msg = "������ �����ϴ�.";
					break;
				case SpeechRecognizer.ERROR_NETWORK:
				case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
					msg = "��Ʈ��ũ ������ �߻��߽��ϴ�.";
					break;
				case SpeechRecognizer.ERROR_NO_MATCH:
					msg = "��ġ�ϴ� �׸��� �����ϴ�.";
					break;
				case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
					msg = "�����ν� ���񽺰� ������ �Ǿ����ϴ�.";
					break;
				case SpeechRecognizer.ERROR_SERVER:
					msg = "�������� ������ �߻��߽��ϴ�.";
					break;
				case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
					msg = "�Է��� �����ϴ�.";
					break;
			}
			
			if(msg != null)		//���� �޽����� null�� �ƴϸ� �޽��� ���
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
		
		 switch(requestCode) {
	        case REQUEST_ENABLE_BT:
	            if(resultCode == RESULT_OK) { // ������� Ȱ��ȭ ����
	                selectDevice();
	            }
	            else if(resultCode == RESULT_CANCELED) { // ������� ��Ȱ��ȭ ���� (����)
	                //Toast.makeText(getApplicationContext(), "��������� ����� �� �����ϴ�.", Toast.LENGTH_LONG).show();
	                //finish();
	            }
	            break;
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	}
	
	//��� list ����ϴ� ���̾�α� ����
	private void showSelectDialog(int requestCode, Intent data){
		String key = "";
		if(requestCode == GOOGLE_STT)					//���������ν��̸�
			key = RecognizerIntent.EXTRA_RESULTS;	//Ű�� ����
		
		mResult = data.getStringArrayListExtra(key);		//�νĵ� ������ list �޾ƿ�.
		String[] result = new String[mResult.size()];			//�迭����. ���̾�α׿��� ����ϱ� ����
		mResult.toArray(result);									//	list �迭�� ��ȯ
		if(AndroidSocket.OPTION_TTS)
		{
			for(int i=0;i<mResult.size();i++)
			{
				String myText = result[i];
				tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
			}
		tts.speak("�߿� �ϳ��� ������.", TextToSpeech.QUEUE_ADD, null);
		}
		//1�� �����ϴ� ���̾�α� ����
		AlertDialog ad = new AlertDialog.Builder(this).setTitle("�����ϼ���.")
							.setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
										mSelectedString = mResult.get(which);		//�����ϸ� �ش� ���� ����
										String myText = mSelectedString;
										if(AndroidSocket.OPTION_TTS){
											tts.stop();
											tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
										}
								}
							})
							.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText(mSelectedString);		//Ȯ�� ��ư ������ ��� ���
									String myText = mSelectedString+"�� �˻��մϴ�.";
									if(AndroidSocket.OPTION_TTS){
										tts.stop();
										tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
									}
								}
							})
							.setNegativeButton("���", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText("");		//��ҹ�ư ������ �ʱ�ȭ
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
//			Log.i("111","����2");
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
//			       					myText = "���濡 �ö󰡴� ����� �ֽ��ϴ�.";
//			       					//if(soccket.BluetoothConnected)
//			       					//	soccket.sendData("1\n\n");
//			       					if(AndroidSocket.OPTION_TTS)
//				       					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//			       					break;
//			       				case 11:
//			       					myText = "���濡 �������� ����� �ֽ��ϴ�.";
//			       					//if(soccket.BluetoothConnected)
//			       					//	soccket.sendData("2\n\n");
//			       					if(AndroidSocket.OPTION_TTS)
//				       					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//			       					break;
//			       				case 1:
//			       					//SendStairsData('1');
//									//if (AndroidSocket.OPTION_TTS)
//									//	myText = "�ö󰡴� ����� �����Ͽ����ϴ�.";
//			       					break;
//			       				case 2:
//			       					//SendStairsData('2');
//									//if (AndroidSocket.OPTION_TTS)
//									//	myText = "�������� ����� �����Ͽ����ϴ�.";
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
		private long time[] = new long[20];// 0 �ð� 1 lat 2 lon
		private int top;
		private final int TIMESIZE = 20;

		public StairsTTSThread() {
			Log.i("#����", "1");
			top = 3;
			time[0] = 0;
			time[1] = 0;
			time[2] = 0;
		}

		public void run() {
			Log.i("#111", "����2");
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
									myText = "���濡 �ö󰡴� ����� �ֽ��ϴ�.";
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
									myText = "���濡 �������� ����� �ֽ��ϴ�.";
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
								&& time[(top - 3) % TIMESIZE] != 0)// 15�� �̳��� 3�� �̻�
						{
							Log.i("#444", "444");
							hStairs.post(new Runnable() {
								String myText;

								@Override
								public void run() {
									switch (soccket.hQueue[soccket.hRear % 20]) {
									// case 10:
									// myText = "���濡 �ö󰡴� ����� �ֽ��ϴ�.";
									// // if(soccket.BluetoothConnected)
									// // soccket.sendData("1\n\n");
									// if (AndroidSocket.OPTION_TTS)
									// myTTS.speak(myText,
									// TextToSpeech.QUEUE_FLUSH, null);
									// break;
									// case 11:
									// myText = "���濡 �������� ����� �ֽ��ϴ�.";
									// // if(soccket.BluetoothConnected)
									// // soccket.sendData("2\n\n");
									// if (AndroidSocket.OPTION_TTS)
									// myTTS.speak(myText,
									// TextToSpeech.QUEUE_FLUSH, null);
									// break;
									case 1:
										SendStairsData('1');
										if (AndroidSocket.OPTION_TTS)
											myText = "�ö󰡴� ����� �����Ͽ����ϴ�.";
										break;
									case 2:
										SendStairsData('2');
										if (AndroidSocket.OPTION_TTS)
											myText = "�������� ����� �����Ͽ����ϴ�.";
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
	//�������
	////////////////////////////////////////////////
    // ������� ��ġ�� �̸��� �־������� �ش� ������� ��ġ ��ü�� �� �� ��ġ ��Ͽ��� ã�Ƴ��� �ڵ�. 
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : �� �� ��� ����� ����. 
        BluetoothDevice selectedDevice = null;
        // getBondedDevices �Լ��� ��ȯ�ϴ� �� �� ��� ����� Set �����̸�, 
        // Set ���Ŀ����� n ��° ���Ҹ� ������ ����� �����Ƿ� �־��� �̸��� ���ؼ� ã�´�. 
        for(BluetoothDevice deivce : mDevices) {
            // getName() : �ܸ����� Bluetooth Adapter �̸��� ��ȯ
            if(name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }
    
    
 
    //  connectToSelectedDevice() : ���� ��ġ�� �����ϴ� ������ ��Ÿ��. 
    //        ���� ������ �ۼ����� ���ؼ��� �������κ��� ����� ��Ʈ���� ��� ����� ��Ʈ���� �̿��Ͽ� �̷�� ����. 
    void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice ���� ������� ��⸦ ��Ÿ��.
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);



       // mRemoteDevie = mBluetoothAdapter.getRemoteDevice(selectedDeviceName);
     
        
        
        // java.util.UUID.fromString : �ڹٿ��� �ߺ����� �ʴ� Unique Ű ����.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        //UUID uuid = java.util.UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");
        try {
            // ���� ����, RFCOMM ä���� ���� ����. 
            // createRfcommSocketToServiceRecord(uuid) : �� �Լ��� ����Ͽ� ���� ������� ��ġ�� ����� �� �ִ� ������ ������. 
            // �� �޼ҵ尡 �����ϸ� ����Ʈ���� �� �� ����̽��� ��� ä�ο� �����ϴ� BluetoothSocket ������Ʈ�� ������. 
        	

        	soccket.mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            //Toast.makeText(getApplicationContext(), "222222222222", Toast.LENGTH_LONG).show();
        	soccket.mSocket.connect(); // ������ ���� �Ǹ� connect() �Լ��� ȣ�������ν� �α���� ������ �Ϸ�ȴ�.
        	Toast.makeText(getApplicationContext(), "�Ź߰� ������ �Ǿ����ϴ�.", Toast.LENGTH_LONG).show();
        	if(AndroidSocket.OPTION_TTS)
        		tts.speak("�Ź߰� ������ �Ǿ����ϴ�. ��ֹ� ������ �޽��ϴ�.", TextToSpeech.QUEUE_ADD, null);
            // ������ �ۼ����� ���� ��Ʈ�� ���. 
            // BluetoothSocket ������Ʈ�� �ΰ��� Stream�� �����Ѵ�. 
            // 1. �����͸� ������ ���� OutputStrem
            // 2. �����͸� �ޱ� ���� InputStream
        	Log.i("#888","888");
            soccket.mOutputStream = soccket.mSocket.getOutputStream();
            soccket.mInputStream = soccket.mSocket.getInputStream();
            soccket.BluetoothConnected=true;
            // ������ ���� �غ�. 
            beginListenForData();
            
        }catch(Exception e) { // ������� ���� �� ���� �߻�
//            Toast.makeText(getApplicationContext(), "������� ���� �� ������ �߻��߽��ϴ�.", Toast.LENGTH_LONG).show();
//            finish();  // App ����
        	e.printStackTrace();
        }
    }
    
    
    //�� ���ۺκ��� �ǹ� �� ��� ������ �����ϱ����� �׺� �����ϸ� �ϴ°ɷ� �ؾߵȴ�.
    // ������� ���� ������ ����
    void beginListenForData() {
    	Log.i("#777","777");
    	soccket.StartBluetoothReceiveThread();
    }
    
    // ������� �����ϸ� Ȱ�� ������ ���.
    void selectDevice() {
        // ������� ����̽��� �����ؼ� ����ϱ� ���� ���� �� �Ǿ�߸� �Ѵ�
        // getBondedDevices() : ���� ��ġ ��� ������ �Լ�.  
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();
        
        if(mPariedDeviceCount == 0 ) { // ���� ��ġ�� ���� ���. 
            Toast.makeText(getApplicationContext(), "���� ��ġ�� �����ϴ�.", Toast.LENGTH_LONG).show();
            //finish(); // App ����. 
        }
        int nTemp=0;
        for(BluetoothDevice device : mDevices) {
        	nTemp++;
        	if(device.getName().equals(shoesName))//�̰� �̸� �Ź��̸����� �ٲ�ߴ�
        	{
        		//�̹� ���� �Ź��� ���� ��� �ڵ� ����
                connectToSelectedDevice(shoesName);
                return;
        	}
        	
        	
        }
        if(nTemp==0){
        	Toast.makeText(getApplicationContext(), "���� �Ź߰� ���� ���ּ���.", Toast.LENGTH_LONG).show();
        }
        
        
        
        // ���� ��ġ�� �ִ� ���.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("������� ��ġ ����");
        
        // �� ����̽��� �̸���(���� �ٸ�) �ּҸ� ������. �� �� ����̽����� ǥ���Ѵ�. 
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            // device.getName() : �ܸ����� Bluetooth Adapter �̸��� ��ȯ. 
            listItems.add(device.getName());
        }
        listItems.add("���");  // ��� �׸� �߰�. 
        
        
        // CharSequence : ���� ������ ���ڿ�. 
        // toArray : List���·� �Ѿ�°� �迭�� �ٲ㼭 ó���ϱ� ���� toArray() �Լ�.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
          // toArray �Լ��� �̿��ؼ� size��ŭ �迭�� ���� �Ǿ���. 
          listItems.toArray(new CharSequence[listItems.size()]);
          
          builder.setItems(items, new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if(item == mPariedDeviceCount) { // ������ ��ġ�� �������� �ʰ� '���' �� ���� ���. 
                    Toast.makeText(getApplicationContext(), "������ ��ġ�� �������� �ʾҽ��ϴ�.", Toast.LENGTH_LONG).show();
                    
                }
                else { // ������ ��ġ�� ������ ���, ������ ��ġ�� ������ �õ���. 
                    connectToSelectedDevice(items[item].toString());
                }
            }
              
          });
          
          builder.setCancelable(false);  // �ڷ� ���� ��ư ��� ����. 
          AlertDialog alert = builder.create();
          alert.show();
          
    }
 
    
    void checkBluetooth() {
    	isWorkingBluetooth=true;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null ) {  // ������� ������
            Toast.makeText(getApplicationContext(), "��Ⱑ ��������� �������� �ʽ��ϴ�.", Toast.LENGTH_LONG).show();
           //finish();  // ������
        }
        else { // ������� ����
            if(!mBluetoothAdapter.isEnabled()) { // ������� �����ϸ� ��Ȱ�� ������ ���.
                Toast.makeText(getApplicationContext(), "���� ��������� ��Ȱ�� �����Դϴ�.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
                // REQUEST_ENABLE_BT : ������� Ȱ�� ������ ���� ����� App ���� �˷��� �� �ĺ��ڷ� ���(0�̻�)
              
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else // ������� �����ϸ� Ȱ�� ������ ���.
                selectDevice();
        }
    }
    
}
