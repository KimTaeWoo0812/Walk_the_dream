package com.ssm.walkthedream;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TTS implements OnInitListener{
	public TextToSpeech myTTS;
	public static Context sContext;
	public TTS()
	{
		myTTS = new TextToSpeech(sContext, this);
	}
	public void onInit(int status) {
	}
    public void Destroy() {
    	myTTS.shutdown();
    }
}
