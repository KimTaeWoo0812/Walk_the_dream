package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriend extends Activity implements OnInitListener{
	private AndroidSocket soccket ;
	String myText;
	TextToSpeech tts;
	private String id;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);
        soccket=AndroidSocket.shared();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        tts = new TextToSpeech(this, this);
	}
	public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
    }
	@Override
	public void onInit(int status) {
	}
	public void onClick(View view) {//버튼 눌렀을때
        switch (view.getId()) {
            case R.id.Btn1://로그인 버튼
            	EditText edit = (EditText) findViewById(R.id.Text1);
            	String strEdit = edit.getText().toString();
            	String strTemp="A_ADDFRIEND#";
            	String strMessage="";
            	char cMsg[]=new char[128];
            	int i;
            	
            	if(strEdit.length()>15)
            	{
            		myText= "아이디는 네자 이상, 19자 미만입니다!";
            		Toast.makeText(this,myText, Toast.LENGTH_SHORT).show();
            		if(AndroidSocket.OPTION_TTS)
						tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
            		break;
            	}
            	
            	for(i=0;i<strTemp.length();i++)//A_ADDFRIEND
            		cMsg[i]=strTemp.charAt(i);
            	for(i=21;i<strEdit.length()+21;i++)//친구 id
            		cMsg[i]=strEdit.charAt(i-21);
            	cMsg[i]='#';
            	
            	for(i=41;i<id.length()+41;i++)//내 id
            		cMsg[i]=id.charAt(i-41);
            	cMsg[i]='#';
            	for(i=0;i<60;i++)
            		strMessage+=cMsg[i];
            	cMsg[i]='#';
            	soccket.SendMessage(strMessage);//
            	char[] getMessage=new char[128];
            	
            	while(soccket.HasMessage());
            		getMessage=soccket.GetMessage();//서버로부터 받기
    		 	
    		 	if(getMessage[21]=='1')
    		 	{
    		 		myText="추가 성공!";
            		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
            		if(AndroidSocket.OPTION_TTS)
						tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
            		
            		long saveTime = System.currentTimeMillis();
    		        long currTime = 0;

    		        while( currTime - saveTime > 2000){
    		        	currTime = System.currentTimeMillis();
    		        }
            		
            		Intent intent1 = new Intent(this, FriendsManage.class);
                 	intent1.putExtra("id", id);
                 	startActivity(intent1);
                 	finish();//이 화면 종료
    		 	}
            	else if(getMessage[21]=='3'){
            		myText="없는 아이디 입니다.";
            		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
            	}
            	
            	else{
            		myText="추가 실패!";
            		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
            	}
    			if(AndroidSocket.OPTION_TTS)
					tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
        }
	}
}
