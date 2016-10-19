package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnInitListener{
    int check=0;
    CheckBox cb1;
    private AndroidSocket socket ;
    private TextToSpeech tts;
    public static int test=1;
    CSharedPreferences sharedPreferences;
    StringFilter stringFilter;
    //StartAndSocket soccket;
    AndroidSocket soccket;
    EditText editId;
    EditText editPW;
    String id;
    String pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

    	
        
    	//Intent intentIntro = new Intent(this, tmp3.class);
        //startActivity(intentIntro);
        
        //Intent intentIntro = new Intent(this, temp_Location.class);
       // startActivity(intentIntro);
        //finish();
        
        
        SC.DeviceWidth = metrics.widthPixels;
		SC.DeviceHeight= metrics.heightPixels;
        
        SavePort.port=5000;
        soccket=AndroidSocket.shared();
        //soccket=new StartAndSocket(SavePort.port);
        //soccket.start();
        sharedPreferences = new CSharedPreferences(this);
        tts=new TextToSpeech(this, this);
        
        
        
        
        //OPTION_TTS
        //OPTION_Bluetooth
    	String option_tts = sharedPreferences.getPreferences("option_tts");
    	if(option_tts.equals("2"))//"2"가 저장되어 있으면 해당 기능을 끈다.
    	{
    		AndroidSocket.OPTION_TTS=false;
    	}
    	String option_bluetooth = sharedPreferences.getPreferences("option_bluetooth");
    	if(option_bluetooth.equals("2"))//"2"가 저장되어 있으면 해당 기능을 끈다.
    	{
    		AndroidSocket.OPTION_Bluetooth=false;
    	}
    	
        int isLogin=sharedPreferences.IsLogin();
        Log.i("555","555");
        if(isLogin==1)//이미 로그인 했다면 다음 엑티비티로 넘어간다.
        {

//        	try {
//				Thread.sleep(100);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
           id = sharedPreferences.getPreferences("id");
           pw=sharedPreferences.getPreferences("pw");
           Log.i("333","333");
           char msg[]=new char[128];
           //msg=LoginDoneMsg();
           msg=SendLoginMsg();
           Log.i("12","1212");
           //아이디를 가져옴. 내부 DB에서
            //다음 엑티비티에 보낸다
               String strTemp="";
               
               for(int i=41;i<45;i++)
                  strTemp+=msg[i];
               //SavePort.port=Integer.parseInt(strTemp);

//               try {
//                soccket.CloseSocket();
//             } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//             }
               //socket=AndroidSocket.shared();
               
               soccket.id=id;

               
               Log.i("12","55454545");
               
               
               
              Intent intent5 = new Intent(this, SearchAndFriends.class);
              intent5.putExtra("id", id);
              startActivity(intent5);
              finish();//이 화면 종료
          
              
        }
        
        cb1=(CheckBox)findViewById(R.id.checkBox1);
        setStringFilter();
    }
    private void setStringFilter() 
	{
		stringFilter = new StringFilter(this);//
    	InputFilter[] allowAlphanumeric = new InputFilter[1];
    	allowAlphanumeric[0] = stringFilter.allowAlphanumeric;
    	editId = (EditText) findViewById(R.id.Text1);
        editId.setFilters(allowAlphanumeric);
    	editPW = (EditText) findViewById(R.id.Text2);
    	editPW.setFilters(allowAlphanumeric);
	}
    @Override
    public void onInit(int status) {
    	if(AndroidSocket.OPTION_TTS)
    		tts.speak("안녕하세요. 워크 더 드림에 오신걸 환영합니다.", TextToSpeech.QUEUE_FLUSH, null);
	}
    @Override
    public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
		stringFilter.tts.shutdown();
    }
    public void onClick(View view) {//버튼 눌렀을때
        switch (view.getId()) {
            case R.id.Btn1://로그인 버튼
            	tts.stop();
            	id = editId.getText().toString();
            	pw = editPW.getText().toString();
                char msg[]= new char[128];
                msg=SendLoginMsg();
               	String myText="";
                if(msg[21]=='1')//1을 받으면 로그인 성공, 아니면 실패
                {
                	if(cb1.isChecked())//내부 디비에 넣는다.
                	{
                		sharedPreferences.savePreferences("id",id);
                		sharedPreferences.savePreferences("isLogin","1");
                	}
                	///!@
                	String strTemp="";
                	
                	for(int i=41;i<45;i++)
                		strTemp+=msg[i];
                	//SavePort.port=Integer.parseInt(strTemp);

//                	try {
//						soccket.CloseSocket();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
                	//socket=AndroidSocket.shared();
            		soccket.id=id;
                	
                	
                	///
                	
                	//다음 엑티비티에 보낸다
                	Intent intent5 = new Intent(this, SearchAndFriends.class);
                	intent5.putExtra("id", id);
                	startActivity(intent5);
                	finish();//이 화면 종료
                	/*
                	try {
						soccket.CloseSocket();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	//moveTaskToBack(true); 
                	//finish();//이 화면 종료
                	//android.os.Process.killProcess(android.os.Process.myPid());//완전종료
               */
                }
                
                else if(msg[21]=='2'){
                	myText="없는 아이디 입니다.";
                	Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
                }
                else if(msg[21]=='4'){
                	myText="이미 접속한 회원 입니다.";
                	Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
                }
               	else{
               		myText="틀린 비밀번호 입니다.";
               		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
               	}
                if(AndroidSocket.OPTION_TTS)
                	tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
               	
               	break;
                
            case R.id.Btn2://회원가입
            	tts.stop();
                Intent intent2 = new Intent(this, Join_Us.class);
                startActivity(intent2);
                //finish();//이 화면 종료
                break;
        }
    }
    public char[] SendLoginMsg()
    {
    	char[] message=new char[128];
    	char[] cTemp=new char[128];
    	char[] getmessage=new char[128];
    	String strmessage;
    	int i,size;
        //아이디를 가져옴
        
        //여기에 서버로 id 비번 보내고 로그인 가능한지 받는다
        String strTemp="A_LOGIN#";
        //message=strTemp.toCharArray();
        
        for(i=0;i<strTemp.length();i++)
        	message[i]=strTemp.charAt(i);
        message[i]='#';
        
        for(i=21;i<id.length()+21;i++)
        	message[i]=id.charAt(i-21);
        message[i]='#';
        
        cTemp=pw.toCharArray();
        size=pw.length();
        for(i=41;i<pw.length()+41;i++)
        	message[i]=pw.charAt(i-41);
        message[i]='#';
        
        strmessage="";
        for(i=0;i<128;i++)
        	strmessage+=message[i];
        soccket.SendMessage(strmessage);//로그인 정보 전송
        while(soccket.HasMessage());
        	getmessage=soccket.GetMessage();//서버로부터 받기
    	return getmessage;
    }
    public char[] LoginDoneMsg()
    {
    	char[] message=new char[128];
    	char[] cTemp=new char[128];
    	char[] getmessage=new char[128];
    	String strmessage;
    	int i,size;
        //아이디를 가져옴
        //여기에 서버로 id 비번 보내고 로그인 가능한지 받는다
        String strTemp="A_ALOGIN#";
        //message=strTemp.toCharArray();
        
        for(i=0;i<strTemp.length();i++)
        	message[i]=strTemp.charAt(i);
        message[i]='#';
        
        for(i=21;i<id.length()+21;i++)
        	message[i]=id.charAt(i-21);
        message[i]='#';
        
        strmessage="";
        for(i=0;i<128;i++)
        	strmessage+=message[i];

        soccket.SendMessage(strmessage);//로그인 정보 전송
        while(soccket.HasMessage());
        	getmessage=soccket.GetMessage();//서버로부터 받기
    	return getmessage;
    }
}