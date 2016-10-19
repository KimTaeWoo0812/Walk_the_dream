package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

public class ShowIntro extends Activity  { 

	boolean isRun=true;
	  
	int inputNumber =0;
    
	
	TextView tv_intro_show;
	int order=0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_intro);
        
        tv_intro_show = (TextView)findViewById(R.id.tv_intro_show);
        isRun=true;
        
        
        final Handler handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		if(msg.what==1)
        			tv_intro_show.setText("¡Ü  ¡Û  ¡Û");
        		else if(msg.what==2)
        			tv_intro_show.setText("¡Û  ¡Ü  ¡Û");
        		else if(msg.what==3)
        			tv_intro_show.setText("¡Û  ¡Û  ¡Ü");
        	}
        };

        
		Runnable task = new Runnable() {
			public void run() {
					
					for(int i =  0 ; i < 3 ; i ++)
					{
						
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
						}
		
						Message message = Message.obtain();
						message.what = 1;
						handler.sendMessage(message);
		
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
						}
						Message message2 = Message.obtain();
						message2.what = 2;
						handler.sendMessage(message2);
		
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
						}
						Message message3 = Message.obtain();
						message3.what = 3;
						handler.sendMessage(message3);
						
						//

					}
					
					
					startActivity(new Intent(getApplicationContext(),MainActivity.class));
					isRun=false;
			        finish();

					
					
					
					
					
					
					
					//
					

			}
		};
		
		Thread thread = new Thread(task);
		thread.start();
        
        
//		startActivity(new Intent(getApplicationContext(),MainActivity.class));
//		isRun=false;
//        finish();
    }
}