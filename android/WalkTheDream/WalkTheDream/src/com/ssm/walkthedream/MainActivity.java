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
    	if(option_tts.equals("2"))//"2"�� ����Ǿ� ������ �ش� ����� ����.
    	{
    		AndroidSocket.OPTION_TTS=false;
    	}
    	String option_bluetooth = sharedPreferences.getPreferences("option_bluetooth");
    	if(option_bluetooth.equals("2"))//"2"�� ����Ǿ� ������ �ش� ����� ����.
    	{
    		AndroidSocket.OPTION_Bluetooth=false;
    	}
    	
        int isLogin=sharedPreferences.IsLogin();
        Log.i("555","555");
        if(isLogin==1)//�̹� �α��� �ߴٸ� ���� ��Ƽ��Ƽ�� �Ѿ��.
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
           //���̵� ������. ���� DB����
            //���� ��Ƽ��Ƽ�� ������
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
              finish();//�� ȭ�� ����
          
              
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
    		tts.speak("�ȳ��ϼ���. ��ũ �� �帲�� ���Ű� ȯ���մϴ�.", TextToSpeech.QUEUE_FLUSH, null);
	}
    @Override
    public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
		stringFilter.tts.shutdown();
    }
    public void onClick(View view) {//��ư ��������
        switch (view.getId()) {
            case R.id.Btn1://�α��� ��ư
            	tts.stop();
            	id = editId.getText().toString();
            	pw = editPW.getText().toString();
                char msg[]= new char[128];
                msg=SendLoginMsg();
               	String myText="";
                if(msg[21]=='1')//1�� ������ �α��� ����, �ƴϸ� ����
                {
                	if(cb1.isChecked())//���� ��� �ִ´�.
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
                	
                	//���� ��Ƽ��Ƽ�� ������
                	Intent intent5 = new Intent(this, SearchAndFriends.class);
                	intent5.putExtra("id", id);
                	startActivity(intent5);
                	finish();//�� ȭ�� ����
                	/*
                	try {
						soccket.CloseSocket();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	//moveTaskToBack(true); 
                	//finish();//�� ȭ�� ����
                	//android.os.Process.killProcess(android.os.Process.myPid());//��������
               */
                }
                
                else if(msg[21]=='2'){
                	myText="���� ���̵� �Դϴ�.";
                	Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
                }
                else if(msg[21]=='4'){
                	myText="�̹� ������ ȸ�� �Դϴ�.";
                	Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
                }
               	else{
               		myText="Ʋ�� ��й�ȣ �Դϴ�.";
               		Toast.makeText(this, myText, Toast.LENGTH_SHORT).show();
               	}
                if(AndroidSocket.OPTION_TTS)
                	tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
               	
               	break;
                
            case R.id.Btn2://ȸ������
            	tts.stop();
                Intent intent2 = new Intent(this, Join_Us.class);
                startActivity(intent2);
                //finish();//�� ȭ�� ����
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
        //���̵� ������
        
        //���⿡ ������ id ��� ������ �α��� �������� �޴´�
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
        soccket.SendMessage(strmessage);//�α��� ���� ����
        while(soccket.HasMessage());
        	getmessage=soccket.GetMessage();//�����κ��� �ޱ�
    	return getmessage;
    }
    public char[] LoginDoneMsg()
    {
    	char[] message=new char[128];
    	char[] cTemp=new char[128];
    	char[] getmessage=new char[128];
    	String strmessage;
    	int i,size;
        //���̵� ������
        //���⿡ ������ id ��� ������ �α��� �������� �޴´�
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

        soccket.SendMessage(strmessage);//�α��� ���� ����
        while(soccket.HasMessage());
        	getmessage=soccket.GetMessage();//�����κ��� �ޱ�
    	return getmessage;
    }
}