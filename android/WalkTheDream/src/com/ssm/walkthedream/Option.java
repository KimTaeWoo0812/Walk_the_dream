package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;


public class Option extends Activity {
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CSharedPreferences sharedPreferences;
	String id;
	EditText editId;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);
        sharedPreferences = new CSharedPreferences(this);
        cb1=(CheckBox)findViewById(R.id.checkBox1);
        cb2=(CheckBox)findViewById(R.id.checkBox2);
        cb3=(CheckBox)findViewById(R.id.checkBox3);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        editId = (EditText) findViewById(R.id.Text1);
        editId.setText(id);
        if(AndroidSocket.OPTION_TTS)
        	cb1.setChecked(true);
        if(AndroidSocket.OPTION_Bluetooth)
        	cb2.setChecked(true);
        
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
		
		super.onDestroy();
    }
}
