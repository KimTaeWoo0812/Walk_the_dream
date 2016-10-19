package com.ssm.walkthedream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ssm.walkthedream.SearchDestination.oneOfList;


public class AddWay extends Activity implements OnInitListener, OnItemClickListener{
	private AndroidSocket soccket;
	TextToSpeech tts;

	ListView lv_addway_lv;
	private String id;
	private String strEdit;
	String tmpName;
    String tmpX;
    String tmpY;
    

	ArrayList<String> al = new ArrayList<String>();
	
	@Override
    public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
    }
	
	
	@Override
	public void onInit(int status) {
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_way);
		soccket = AndroidSocket.shared();
		
		lv_addway_lv = (ListView)findViewById(R.id.lv_addway_lv);
		
		lv_addway_lv.setOnItemClickListener(this);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tts = new TextToSpeech(this, this);
	}

	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.Btn1:// 친구 추가
			EditText edit = (EditText) findViewById(R.id.Text1);
			strEdit = edit.getText().toString();
			
			String str1 = strEdit;
			
			
			String str1_1 = "";
			try {
				str1_1 = URLEncoder.encode(str1, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
			String str2 = "http://map.naver.com/search2/local.nhn?sm=hty&searchCoord=128.3784%3B36.1376997&isFirstSearch=true&query="
					+ str1_1
					+ "&menu=location&mpx=04190690%3A36.1376997%2C128.3784%3AZ11%3A0.0336476%2C0.0065756";

			(new ParseURL()).execute(new String[] { "LIST", str2 });

			
		}
	}
	//보내기
	public void SendMgsToServer(String name, double x,double y)
    {
       String strX="";
       String strY="";
       
       strX= Double.toString(x);
       strY= Double.toString(y);
       
       {
          if(strX.equals("0"))
          {
             String myText="검색할수 없는 단어입니다.";
             Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
             if(AndroidSocket.OPTION_TTS)
                tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
             return;
          }
       }
       String strTemp = "A_ADDWAY#";
       String strMessage = "";
       char cMsg[] = new char[128];
       int i;
       // A_ADDWAY
       for (i = 0; i < strTemp.length(); i++)
          cMsg[i] = strTemp.charAt(i);
       // 내 id
       for (i = 21; i < id.length() + 21; i++)
          cMsg[i] = id.charAt(i - 21);
       cMsg[i] = '#';
       // 목적지
       for (i = 41; i < name.length() + 41; i++)
          cMsg[i] = name.charAt(i - 41);
       cMsg[i] = '#';
       
       // 좌표
       for (i = 61; i < strX.length() + 61; i++)
          cMsg[i] = strX.charAt(i - 61);
       cMsg[i] = '#';
       
       // 좌표
       for (i = 81; i < strY.length() + 81; i++)
          cMsg[i] = strY.charAt(i - 81);
       cMsg[i] = '#';
       
       
       for (i = 0; i < 128; i++)
          strMessage += cMsg[i];

       soccket.SendMessage(strMessage);//
       char[] getMessage = new char[128];

       while (soccket.HasMessage());
       getMessage = soccket.GetMessage();// 서버로부터 받기
       String myText="";
       if (getMessage[21] == '1') {
          myText="추가 성공";
          if(AndroidSocket.OPTION_TTS)
             tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
          Toast.makeText(this, myText+"!", Toast.LENGTH_SHORT).show();
          long saveTime = System.currentTimeMillis();
            long currTime = 0;

            while( currTime - saveTime > 2000){
               currTime = System.currentTimeMillis();
            }
          Intent intent1 = new Intent(this, OftenWay.class);
          intent1.putExtra("id", id);
          startActivity(intent1);
          finish();// 이 화면 종료
       } else if (getMessage[21] == '3'){
          myText="저장 실패";
          if(AndroidSocket.OPTION_TTS)
             tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
          Toast.makeText(this, myText+"!", Toast.LENGTH_SHORT).show();
       }
       else{
          myText="추가 실패";
          if(AndroidSocket.OPTION_TTS)
             tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
          Toast.makeText(this, myText+"!", Toast.LENGTH_SHORT).show();
       }
    }
		

		ArrayList<oneOfList2> ool = new ArrayList<oneOfList2>();
		
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
			            
	//		            Log.i("BODY ",body);
	
			            body = body.replace("\n", "");
			            body = body.replace("\t", "");
			            
			            String [] names = body.split("\"name\": \"");
			            String [] xs = body.split("\"x\": \"");
			            String [] ys = body.split("\"y\": \"");
	
			            String [] address = body.split("\"address\": \"");
			            tmpName="NULL";
			            
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
				               ool.add(new oneOfList2(tmpName, Double.parseDouble(tmpX), Double.parseDouble(tmpY),tmpAddress));
							
						}
						
	
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

				// 어댑터 준비
				ArrayAdapter<String> Adapter;
				Adapter = new ArrayAdapter<String>(AddWay.this,
						android.R.layout.simple_list_item_1, al);
				// 어댑터 연결
				lv_addway_lv.setAdapter(Adapter);

			}  
			
			
			
			
			
			
//			if(tmpName.compareTo("NULL")==0)
//			{
//				tmpX="0";
//				SendMgsToServer();
//			}
//			else
//			{
//				// tmpX tmpY
//				SendMgsToServer();
//				
//			}
			
		}

	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		
		SendMgsToServer(ool.get(pos).name, ool.get(pos).x,ool.get(pos).y);
		
		
		
	}
	
	class oneOfList2
	{
		public String name;
	      public double x;
	      public double y;
	      public String address;
	      
	      public oneOfList2(String name_, double x_, double y_,String address_) {
	         name = name_;
	         x=x_;
	         y=y_;
	         address = address_;
	         
	      }
		
		
	}
	
	
}