package com.ssm.walkthedream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class IntroSpeech extends Activity implements OnInitListener {

	private TextToSpeech tts;

	int k = 0;
	LinearLayout ll_introspeech_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro_speech);
		tts = new TextToSpeech(this, this);

		ll_introspeech_ll = (LinearLayout) findViewById(R.id.ll_introspeech_ll);
		ll_introspeech_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				k=1;
				finish();

			}
		});

		Handler hh = new Handler();
		hh.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(k==0)
					finish();
			}
		}, 29000);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onInit(int status) {

		String myText = "안녕하세요. 워크 더 드림 앱에 온신걸 환영합니다."
				+ "이 앱은 시각장애인을 위한 네비게이션 앱 입니다. 목적지를 설정하고 길찾기를 누르면 안내가 시작됩니다."
				+ "그리고 친구 버튼을 눌러서 친구를 추가하면 친구의 실시간 위치도 확인이 가능합니다."
				+ "자주가는 길은 자주가는 길에 저장해서 편하게 사용하세요.";
		tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}

}