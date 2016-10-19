package com.ssm.walkthedream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsManage extends Activity implements OnClickListener,
		OnInitListener {
	private AndroidSocket soccket;
	String friendId[] = new String[20];
	char friendIsLogin[] = new char[50];
	MyLocationListener gps;
	private String id;
	int out = 0;
	TextToSpeech tts;

	@Override
	public void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}

	@Override
	public void onInit(int status) {
		String myText = "친구 추가, 삭제, 위치보기를 할 수 있습니다.";
		if(AndroidSocket.OPTION_TTS)
			tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
	}

	
	
	@Override
	protected void onPause() {
	
		gps.endGps();
		
		super.onPause();
	}

	
	
	@Override
	protected void onResume() {

		
		super.onResume();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends_manage);

		
		
		soccket = AndroidSocket.shared();
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tts = new TextToSpeech(this, this);
		SetFriends();
		
		gps = new MyLocationListener(this);
		
	}

	/*
	 * public void onResume() { soccket.SendMessage("SetFriends 안" );//
	 * super.onResume(); SetFriends(); }
	 */

	// A_SET_FRIENDS
	public void SetFriends() {
		char[] message = new char[128];
		char[] getMessage = new char[128];
		String strMessage = "A_SET_FRIENDS#";
		int i;
		for (i = 0; i < strMessage.length(); i++)
			message[i] = strMessage.charAt(i);
		for (i = 21; i < id.length() + 21; i++)
			message[i] = id.charAt(i - 21);
		message[i] = '#';

		strMessage = "";
		for (i = 0; i < 60; i++)
			strMessage += message[i];
		// A_SET_FRIENDS
		soccket.SendMessage(strMessage);// 친구목록을 달라고 요구한다

		LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout1);
		LinearLayout arrLinear[] = new LinearLayout[30];
		TextView textView[] = new TextView[30];
		Button locationBtn[] = new Button[30];
		Button deleteBtn[] = new Button[30];

		out = 0;
		for (;; out++) {
			while (soccket.HasMessage())
				;
			getMessage = soccket.GetMessage();// 서버로부터 받기

			if (getMessage[0] != '0') {
				friendId[out] = "";
				for (i = 0; getMessage[i] != '#'; i++)
					friendId[out] += getMessage[i];
				for (i = 20; getMessage[i] != '#'; i++)
					friendIsLogin[out] = getMessage[i];

				// soccket.SendMessage(out+". 친구 아이디: "+friendId[out]+" 친구 접속여부 "+friendIsLogin[out]);//친구목록을
				// 달라고 요구한다
			} else {
				break;
			}
			
			
			
			arrLinear[out] = new LinearLayout(this);
			arrLinear[out].setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			arrLinear[out].setOrientation(LinearLayout.HORIZONTAL);
			arrLinear[out].setPadding(5, 5, 5, 0);
			arrLinear[out].setGravity(Gravity.CENTER_VERTICAL);
			
			linear.addView(arrLinear[out]);

			textView[out] = new TextView(this);
			textView[out].setText(friendId[out]);
			textView[out].setWidth(SC.DeviceWidth/3 );
			textView[out].setTextSize(18);
			textView[out].setId(200 + out);
			textView[out].setTextColor(Color.parseColor("#000000"));
			
			locationBtn[out] = new Button(this);
			locationBtn[out].setText("위치보기");
			locationBtn[out].setWidth(SC.DeviceWidth/3);
			locationBtn[out].setTextSize(11);
			locationBtn[out].setTextColor(Color.parseColor("#ffffff"));
			locationBtn[out].setId(out);
			locationBtn[out].setOnClickListener(this);
			locationBtn[out].setBackgroundColor(Color.parseColor("#00BAFF"));
			locationBtn[out].setTextSize(17);

			

			
			deleteBtn[out] = new Button(this);
			deleteBtn[out].setText("삭제");
			deleteBtn[out].setWidth(SC.DeviceWidth/3);
			deleteBtn[out].setTextSize(10);
			deleteBtn[out].setId(100 + out);

			deleteBtn[out].setOnClickListener(this);
			deleteBtn[out].setTextColor(Color.parseColor("#ffffff"));
			deleteBtn[out].setBackgroundColor(Color.parseColor("#00BAFF"));
			deleteBtn[out].setTextSize(17);
			
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
			param.leftMargin= 5;
			deleteBtn[out].setLayoutParams(param); 
			

			arrLinear[out].addView(textView[out]);
			arrLinear[out].addView(locationBtn[out]);
			arrLinear[out].addView(deleteBtn[out]);

			if (friendIsLogin[out] != '1')
			{
				locationBtn[out].setEnabled(false);
				locationBtn[out].setVisibility(View.INVISIBLE);
			}

		}
	}

	// A_LOCATION
	public void SendLocationMsg(String friendid) {
		soccket.latitude = AndroidSocket.latitude;
		soccket.longitude = AndroidSocket.longitude;
		char[] getMessage = new char[128];
		char message[] = new char[128];
		String strTemp = "A_LOCATION";
		int i;
		for (i = 0; i < strTemp.length(); i++)
			message[i] = strTemp.charAt(i);

		for (i = 21; i < friendid.length() + 21; i++)
			message[i] = friendid.charAt(i - 21);
		message[i] = '#';
		for (i = 41; i < id.length() + 41; i++)
			message[i] = id.charAt(i - 41);
		message[i] = '#';
		strTemp = "";
		for (i = 0; i < 60; i++)
			strTemp += message[i];

		soccket.SendMessage(strTemp);//

		while (soccket.HasMessage())
			;
		getMessage = soccket.GetMessage();// 서버로부터 받기

		if (getMessage[21] == '0') {
			Toast.makeText(this, "현재 접속 중이지 않은 친구입니다!", Toast.LENGTH_SHORT).show();
			if(AndroidSocket.OPTION_TTS)
				tts.speak("현재 접속 중이지 않은 친구입니다!", TextToSpeech.QUEUE_FLUSH, null);
			
			return;
		}

		Intent intent = new Intent(this, ViewMap.class);
		startActivity(intent);

	}

	// A_DELETEFRIEND
	public void SendDeleteMsg(String friendid) {
		char[] getMessage = new char[128];
		char message[] = new char[128];
		String strTemp = "A_DELETEFRIEND";
		int i;
		for (i = 0; i < strTemp.length(); i++)
			message[i] = strTemp.charAt(i);
		for (i = 21; i < friendid.length() + 21; i++)
			message[i] = friendid.charAt(i - 21);
		message[i] = '#';
		for (i = 41; i < id.length() + 41; i++)
			message[i] = id.charAt(i - 41);
		message[i] = '#';
		strTemp = "";
		for (i = 0; i < 60; i++)
			strTemp += message[i];
		soccket.SendMessage(strTemp);//
		String myText;
		while (soccket.HasMessage())
			;
		getMessage = soccket.GetMessage();// 서버로부터 받기

		if (getMessage[21] == '1') {
			Toast.makeText(this, "삭제 성공!", Toast.LENGTH_SHORT).show();
			myText = friendid + " 삭제 성공.";
			if(AndroidSocket.OPTION_TTS)
				tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
		} else {
			Toast.makeText(this, "삭제 실패!", Toast.LENGTH_SHORT).show();
			myText = friendid + " 삭제 실패.";
			if(AndroidSocket.OPTION_TTS)
				tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
		}

	}

	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.Btn00:// 친구추가
			tts.stop();
			Intent intent3 = new Intent(this, AddFriend.class);
			intent3.putExtra("id", id);
			startActivity(intent3);
			this.finish();// 이 화면 종료
			return;
		}
		// soccket.SendMessage("qwetyuiuytrew");//
		// soccket.SendMessage(Integer.toString(view.getId()));//
		// soccket.SendMessage(friendId[view.getId()]);//
		if (100 > view.getId())// 위치보기
		{
			SendLocationMsg(friendId[view.getId()]);
		} else {// 삭제
			SendDeleteMsg(friendId[view.getId() - 100]);
			Reset();

		}
		// 위치보기: 0~
		// 삭제: 100~
		// (view.getId())
	}

	public void Reset() {
		Intent intent1 = new Intent(this, FriendsManage.class);
		intent1.putExtra("id", id);
		startActivity(intent1);
		finish();// 이 화면 종료
	}
}
