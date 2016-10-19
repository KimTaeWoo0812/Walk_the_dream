package com.ssm.walkthedream;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Join_Us extends Activity implements OnInitListener{
	private AndroidSocket soccket ;
	int isChecked=0;
	char[] message=new char[128];
	String strTemp;
	String myText;
	int i;
	TextToSpeech tts;
	EditText checkId;
	EditText editPW;
	StringFilter stringFilter;
	String id;
	@Override
	protected void onResume() {
		 super.onResume();
	}
	@Override
    public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
		stringFilter.tts.shutdown();
    }
	@Override
	public void onInit(int status) {
		myText = "ȸ������ȭ�� �Դϴ�. ���̵�� ��й�ȣ�� �Է��ϼ���.";
		if(AndroidSocket.OPTION_TTS)
			tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
	}
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_us);
        tts = new TextToSpeech(this, this);

        soccket=AndroidSocket.shared();
        setStringFilter();
        
    	
	}
	private void setStringFilter() 
	{
		stringFilter = new StringFilter(this);//
    	InputFilter[] allowAlphanumeric = new InputFilter[1];
    	allowAlphanumeric[0] = stringFilter.allowAlphanumeric;
    	checkId = (EditText) findViewById(R.id.Text1);
    	checkId.setFilters(allowAlphanumeric);
    	editPW = (EditText) findViewById(R.id.Text2);
    	editPW.setFilters(allowAlphanumeric);
	}
	
	 public void onClick(View view) {//��ư ��������
	        switch (view.getId()) {
	            case R.id.Btn1://�ߺ�Ȯ��
	            	id = checkId.getText().toString();
	            	//soccket.SendMessage("���̵� ����   "+checkId.length());//
	            	
	                if(checkId.length()<4||checkId.length()>19)
	                {
	                	Toast.makeText(this, "���̵�� 4�� �̻�, 19�� �̸��Դϴ�!", Toast.LENGTH_SHORT).show();
	                	myText = "���̵�� ���� �̻�, 19�� �̸��Դϴ�!";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                	break;
	                }
	                strTemp="A_IDCHECK";
	                
	                for(i=0;i<9;i++)
	                	message[i]=strTemp.charAt(i);
	                message[i]='#';
	                for(i=21;i<id.length()+21;i++)
	                	message[i]=id.charAt(i-21);
	                message[i]='#';
	                strTemp="";
	                for(i=0;i<128;i++)
	                	strTemp+=message[i];
	                
	                soccket.SendMessage(strTemp);
	                while(soccket.HasMessage());
	                	message=soccket.GetMessage();//�����κ��� �ޱ�
	                	
	                if(message[21]=='1')
	                {
	                	isChecked=1;
	                	Toast.makeText(this, "���̵� ��� ����!", Toast.LENGTH_SHORT).show();
	                	myText = "�ߺ�Ȯ�� �Ϸ�";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                }
	                else
	                {
	                	isChecked=0;
	                	Toast.makeText(this, "�̹� �ִ� ���̵� �Դϴ�!", Toast.LENGTH_SHORT).show();
	                	myText = "�̹� �ִ� ���̵� �Դϴ�!";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                }
	            	break;
	            	
	            case R.id.Btn2://����
	                String PW = editPW.getText().toString();
	                EditText editPWckeck = (EditText) findViewById(R.id.Text3);
	                String PWcheck = editPWckeck.getText().toString();
	                if(isChecked==0)
	                {
	                	Toast.makeText(this, "���̵� �ߺ�Ȯ���� ���ּ���!", Toast.LENGTH_SHORT).show();
	                	myText = "���̵� �ߺ�Ȯ���� ���ּ���!";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                	break;
	                }
	                if(editPW.length()<4||editPW.length()>19)
	                {
	                	Toast.makeText(this, "��й�ȣ�� ���� �̻�, 19�� �̸��Դϴ�!", Toast.LENGTH_SHORT).show();
	                	myText = "��й�ȣ�� ���� �̻�, 19�� �̸��Դϴ�!";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                	break;
	                }
	                if(!PW.equals(PWcheck))
	                {
	                	Toast.makeText(this, "�Է��� ��й�ȣ�� ���� �ʽ��ϴ�!", Toast.LENGTH_SHORT).show();
	                	myText = "�Է��� ��й�ȣ�� ���� �ʽ��ϴ�!";
	                	if(AndroidSocket.OPTION_TTS)
	                		tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	                	break;
	                }
	               
	                
	                strTemp="A_JOIN_US";
	                
	                for(i=0;i<9;i++)
	                	message[i]=strTemp.charAt(i);
	                message[i]='#';
	                for(i=21;i<id.length()+21;i++)
	                	message[i]=id.charAt(i-21);
	                message[i]='#';
	                for(i=41;i<PW.length()+41;i++)
	                	message[i]=PW.charAt(i-41);
	                message[i]='#';
	                
	                strTemp="";
	                for(i=0;i<128;i++)
	                	strTemp+=message[i];
	                soccket.SendMessage(strTemp);
	                while(soccket.HasMessage());
                		message=soccket.GetMessage();//�����κ��� �ޱ�
	                
                	if(message[21]=='1')
                	{
                		myText = "ȸ������ ����!";
                		Toast.makeText(this,myText, Toast.LENGTH_SHORT).show();
                		if(AndroidSocket.OPTION_TTS)
                			tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
                		long saveTime = System.currentTimeMillis();
        		        long currTime = 0;
        		       while( currTime - saveTime > 2000){
        		            currTime = System.currentTimeMillis();
        		            soccket.rear++;
        		       }
                		finish();//�� ȭ�� ����
                		break;
                	}
                	if(message[21]!='1')
                	{
                		myText = "ȸ������ ����! �ٽ� �õ��� �ּ���.";
                		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
                		if(AndroidSocket.OPTION_TTS)
                			tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
                		checkId=null;
                		editPW=null;
                		editPWckeck=null;
                		isChecked=0;
                	}
                	
	            	break;
	            	
	            case R.id.Btn3://���
	            	finish();//�� ȭ�� ����
	            	break;
	        }
	 }
}
