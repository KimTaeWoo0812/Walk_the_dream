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

		String myText = "�ȳ��ϼ���. ��ũ �� �帲 �ۿ� �½Ű� ȯ���մϴ�."
				+ "�� ���� �ð�������� ���� �׺���̼� �� �Դϴ�. �������� �����ϰ� ��ã�⸦ ������ �ȳ��� ���۵˴ϴ�."
				+ "�׸��� ģ�� ��ư�� ������ ģ���� �߰��ϸ� ģ���� �ǽð� ��ġ�� Ȯ���� �����մϴ�."
				+ "���ְ��� ���� ���ְ��� �濡 �����ؼ� ���ϰ� ����ϼ���.";
		tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}

}