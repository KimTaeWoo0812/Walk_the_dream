//package com.ssm.walkthedream;
//
//import android.app.ActionBar.LayoutParams;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class tmp4_part  extends Activity{
//	public static int DeviceWidth;
//	String roomHost="";
//	String id="";
//	Button join;
//	 protected void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.tmp4_part);
//	        
//	        //여기에 전의 엑티비티에서 내 폰번을 받아와서 id에 넣는다
//	        //RoomNum에 이 방번호 넣어야댐
//	        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
//	        DeviceWidth = metrics.widthPixels;
//	        
//	        String RoomNum="1";
//	        String getmessage="";
//	       socket.SendMessage("A_PART_SET#"+RoomNum+'#');
//	       
//	       while(soccket.HasMessage());
//	       	getmessage=soccket.GetMessage();//서버로부터 받기
//	       	String Msg[]=getmessage.split("#");
//	       	int num=Integer.parseInt(Msg[1]);
//	       	roomHost=Msg[2];
//	       
//	       	join = (Button) findViewById(R.id.Btn1);
//	       	
//	       	if(!roomHost.equals(id))
//	       	{
//	       		join.setEnabled(false);
//	       		join.setVisibility(View.INVISIBLE);
//	       	}
//	       	
//	       	int i;
//	       	String ponNum[]=new String[num];
//	       	String userName[]=new String[num];
//	       	String sex[]=new String[num];
//	       	String age[]=new String[num];
//	       	String trust[]=new String[num];
//	       	
//	       	LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout1);
//			LinearLayout arrLinear[] = new LinearLayout[num];
//			LinearLayout arrInLinear1[] = new LinearLayout[num];
//			LinearLayout arrInLinear2[] = new LinearLayout[num];
//			TextView textPonNum[] = new TextView[num];
//			TextView textUserName[] = new TextView[num];
//			TextView textSex[] = new TextView[num];
//			TextView textAge[] = new TextView[num];
//			TextView textTrust[] = new TextView[num];
//	       	
//	       	for(i=0;i<num;i++)
//	       	{
//	       		while(soccket.HasMessage());
//		       		getmessage=soccket.GetMessage();//서버로부터 받기
//		       	
//		       	String msg[]=getmessage.split("#");
//	       		ponNum[i]=msg[1];
//	       		userName[i]=msg[2];
//	       		sex[i]=msg[3];
//	       		age[i]=msg[4];
//	       		trust[i]=msg[5];
//	       		
//	       		
//	       		arrLinear[i] = new LinearLayout(this);
//				arrLinear[i].setLayoutParams(new LinearLayout.LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				arrLinear[i].setOrientation(LinearLayout.HORIZONTAL);
//				arrLinear[i].setPadding(5, 5, 5, 0);
//				arrLinear[i].setGravity(Gravity.CENTER_VERTICAL);
//				//arrLinear[i].setGravity(Gravity.VERTICAL_GRAVITY_MASK);
//				linear.addView(arrLinear[i]);
//
//				arrInLinear1[i] = new LinearLayout(this);
//				arrInLinear1[i].setLayoutParams(new LinearLayout.LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				arrInLinear1[i].setOrientation(LinearLayout.HORIZONTAL);
//				arrInLinear1[i].setPadding(5, 5, 5, 0);
//				arrInLinear1[i].setGravity(Gravity.VERTICAL_GRAVITY_MASK);
//				//arrLinear[i].setGravity(Gravity.VERTICAL_GRAVITY_MASK);
//				arrLinear[i].addView(arrInLinear1[i]);
//				
//				arrInLinear2[i] = new LinearLayout(this);
//				arrInLinear2[i].setLayoutParams(new LinearLayout.LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				arrInLinear2[i].setOrientation(LinearLayout.HORIZONTAL);
//				arrInLinear2[i].setPadding(5, 5, 5, 0);
//				arrInLinear2[i].setGravity(Gravity.VERTICAL_GRAVITY_MASK);
//				//arrLinear[i].setGravity(Gravity.VERTICAL_GRAVITY_MASK);
//				arrLinear[i].addView(arrInLinear2[i]);
//				
//				
//				
//				
//				textPonNum[i] = new TextView(this);
//				textPonNum[i].setText(ponNum[i]);
//				textPonNum[i].setWidth(DeviceWidth/2 );
//				textPonNum[i].setTextSize(15);
//				textPonNum[i].setId(200 + i);
//				textPonNum[i].setTextColor(Color.parseColor("#000000"));
//				arrInLinear1[i].addView(textPonNum[i]);
//	       		
//				textUserName[i] = new TextView(this);
//				textUserName[i].setText(userName[i]);
//				textUserName[i].setWidth(DeviceWidth/2 );
//				textUserName[i].setTextSize(15);
//				textUserName[i].setId(200 + i);
//				textUserName[i].setTextColor(Color.parseColor("#000000"));
//				arrInLinear1[i].addView(textUserName[i]);
//	       		
//				textSex[i] = new TextView(this);
//				textSex[i].setText(sex[i]);
//				textSex[i].setWidth(DeviceWidth/2 );
//				textSex[i].setTextSize(15);
//				textSex[i].setId(200 + i);
//				textSex[i].setTextColor(Color.parseColor("#000000"));
//				arrInLinear1[i].addView(textSex[i]);
//	       		
//				
//				textAge[i] = new TextView(this);
//				textAge[i].setText(age[i]);
//				textAge[i].setWidth(DeviceWidth/2 );
//				textAge[i].setTextSize(15);
//				textAge[i].setId(200 + i);
//				textAge[i].setTextColor(Color.parseColor("#000000"));
//				arrInLinear2[i].addView(textAge[i]);
//				
//				textTrust[i] = new TextView(this);
//				textTrust[i].setText(trust[i]);
//				textTrust[i].setWidth(DeviceWidth/2 );
//				textTrust[i].setTextSize(15);
//				textTrust[i].setId(200 + i);
//				textTrust[i].setTextColor(Color.parseColor("#000000"));
//				arrInLinear2[i].addView(textTrust[i]);
//	       	}
//	       	
//	 }
//	
//	
//	 public void onClick(View view) {// 버튼 눌렀을때
//			switch (view.getId()) {
//			case R.id.Btn1:// 참여하기
//				socket.SendMessage("A_JOINGROUP#"+id+"#"+RoomNum+"#");
//				Reset();
//				break;
//				
//			case R.id.Btn2://시작하기
//				socket.SendMessage("A_STARTTOUR#"+RoomNum+"#");
//				break;
//	 
//			}
//	 }
//	 public void Reset() {
//			Intent intent1 = new Intent(this, tmp4_part.class);
//			intent1.putExtra("id", id);
//			startActivity(intent1);
//			finish();// 이 화면 종료
//		}
//}
