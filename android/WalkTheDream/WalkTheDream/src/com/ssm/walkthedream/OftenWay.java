package com.ssm.walkthedream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//������ �״�� ���� ��, ���� �� �����ؾߴ�
//�� ����, ����, �߰� ��ư ���� ��
public class OftenWay extends Activity implements OnInitListener, android.view.View.OnClickListener{
	private AndroidSocket soccket ;
	String WayName[]=new String[20];
	String lat[]=new String[20];
	String lon[]=new String[20];
	private String id;
	int out=0;
	TextToSpeech tts;

	@Override
    public void onDestroy()
    {
		super.onDestroy();
		tts.shutdown();
    }
	
	@Override
	public void onInit(int status) {
		String myText = "���ְ��� �� �߰� �� ���� �� ã�⸦ �� �� �ֽ��ϴ�.";
		if(AndroidSocket.OPTION_TTS)
			tts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
	}
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_often_way);

	        soccket=AndroidSocket.shared();
	        Intent intent=getIntent();
	        id=intent.getStringExtra("id");
	        tts = new TextToSpeech(this, this);
			 SetWay();
	 }
	 
	 //A_SET_WAY
	 public void SetWay()
	 {
		 char[] message=new char[128];
	        char[] getMessage=new char[128];
	        String strMessage="A_SET_WAY#";
	        int i;
	        for(i=0;i<strMessage.length();i++)
         	message[i]=strMessage.charAt(i);
         for(i=21;i<id.length()+21;i++)
         	message[i]=id.charAt(i-21);
         message[i]='#';
         
         strMessage="";
         for(i=0;i<60;i++)
         	strMessage+=message[i];
	        //A_SET_FRIENDS
	        soccket.SendMessage(strMessage);//���ְ��� ���� �޶�� �䱸�Ѵ�
	        
	        
	        LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout1);
	        LinearLayout arrLinear[]=new LinearLayout[30];
	        TextView textView[]=new TextView[30];
	        Button goWayBtn[]= new Button[30];
	        Button deleteBtn[]= new Button[30];
	        
	        
	        out=0;
	        for(;;out++)
	        {
	        	while(soccket.HasMessage());
	        		getMessage=soccket.GetMessage();//�����κ��� �ޱ�
	        		
	        		if(getMessage[0]!='0')
	        		{
	        			WayName[out]="";
	        			lat[out]="";
	        			lon[out]="";
	        			for(i=0;getMessage[i]!='#';i++)
	        				WayName[out]+=getMessage[i];
	        			for(i=15;getMessage[i]!='#';i++)
	        				lat[out]+=getMessage[i];
	        			for(i=27;getMessage[i]!='#';i++)
	        				lon[out]+=getMessage[i];
	        			soccket.SendMessage(out+". ������: "+WayName[out]+" lat: "+lat[out]+" lon: "+lon[out]);//ģ������� �޶�� �䱸�Ѵ�
	        		}
	        		else{
	        			break;
	        		}
	        		
	        		Log.i("OftenWay", String.valueOf(out));
	        		
	        			  arrLinear[out]=new LinearLayout(this);
	        			  arrLinear[out].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        			  arrLinear[out].setOrientation(LinearLayout.HORIZONTAL);
	        			  arrLinear[out].setPadding(5, 5, 5, 0);
	        			  arrLinear[out].setGravity(Gravity.CENTER_VERTICAL);
	        			  linear.addView(arrLinear[out]); // 
	        			  	      			  
	        			  textView[out] = new TextView(this);
	        			  textView[out].setText(WayName[out]);
	        			  textView[out].setWidth(SC.DeviceWidth/3);
	        			  textView[out].setTextSize(18);
	        			  textView[out].setId(200 + out);
	        			  textView[out].setTextColor(Color.parseColor("#000000"));
	        			  
	        			  goWayBtn[out] = new Button(this);
	        			  goWayBtn[out].setText("�ٷ� �ȳ�");
	        			  goWayBtn[out].setWidth(SC.DeviceWidth/3);
	        			  goWayBtn[out].setTextSize(11);
	        			  goWayBtn[out].setTextColor(Color.parseColor("#ffffff"));
	        			  goWayBtn[out].setId(out);
	        			  goWayBtn[out].setOnClickListener(this);
	        			  goWayBtn[out].setBackgroundColor(Color.parseColor("#00BAFF"));
	        			  goWayBtn[out].setTextSize(17);
	        			  
	        			  deleteBtn[out] = new Button(this);
	        			  deleteBtn[out].setText("����");
	        			  deleteBtn[out].setWidth(SC.DeviceWidth/3);
	        			  deleteBtn[out].setTextSize(10);
	        			  deleteBtn[out].setId(100 + out);
	        			  deleteBtn[out].setOnClickListener(this);
	        			  deleteBtn[out].setTextColor(Color.parseColor("#ffffff"));
	        			  deleteBtn[out].setBackgroundColor(Color.parseColor("#00BAFF"));
	        			  deleteBtn[out].setTextSize(17);
	        				
	        			  arrLinear[out].addView(textView[out]);
	        			  arrLinear[out].addView(goWayBtn[out]);
	        			  arrLinear[out].addView(deleteBtn[out]);
	        			  
	        			  
		 }
	 }
///////////////
	 //////////////////
	 //////////////////////
	 
	 //A_WAYLOC
	    public void SendLocationMsg(String WayName)
	    {
	       char[] getMessage=new char[128];
	       char message[]=new char[128];
	       String strTemp="A_WAYLOC#";
	       int i;
	       for(i=0;i<strTemp.length();i++)
	          message[i]=strTemp.charAt(i);

	       for(i=21;i<id.length()+21;i++)
	          message[i]=id.charAt(i-21);
	       message[i]='#';
	       for(i=41;i<WayName.length()+41;i++)
	          message[i]=WayName.charAt(i-41);
	       message[i]='#';
	       strTemp="";
	       for(i=0;i<60;i++)
	          strTemp+=message[i];

	       soccket.SendMessage(strTemp);//21~ id, 41~ ��ġ �̸��� ������
	       String myText;
	       while(soccket.HasMessage());
	          getMessage=soccket.GetMessage();//�����κ��� �ޱ�
	          
	           //lat 41~ lon
	          if(getMessage[21]!='0'){
	             String lat="";
	             String lon="";
	             
	             for(i=21;getMessage[i]!='#';i++)
	                lat+=getMessage[i];
	             
	             for(i=41;getMessage[i]!='#';i++)
	                lon+=getMessage[i];
	             
	             //WayName = ���̸�
	             //lat = x
	             // lon = y
	             
	             
//	             String tName = null;
//		        	double tX = 0;
//		        	double tY = 0;

	             if(soccket.OPTION_NAVI_TYPE)
	     		{
	            	 Intent ii = new Intent(OftenWay.this ,
		    				 NewNavi.class);
		    				 ii.putExtra("str",WayName);
		    				 ii.putExtra("lat", lat);
		    				 ii.putExtra("lng", lon);
		    				
		    				 startActivity(ii);
	     		}
	     		else
	     		{
	     			Intent ii = new Intent(OftenWay.this ,
		    				 Navi.class);
		    				 ii.putExtra("str",WayName);
		    				 ii.putExtra("lat", lat);
		    				 ii.putExtra("lng", lon);
		    				
		    				 startActivity(ii);
	     		}
		    		
	             
	             
	             
	             
	             
	             
	          }
	           else{
	              Toast.makeText(this, "�˻� ����!", Toast.LENGTH_SHORT).show();
	              myText = WayName+" �˻� ����.";
	              if(AndroidSocket.OPTION_TTS)
	                 tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
	           }   
	    }
	    
	    
	 //A_WAYDELETE
	 public void SendDeleteMsg(String WayName)
	 {
		 char[] getMessage=new char[128];
		 char message[]=new char[128];
		 String strTemp="A_WAYDELETE";
		 int i;
		 for(i=0;i<strTemp.length();i++)
			 message[i]=strTemp.charAt(i);

		 for(i=21;i<id.length()+21;i++)
			 message[i]=id.charAt(i-21);
		 message[i]='#';
		 for(i=41;i<WayName.length()+41;i++)
			 message[i]=WayName.charAt(i-41);
		 message[i]='#';
		 strTemp="";
		 for(i=0;i<60;i++)
			 strTemp+=message[i];

		 soccket.SendMessage(strTemp);//21~ id, 41~ ��ġ �̸��� ������
		 String myText;
		 while(soccket.HasMessage());
		 	getMessage=soccket.GetMessage();//�����κ��� �ޱ�
		 	
		 	if(getMessage[21]=='1'){
        		Toast.makeText(this, "���� ����!", Toast.LENGTH_SHORT).show();
        		myText = WayName+" ���� ����.";
        		if(AndroidSocket.OPTION_TTS)
        			tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
		 	}
        	else{
        		Toast.makeText(this, "���� ����!", Toast.LENGTH_SHORT).show();
        		myText = WayName+" ���� ����.";
        		if(AndroidSocket.OPTION_TTS)
        			tts.speak(myText, TextToSpeech.QUEUE_ADD, null);
        	}	
	 }
	 
	 public void onClick(View view) {//��ư ��������
	        switch (view.getId()) {
	        	case R.id.Btn00://���ְ��±� �߰�
	        		tts.stop();
	        		Intent intent3 = new Intent(this, AddWay.class);
	        		intent3.putExtra("id", id);
	            	startActivity(intent3);
	            	this.finish();//�� ȭ�� ����
	        		return;
	        }
	        if(100>view.getId())//�ٷ� �ȳ�
	        {
	        	SendLocationMsg(WayName[view.getId()]);
	        }
	        else{//����
	        	SendDeleteMsg(WayName[view.getId()-100]);
	        	Reset();
	        }
	    //��ġ����: 0~
	    //����: 100~
	       //(view.getId())
	 }
	 public void Reset()
	 {
		 Intent intent1 = new Intent(this, OftenWay.class);
     	intent1.putExtra("id", id);
     	startActivity(intent1);
     	finish();//�� ȭ�� ����
	 }
}
