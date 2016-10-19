package com.ssm.walkthedream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchDestination extends Activity implements OnInitListener,
		OnClickListener, OnItemClickListener {
	private AndroidSocket soccket ;
	private boolean stopThread=true;
	Handler hStairs;
	ListView lv_searchdestination_lv;
	Button bt_searchdestination_searchvoice;
	Button bt_searchdestination_search;
	EditText et_searchdestination_getText;

	String lat;
	String lng;

	String selectedLocation;

	ArrayList<String> al = new ArrayList<String>();

	TextToSpeech tts;

	private final int GOOGLE_STT = 1000, MY_UI = 1001; // requestCode. ���������ν�
	private ArrayList<String> mResult; // �����ν� ��� ������ list
	private String mSelectedString; // ��� list �� ����ڰ� ������ �ؽ�Ʈ

	Intent ii;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		tts.shutdown();
		stopThread=false;
		
	}

	@Override
	public void onInit(int status) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // ȭ���׻���
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_destination);
		soccket=AndroidSocket.shared();
		lv_searchdestination_lv = (ListView) findViewById(R.id.lv_searchdestination_lv);
		bt_searchdestination_searchvoice = (Button) findViewById(R.id.bt_searchdestination_searchvoice);
		bt_searchdestination_search = (Button) findViewById(R.id.bt_searchdestination_search);
		et_searchdestination_getText = (EditText) findViewById(R.id.et_searchdestination_getText);

		bt_searchdestination_search.setOnClickListener(this);
		bt_searchdestination_searchvoice.setOnClickListener(this);
		
		lv_searchdestination_lv.setOnItemClickListener(this);

		tts = new TextToSpeech(this, this);
		
		 ii = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			//intent ����
		 ii.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	//�����ν��� ȣ���� ��Ű��
		 ii.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							//�����ν� ��� ����
		 ii.putExtra(RecognizerIntent.EXTRA_PROMPT, "���� �ϼ���.");						//����ڿ��� ���� �� ����
		
		 if(AndroidSocket.OPTION_Bluetooth)
		 {
			 //hStairs = new Handler();

			 //StairsTTSThread stairsTTS=new StairsTTSThread();
			//stairsTTS.start();
			 
		 }
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bt_searchdestination_search) {
			String str1 = et_searchdestination_getText.getText().toString();

			String str1_1 = "";
			try {
				str1_1 = URLEncoder.encode(str1, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}

			String str2 = "http://map.naver.com/search2/local.nhn?sm=hty&searchCoord=128.3784%3B36.1376997&isFirstSearch=true&query="
					+ str1_1
					+ "&menu=location&mpx=04190690%3A36.1376997%2C128.3784%3AZ11%3A0.0336476%2C0.0065756";

			(new ParseURL()).execute(new String[] { "LIST", str2 });

		} else if (v.getId() == R.id.bt_searchdestination_searchvoice) {
			//#
			startActivityForResult(ii, GOOGLE_STT);
			
		}

	}

	ArrayList<oneOfList> ool = new ArrayList<oneOfList>();
	
	public class ParseURL extends AsyncTask<String, Void, String> {
		String retStr = "";
		String rstType;

		@Override
		protected String doInBackground(String... strings) {
			String body = "";

			try {
				
		
				
				rstType = strings[0];
				if (rstType.compareTo("LIST") == 0) {
					try {
						body = (Jsoup.connect(strings[1]).timeout(20000)
								.ignoreContentType(true).execute().body());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
	//				Log.i("BODY ",body);
	
					body = body.replace("\n", "");
					body = body.replace("\t", "");
					
					String [] names = body.split("\"name\": \"");
					String [] xs = body.split("\"x\": \"");
					String [] ys = body.split("\"y\": \"");
	
		            String [] address = body.split("\"address\": \"");
					String tmpName;
					String tmpX;
					String tmpY;
	
		            String tmpAddress;
					ool.clear();
					
					for(int i = 1 ; i < names.length ; i++)
					{
						
						tmpName  = names[i].substring(0, names[i].indexOf("\"") );
						tmpX  = xs[i].substring(0, xs[i].indexOf("\"") );
						tmpY = ys[i].substring(0, ys[i].indexOf("\"") );
						 tmpAddress = address[i].substring(0,address[i].indexOf("\""));
			               ool.add(new oneOfList(tmpName, Double.parseDouble(tmpX), Double.parseDouble(tmpY),tmpAddress));
			               
					}
					
					
					
				} else if (rstType.compareTo("GPS") == 0) {
					try {
						
						
						Log.i("ASDASD",strings[1]);
						
						body = (Jsoup.connect(strings[1]).timeout(20000)
								.ignoreContentType(true).execute().body());
					} catch (IOException e) {
						e.printStackTrace();
					}
	
					Log.i("TMPTMPTMP",body);
	
					
					String[] str5 = body.split("location");
	
					
					String str6 = str5[1].replace(" ", "");
	
					System.out.println(str6);
	
					lat = (str6.substring(str6.indexOf("lat") + 5,
							str6.indexOf(",")));
	
					lng = (str6.substring(str6.indexOf("lng") + 5,
							str6.indexOf("}") - 1));
	
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;

		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);

			if (rstType.compareTo("LIST") == 0) {

				al.clear();

				for (int i = 0; i < ool.size(); i++) {

					al.add(ool.get(i).name+"\n"+ool.get(i).address);

				}

				// ����� �غ�
				ArrayAdapter<String> Adapter;
				Adapter = new ArrayAdapter<String>(SearchDestination.this,
						android.R.layout.simple_list_item_1, al);
				// ����� ����
				lv_searchdestination_lv.setAdapter(Adapter);

			}  
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LaunchNavi(position);

	}

	void LaunchNavi(int pos) {
		
		//##
		//if(soccket.OPTION_NAVI_TYPE)
		//{
		Intent i = new Intent(SearchDestination.this ,
				 NewNavi.class);
				 i.putExtra("str", ool.get(pos).name);
				 i.putExtra("lat", ool.get(pos).x);
				 i.putExtra("lng", ool.get(pos).y);
				
				 startActivity(i);
		//}
		/*
		else
		{
			Intent i = new Intent(SearchDestination.this ,
					 Navi.class);
					 i.putExtra("str", ool.get(pos).name);
					 i.putExtra("lat", ool.get(pos).x);
					 i.putExtra("lng", ool.get(pos).y);
					
					 startActivity(i);
		}*/
				 
	}

	class oneOfList
	{
		public String name;
	      public double x;
	      public double y;
	      public String address;
	      
	      public oneOfList(String name_, double x_, double y_,String address_) {
	         name = name_;
	         x=x_;
	         y=y_;
	         address = address_;
	         
	      }
		
		
	}
	
	
	
	
	
	
	
	
	
	
	// STT �Լ�

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
										et_searchdestination_getText.setText(mSelectedString);		//Ȯ�� ��ư ������ ��� ���
										
										
										
										
										
										String str1 = mSelectedString;
										String str1_1 = "";
										try {
											str1_1 = URLEncoder.encode(str1, "UTF-8");
										} catch (UnsupportedEncodingException e) {
										}

										String str2 = "http://map.naver.com/search2/local.nhn?sm=hty&searchCoord=128.3784%3B36.1376997&isFirstSearch=true&query="
												+ str1_1
												+ "&menu=location&mpx=04190690%3A36.1376997%2C128.3784%3AZ11%3A0.0336476%2C0.0065756";

										(new ParseURL()).execute(new String[] { "LIST", str2 });
										
										
										
										
										
										
										
										
										String myText = mSelectedString+"�� �˻��մϴ�.";
										if(AndroidSocket.OPTION_TTS){
											tts.stop();
											tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
										}
									}
								})
								.setNegativeButton("���", new DialogInterface.OnClickListener() {
									@Override public void onClick(DialogInterface dialog, int which) {
										et_searchdestination_getText.setText("");		//��ҹ�ư ������ �ʱ�ȭ
										mSelectedString = null;
									}
								}).create();
			ad.show();
		}

		
		//������� �޴°�
		public class StairsTTSThread extends Thread{

			public void run(){
				for(;stopThread;)
				 {
					 try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				       	if(soccket.hTop!=soccket.hRear)
				       	{
				       		hStairs.post(new Runnable() {
				       			String myText;
				       			@Override
				       			public void run() {
				       				switch(soccket.hQueue[soccket.hRear%20])
				       				{
				       				case 10:
				       					myText = "���濡 �ö󰡴� ����� �ֽ��ϴ�.";
				       					break;
				       				case 11:
				       					myText = "���濡 �������� ����� �ֽ��ϴ�.";
				       					break;
				       				case 1:
				       					//myText = "�ö󰡴� ����� �����Ͽ����ϴ�.";
				       					break;
				       				case 2:
				       					//myText = "�������� ����� �����Ͽ����ϴ�.";
				       					break;
				       				}
//				       				if(AndroidSocket.OPTION_TTS)
				       				if(false)
				       					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
				       				soccket.hRear++;
				       			}
				       		});
				       	}
				 }
		
			}
		}
}//et_searchdestination_getText
