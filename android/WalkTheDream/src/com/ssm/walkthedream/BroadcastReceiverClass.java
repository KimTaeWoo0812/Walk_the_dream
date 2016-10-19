package com.ssm.walkthedream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiverClass extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
         
        String name = intent.getAction();
         
        if(name.equals("arabiannight.tistory.com.sendreciver.stairsUps")){
            Toast.makeText
            (context, "정상적으로 값을 받았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
