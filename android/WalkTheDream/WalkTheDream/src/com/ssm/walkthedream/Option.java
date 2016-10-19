package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class Option extends Activity {
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CheckBox cb4;
	CheckBox cb5;
	CSharedPreferences sharedPreferences;
	String id;
	TextView editId;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);
        sharedPreferences = new CSharedPreferences(this);
        cb1=(CheckBox)findViewById(R.id.checkBox1);
        cb2=(CheckBox)findViewById(R.id.checkBox2);
        cb3=(CheckBox)findViewById(R.id.checkBox3);
        cb4=(CheckBox)findViewById(R.id.checkBox4);
        cb5=(CheckBox)findViewById(R.id.checkBox5);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        
        Log.i("#id", id);
        Log.i("#id", id);
        Log.i("#id", Integer.toString(id.length()));
        
        editId = (TextView) findViewById(R.id.textView00);
        editId.setText(id);
        if(AndroidSocket.OPTION_TTS)
        	cb1.setChecked(true);
        if(AndroidSocket.OPTION_Bluetooth)
        	cb2.setChecked(true);
        {
        	if(AndroidSocket.OPTION_NAVI_TYPE)
        		cb4.setChecked(true);
        	else
        		cb5.setChecked(true);
        }
        int isLogin=sharedPreferences.IsLogin();
        if(isLogin==1)//이미 로그인 했다면 다음 엑티비티로 넘어간다.
        	cb3.setChecked(true);
	}
	
	@Override
    public void onDestroy()
    {
		if(cb1.isChecked())
		{
			AndroidSocket.OPTION_TTS=true;
			sharedPreferences.savePreferences("option_tts","1");
		}
		if(!cb1.isChecked())
		{
			AndroidSocket.OPTION_TTS=false;
			sharedPreferences.savePreferences("option_tts","2");
		}
		if(cb2.isChecked())
		{
			AndroidSocket.OPTION_Bluetooth=true;
			sharedPreferences.savePreferences("option_bluetooth","1");
		}
		if(!cb2.isChecked())
		{
			AndroidSocket.OPTION_Bluetooth=false;
			sharedPreferences.savePreferences("option_bluetooth","2");
		}
		if(cb3.isChecked())
		{
			sharedPreferences.savePreferences("id",id);
			sharedPreferences.savePreferences("isLogin","1");
		}
		if(!cb3.isChecked())
		{
			sharedPreferences.savePreferences("isLogin","");
		}
		if(cb4.isChecked())
		{
			sharedPreferences.savePreferences("naviType","1");
			AndroidSocket.OPTION_NAVI_TYPE=true;
		}
		if(cb5.isChecked())
		{
			sharedPreferences.savePreferences("naviType","2");
			AndroidSocket.OPTION_NAVI_TYPE=false;
		}
		
		super.onDestroy();
    }
	
	
	
	 public void onClick(View view) {//버튼 눌렀을때
	        switch (view.getId()) {
	        	case R.id.Btn1://길 찾기 버튼
	        		Intent intent = new Intent(this, IntroSpeech.class);
	        		startActivity(intent);
	        		break;
	        		
	        	case R.id.checkBox4://경로 고정 네비 사용 (권장)
	        		if(cb5.isChecked()==true)
	        			cb5.setChecked(false);
	        		else
	        			cb5.setChecked(true);
	        		break;
	        		
	        	case R.id.checkBox5://실시간 경로 갱신 네비 사용
	        		if(cb5.isChecked()==true)
	        			cb5.setChecked(false);
	        		else
	        			cb5.setChecked(true);
	        		
	        		break;
	        		
	        		
	        }
	    
	    }
}
