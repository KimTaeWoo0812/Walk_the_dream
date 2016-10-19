//package com.ssm.walkthedream;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.jsoup.Jsoup;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.speech.tts.TextToSpeech;
//import android.speech.tts.TextToSpeech.OnInitListener;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ssm.walkthedream.MyLocationListener.onChangedXYListener;
//import com.ssm.walkthedream.SearchAndFriends.StairsTTSThread;
//
//public class Navi extends Activity implements OnInitListener{
//   private AndroidSocket soccket ;
//   Handler hStairs;
//   private boolean stopThread=true;
//   TextView tv_navi_meter;
//   ImageView iv_navi_leftright;
//   TextView tv_niavi_do;
//   TextView tv_navi_tofrom;
//   TextView tv_navi_test;
//   
//   private TextToSpeech myTTS;
//
//    String TagString;
//
//    MyLocationListener listener;
//
//    static String gpsRst="";
//    static TextView tv_main_gpsval;
//    TextView tv_main_test;
//    TextView tv_main_test2;
//
//    int maxCheckReflashNum = 3;
//    int checkReflashNum;
//
//    double endX;
//    double endY;
//    double firX;
//    double firY;
//    double curX;
//    double curY;
//    double befX=0;
//    double befY=0;
//
//    String nowRotaCode;
//    String nextRotaCode;
//    String befRotaCode="";
//    String bungiLength;
//
//    int curBungiNum = 0;
//    int befBungiNum = 0;
//
//    double curResult;
//    boolean isFirst = true;
//
//    String Destination;
//
//    boolean rotaCodeChange;
//    StairsTTSThread stairsTTS;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면항상밝게
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_navi);
//        soccket=AndroidSocket.shared();
//        myTTS = new TextToSpeech(this, this);
//        
//      Intent i = getIntent();
//      
//      Destination = i.getExtras().getString("str");
//      Double lat = i.getExtras().getDouble("lng") ;
//      Double lng =  i.getExtras().getDouble("lat") ; // 바뀜
//    
//        tv_navi_meter = (TextView)findViewById(R.id.tv_navi_meter);
//        iv_navi_leftright = (ImageView)findViewById(R.id.iv_navi_leftright);
//        tv_niavi_do = (TextView)findViewById(R.id.tv_niavi_do);
//        tv_navi_tofrom = (TextView)findViewById(R.id.tv_navi_tofrom);
//        tv_navi_test = (TextView)findViewById(R.id.tv_navi_test);
//      
////      double lat = 35.86953379;
////      double lng = 128.60314263;
//      
////        tv_main_test=(TextView)findViewById(R.id.tv_main_test);
////        tv_main_test2=(TextView)findViewById(R.id.tv_main_test2);
////        
////        Log.i("ASDASD",Destination);
////        tv_main_gpsval=(TextView)findViewById(R.id.tv_main_gpsval);
//
//        
////        endX = 813960.625;
////        endY = 736660;
//        
////        endX = lng;
////        endY = lat;
//        
//        Log.i("TMP ADR","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+lng+"&y="+lat+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml");
//        ( new ParseURL() ).execute(new String[]{"GPS","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+lng+"&y="+lat+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml"});
//        
//        Log.i("DST XY" ,String.valueOf(lng) + "   " + String.valueOf(lat));
//
//        
//        
//        
//        
//    }
//    public void StartDtsirsTTSThread()
//    {
//       hStairs = new Handler();
//
//       stairsTTS=new StairsTTSThread();
//       stairsTTS.start();
//    }
//    
//    
//    @Override
//   protected void onPause() {
//      stopThread=false;
//      super.onPause();
//   }
//   @Override
//   protected void onResume() {
//       if(AndroidSocket.OPTION_Bluetooth)
//          StartDtsirsTTSThread();
//      super.onResume();
//   }
//   
//    @Override
//   protected void onDestroy() {
//       listener.endGps();
//       myTTS.shutdown();
//       stopThread=false;
//      super.onDestroy();
//   }
//
//
//
//
//   @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//       if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
//           Log.i("KEYDOWN","KEYCODE_VOLUME_DOWN");
//           
//           startActivity(new Intent(getApplicationContext(),CheckVideo.class));
//           
//        }
//       else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
//           Log.i("KEYDOWN","KEYCODE_VOLUME_UP");
//        }
//       else if (keyCode == KeyEvent.KEYCODE_BACK){
//           Log.i("KEYDOWN","KEYCODE_BACK");
//           onBackPressed();
//        }
//        
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//       super.onBackPressed();
//    }
//
//    private void getMyLocation() {
//       
//       listener = new MyLocationListener(this.getApplicationContext(),onchangedXYListener);
//
//        appendText("내 위치를 요청 했습니다.");
//    }
//
//
//    private void appendText(String msg) {
////        tv_main_gpsval.setText(msg + "\n");
//    }
//
//    int k =0;
//    
//    
////    changeGPSType(longitude, latitude); // 호출
//    
//
//    
////인터페이스로  
//    onChangedXYListener onchangedXYListener = new onChangedXYListener() {
//      
//      @Override
//      public void onChangedXY(double lat_, double lon_) {
//         changeGPSType(lon_,lat_);
//      }
//   };
//
//    public void changeGPSType(double dx,double dy)
//    {
//        Log.i("GPS VAL",dx + " " + dy);
//        
//        
//         ( new ParseURL() ).execute(new String[]{"GPS","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+dx+"&y="+dy+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml"});
//
//    }
//
//    public void bt_main_getgps(View v)
//    {
//        getMyLocation();
//    }
//
//    public void bt_main_removegps(View v)
//    {
//        tv_main_gpsval.setText("GPS 종료");
////       listener.endGps();
//    }
//
//
//    private void getRoute(double x1,double y1, double x2, double y2) {
////        Log.i("JINIB","getRoute진입시작");
////        ( new ParseURL() ).execute(new String[]{"ROUTE","http://map.daum.net/route/walk.json?callback=jQuery18107084809814114124_1432346490191&walkMode=RECOMMENDATION&walkOption=NONE&sName=%EB%8C%80%EA%B5%AC%EC%97%AD&sX="+x1+"&sY="+y1+"&eName=%EC%A4%91%EC%95%99%EB%A1%9C&eX="+x2+"&eY="+y2+"&ids=P21367546%2CP12728831"});
//        ( new ParseURL() ).execute(new String[]{"ROUTE","http://map.daum.net/route/walkset.json?callback=jQuery1810362975598545745_1435566230779&sName=%EA%B2%BD%EB%B6%81+%EA%B5%AC%EB%AF%B8%EC%8B%9C+%EA%B1%B0%EC%9D%98%EB%8F%99+468&sX="+x1+"&sY="+y1+"+&eName=%EA%B2%BD%EB%B6%81+%EA%B5%AC%EB%AF%B8%EC%8B%9C+%EA%B1%B0%EC%9D%98%EB%8F%99+468&eX="+x2+"+&eY="+y2+"&ids=%2C"});
//
//
//    }
//
//    String LorR_TMP;
//    
//    public class ParseURL extends AsyncTask<String, Void, String> {
//        String retStr="";
//        String retStrForGPS="";
//        @Override
//        protected String doInBackground(String... strings) {
////            Log.d("MAIN","CALL");
//
//            TagString=strings[0];
//
////            boolean isFirst = true;
//
//            nowRotaCode="NONE";
//            nextRotaCode="NONE";
//
//            if(strings[0].compareTo("ROUTE")==0) {
//                try {
////                    Log.i("JINIB","getRoute진입중1");
//
//                    String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
//                    String strBuf = "";
//                    strBuf = (Jsoup.connect(strings[1]).timeout(20000).ignoreContentType(true).execute().body());
//
//
//
//
//
//                    String[] strSPLrouteMode = strBuf.split("routeMode");
//                    ArrayList<OneOfRoute> oneOfRouteList = new ArrayList<OneOfRoute>();
//                    Log.i("strBuf",strBuf);
//
//                    String[] strSPLgroupId = strSPLrouteMode[1].split("groupId");
//                    String[] strEnd = strSPLrouteMode[1].split("END");
//
//
//                    String[] xx = strEnd[1].split("x\":");
//                    String[] yy = strEnd[1].split("y\":");
//
//                    if(xx[0].contains(","))
//                        xx[0] = xx[1].substring(0, xx[1].indexOf(","));
//                    else
//                        xx[0] = xx[1].substring(0, xx[1].indexOf("}"));
//
//                    if(xx[0].contains("}"))
//                        xx[0]=xx[0].substring(0,xx[0].length()-1);
//
//                    yy[0] = yy[1].substring(0, yy[1].indexOf(","));
////                    int count = 0;
//
//                    ///////////////xxxxxxxxyyyy 끝일때 목적지로추가해주면될차례
//
//
//
//                    Log.i("dlfghldyd",String.valueOf(strSPLgroupId.length));
//
//                    befBungiNum = curBungiNum;
//                    int tmpLen = curBungiNum = strSPLgroupId.length;
//                    
//                
//
//                    for (int i = 1; i < strSPLgroupId.length - 1 ; i++) {
////                        Log.d("MAIN", "PARSER " + i + " ED");
////                    if(count>=2)
////                        break;
//                        //count++;
//                        //for문 length-1 한 이유가 마지막라인은 도착이므로
//
//                        String fullstr = strSPLgroupId[i];
//
//                        String[] guidecode = fullstr.split("guideCode\":\"");
//                        String[] guidement = fullstr.split("guideMent\":\"");
//                        String[] rotationCode = fullstr.split("rotationCode\":\"");
//                        String[] x = fullstr.split("x\":");
//                        String[] y = fullstr.split("y\":");
//
//
//
//
//                        guidecode[0] = guidecode[1].substring(0, guidecode[1].indexOf("\""));
//                        guidement[0] = guidement[1].substring(0, guidement[1].indexOf("\""));
//                        rotationCode[0] = rotationCode[1].substring(0, rotationCode[1].indexOf("\""));
//
//
////                        Log.i("dlfghldyd",x[0]);
//
//                        if(x[0].contains(","))
//                            x[0] = x[1].substring(0, x[1].indexOf(","));
//                        else
//                            x[0] = x[1].substring(0, x[1].indexOf("}"));
//
//                        if(x[0].contains("}"))
//                            x[0]=x[0].substring(0,x[0].length()-1);
//
//                        y[0] = y[1].substring(0, y[1].indexOf(","));
//
//
//                        retStr="";
//                        retStr += guidecode[0] + "@";
//                        retStr += guidement[0] + "@";
//                        retStr += rotationCode[0] + "@";
//                        retStr += x[0] + "@";
//                        retStr += y[0] + "@";
//
//                        if(rotaCodeChange==true)
//                        {
//                           rotaCodeChange=false;
//                            befRotaCode = nextRotaCode;
//                        }
//                        if(i==1)
//                            nowRotaCode = rotationCode[0];
//                        if(i==2)
//                            nextRotaCode = rotationCode[0];
//                        if(i==1)
//                            bungiLength = guidement[0];
//                        
//                    if(bungiLength.contains("m")==true)
//                       bungiLength = bungiLength.substring(0,bungiLength.indexOf("m"));
//                    else if(bungiLength.contains("미")==true)
//                       bungiLength = bungiLength.substring(0,bungiLength.indexOf("미"));
//
//                        if(tmpLen==3 && i!=2)
//                        {
//                            firX = Double.parseDouble( x[0] );
//                            firY = Double.parseDouble( y[0] );
//                        }
//
//                        if(i==2)
//                        {
//                            firX = Double.parseDouble( x[0] );
//                            firY = Double.parseDouble( y[0] );
//                        }
//
//                        Log.i("retStr",i+"번쨰  " + retStr);
//                    }
//
////                    tv_main_gpsval.setText(retStr);
//
////                    Log.i("JINIB","getRoute진입중2");
//
//
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                    Log.d("MAIN", "ERROR");
//                }
//            }
//            if(strings[0].compareTo("GPS")==0) {
//                try {
////                    Log.i("JINIB","GPSC진입중1");
//                    String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
//                    String strBuf = "";
//                    strBuf = (Jsoup.connect(strings[1]).timeout(20000).ignoreContentType(true).execute().body());
//
//                    double xx;
//                    double yy;
//
//                    String []tmpStrArr = strBuf.split("'");
//                    xx = Double.parseDouble( tmpStrArr[1] );
//                    yy = Double.parseDouble( tmpStrArr[3] );
//
//                    retStrForGPS = xx + "@" + yy;
////                    Log.i("JINIB","GSPC진입중2");
//
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                    Log.d("MAIN", "ERROR");
//                }
//            }
//
//            return null;
//        }
//
//        int bungi = 0;
//        
//        
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
////            Log.d("TagString",TagString);
//            if(TagString.compareTo("ROUTE")==0)
//            {
////                Log.i("JINIB","getRoute진입완료");
//
//                //firX curX befX
//
//                Log.i("RSTT1", "firX : " + firX + "  firY : " + firY + "   befX : " + befX + "   befY : " + befY + "   curX : " + curX + "   curY : "+ curY);
//
//
//                if(firX == befX && firY == befY)
//                {
//                    appendText("움직임없음");
//                    return;
//                }
//
//                double a = Math.sqrt ( Math.pow(firX - befX,2) + Math.pow(firY - befY,2) );
//                double b = Math.sqrt ( Math.pow(curX - firX,2) + Math.pow(curY - firY,2) );
//                double c = Math.sqrt ( Math.pow(curX - befX,2) + Math.pow(curY - befY,2) );
//
//
//                curResult = Math.acos( ( c*c + a*a - b*b ) / ( 2 * c * a )   )  * 180/Math.PI;
//
//
//                if(curResult >60)
//                    checkReflashNum++;
//                else
//                    checkReflashNum=0;
//
//
//
//                Log.i("RSTT2", "a : " + a +  "b : " + b + "    c : "+ c + "      rst : " + curResult);
//
//
//                befX = curX;
//                befY = curY;
//
//                
//                
//                
//                
//            final Handler handler = new Handler();
//            new Thread(new Runnable() {
//               @Override
//               public void run() {
//                  handler.post(new Runnable() {
//                     public void run() {
//
////                        tv_navi_meter
////                          iv_navi_leftright
////                          tv_niavi_do
////                          tv_navi_tofrom
//                        
//                        Log.i("ASDASD","asdasdasd");
//                        
//                        bungi++;
//                     
//                        // 길 안내 종료
//                        
//                        String tmpStr = bungiLength;
//                        
//                        
//                       
//                        while(tmpStr.charAt(0) >'9' || tmpStr.charAt(0)  <'0')
//                        {
//                          tmpStr = tmpStr.substring(1, tmpStr.length());
//                        }       
//                        
//                        bungiLength=tmpStr;
//                        
//                        if( curBungiNum == 3 && Integer.parseInt( tmpStr)<30)
//                        {
//                           myTTS.speak("목적지에 도착하였습니다. 길 안내를 종료합니다.", TextToSpeech.QUEUE_ADD, null);
//                           Handler hh = new Handler();
//                             hh.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                      finish();
//                                }
//                             }, 4000);                     
//                             }
//                      
//                        
//                            if(curBungiNum != befBungiNum)
//                            {
////                                appendText(befRotaCode + " 방향으로 턴입니다");
//
//                               
//                               if(befRotaCode.compareTo("TURN_LEFT")==0)
//                               {
//                                  myTTS.speak("현재 지점에서 왼쪽 방향으로 턴입니다", TextToSpeech.QUEUE_ADD, null);
//                                  rotaCodeChange=true;
//                               }
//                               else if(befRotaCode.compareTo("TURN_RIGHT")==0)
//                               {
//                                  myTTS.speak("현재 지점에서 오른쪽 방향으로 턴입니다", TextToSpeech.QUEUE_ADD, null);
//                                  rotaCodeChange=true;
//                               }
//
//                               
//                               
//                            }
//                            else
//                            {
////                               if(bungi>10)
////                               {
//                                  bungi=0;
//                                  if(checkReflashNum>=maxCheckReflashNum)
//                                  {
//                                     tv_navi_meter.setText(String.valueOf(bungiLength)+"m 후");
//                                     
//                                     tv_navi_test.setText(nextRotaCode);
//                                     
//                                     if(nextRotaCode.compareTo("TURN_LEFT")==0)
//                                     {
//                                        iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.left));
//                                     }
//                                     else if(nextRotaCode.compareTo("TURN_RIGHT")==0)
//                                     {
//                                        iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.right));
//                                     }
//                                     
//                                     tv_niavi_do.setText(String.valueOf((int)(curResult))+"º\n틀어졌습니다.");                   
//                                     tv_navi_tofrom.setText(String.valueOf(curX)+"\n"+String.valueOf(curY));
//
//                                     if(SC.doNum>2)
//                                     {
//                                        SC.doNum=0;
//                                        
//                                        
//                                        myTTS.speak(String.valueOf((int)(curResult))+"도 틀어졌습니다.", TextToSpeech.QUEUE_ADD, null);
//                                     }
//                                     
//                                     SC.doNum++;
//                                     
//                                     
//                                     
//                                     
//                                     
//                                     if(SC.meterNum>3)
//                                     {
//                                        SC.meterNum=0;
//                                        myTTS.speak(String.valueOf(bungiLength), TextToSpeech.QUEUE_ADD, null);
//                                     }
//                                     SC.meterNum++;
//                                     
//                                     tv_navi_test.setText("미터 : " + String.valueOf(SC.meterNum) +"\n도넘 : " + String.valueOf(SC.doNum));
//                                     
////                                     appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode );
////                                     myTTS.speak("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode , TextToSpeech.QUEUE_ADD, null);
//                                  }
//                                  else
//                                  {
//                                     SC.doNum=0;
//                                     tv_navi_meter.setText(String.valueOf(bungiLength)+"m 후");
//
//                                     tv_navi_test.setText(nextRotaCode);
//
//                                     
//                                     if(nextRotaCode.compareTo("TURN_LEFT")==0)
//                                     {
//                                        iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.left));
//                                     }
//                                     else if(nextRotaCode.compareTo("TURN_RIGHT")==0)
//                                     {
//                                        iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.right));
//                                     }
//                                     
//                                     tv_niavi_do.setText("올바른 방향입니다.\n"+String.valueOf((int)(curResult))+"º");
//                                     
//                                     tv_navi_tofrom.setText(String.valueOf(curX)+"\n"+String.valueOf(curY));
//
//                                     if(SC.meterNum>2)
//                                     {
//                                        SC.meterNum=0;
//                                        myTTS.speak(String.valueOf(bungiLength), TextToSpeech.QUEUE_ADD, null);
//                                     }
//                                     SC.meterNum++;
//
//                                     tv_navi_test.setText("미터 : " + String.valueOf(SC.meterNum) +"\n도넘 : " + String.valueOf(SC.doNum));
//
//                                     
////                                     appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode );
////                                     myTTS.speak("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode, TextToSpeech.QUEUE_ADD, null);                                  
//                                  
//                                  }
//                                  
////                               }
//                                  
//                            }
//                        
//                        
//                          
//                     }
//                  });
//               }
//            }).start();
//                
//                
//                
//                
//                
//                
//                
//                // 3번 방향 60도벗어낫을경우 틀어짐알려줌
//                if(checkReflashNum>=maxCheckReflashNum)
//                {
//                    appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode );
//                    return;
//                }
//
//                // 분기도달시
////                if(curBungiNum != befBungiNum)
////                {
////                    appendText(befRotaCode + " 방향으로 턴입니다");
////                    return;
////                }
//
////                setDirection("curResult : " + curResult);
//                appendText("curResult : " + curResult);
//
//                appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode );
//
//
//            }
//            else if(TagString.compareTo("GPS")==0)
//            {
//               
//               
//               
////                Log.i("JINIB","GPSC진입완료");
//
//                String [] tmpStrArr = retStrForGPS.split("@");
//
//                Log.i("retStrForGPS" , retStrForGPS);
//
//                double[] tmpDoubleArr = new double[2];
//
//                
//                if(isFirst==true)
//                {
//                   endX = Double.parseDouble(tmpStrArr[0]);
//                   endY = Double.parseDouble(tmpStrArr[1]);
//                   
//                   isFirst=false;
//                   bt_main_getgps(null);
//                   return ;
//                }
//
//                //바뀐 x,y값
//                curX = tmpDoubleArr[0] = Double.parseDouble(tmpStrArr[0]);
//                curY = tmpDoubleArr[1] = Double.parseDouble(tmpStrArr[1]);
//
////                appendText(tmpStrArr[0]+"  ,  "+tmpStrArr[1]);
//
//
//
//                getRoute(tmpDoubleArr[0],tmpDoubleArr[1],endX,endY);
//
//
//            }
//
//        }
//    }
//
//    private void setDirection(String retStr) {
//
//        Log.i("THIS",retStr);
//    }
//
//
//   @Override
//   public void onInit(int status) {
//        String tmpStr = "목적지 " + Destination+ "으로 길 안내를 시작합니다";
//        myTTS.speak(tmpStr, TextToSpeech.QUEUE_ADD, null);
//      
//   }
//    
//   
//   
//   
//   
//   
//   
//   
//   
//   //계단정보 받는곳
//   public class StairsTTSThread extends Thread{
//
//      public void run(){
//         for(;stopThread;)
//          {
//             try {
//                  Thread.sleep(3000);
//               } catch (InterruptedException e) {
//                  // TODO Auto-generated catch block
//                  e.printStackTrace();
//               }
//                   if(soccket.hTop!=soccket.hRear)
//                   {
//                      hStairs.post(new Runnable() {
//                         String myText;
//                         @Override
//                         public void run() {
//                            switch(soccket.hQueue[soccket.hRear%20])
//                            {
//                            case 10:
//                               if(soccket.BluetoothConnected)
//                               soccket.sendData("1\n\n");
//                               myText = "전방에 올라가는 계단이 있습니다.";
//                               break;
//                            case 11:
//                               if(soccket.BluetoothConnected)
//                               soccket.sendData("2\n\n");
//                               myText = "전방에 내려가는 계단이 있습니다.";
//                               break;
//                            case 1:
//                               SendStairsData('1');
//                               myText = "올라가는 계단을 저장하였습니다.";
//                               break;
//                            case 2:
//                               SendStairsData('2');
//                               myText = "내려가는 계단을 저장하였습니다.";
//                               break;
//                            }
////                            if(AndroidSocket.OPTION_TTS)
//                            if(false)
//                               myTTS.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
//                            soccket.hRear++;
//                         }
//                      });
//                   }
//          }
//   
//      }
//      public void SendStairsData(char type)
//      {
//         String strTemp="A_SAVESTA#";
//         char cMsg[]=new char[128];
//         String lat = Double.toString( soccket.latitude);//gps.loc.getLatitude());
//         String lon = Double.toString(soccket.longitude);//gps.loc.getLongitude());
//         int i;
//         
//         for(i=0;strTemp.charAt(i)!='#';i++)
//            cMsg[i]=strTemp.charAt(i);
//       
//         for(i=21;i<lat.length()+21;i++)
//            cMsg[i]=lat.charAt(i-21);
//         cMsg[i]='#';
//         
//         for(i=41;i<lon.length()+41;i++)
//            cMsg[i]=lon.charAt(i-41);
//         cMsg[i]='#';
//         
//         cMsg[61]=type;
//         cMsg[62]='#';
//         
//         strTemp="";
//         for(i=0;i<128;i++)
//            strTemp+=cMsg[i];
//         soccket.SendMessage(strTemp);
//      }
//   }
//   
//}
//
//
//// 버튼누름 - getMyLocation - GPS 가동 - changeGPSType - getRoute - 현 전 첫 관계로 세타계산



package com.ssm.walkthedream;

import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssm.walkthedream.MyLocationListener.onChangedXYListener;

public class Navi extends Activity implements OnInitListener{

	TextView tv_navi_meter;
	ImageView iv_navi_leftright;
	TextView tv_niavi_do;
	TextView tv_navi_tofrom;
	TextView tv_navi_test;
	
	private TextToSpeech myTTS;

    String TagString;

    MyLocationListener listener;

    static String gpsRst="";
    static TextView tv_main_gpsval;
    TextView tv_main_test;
    TextView tv_main_test2;

    int maxCheckReflashNum = 3;
    int checkReflashNum;

    double endX;
    double endY;
    double firX;
    double firY;
    double curX;
    double curY;
    double befX=0;
    double befY=0;

    String nowRotaCode;
    String nextRotaCode;
    String befRotaCode="";
    String bungiLength;

    int curBungiNum = 0;
    int befBungiNum = 0;

    double curResult;
    boolean isFirst = true;

    String Destination;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면항상밝게

        requestWindowFeature(Window.FEATURE_NO_TITLE);        setContentView(R.layout.activity_navi);
        
        myTTS = new TextToSpeech(this, this);
        
		Intent i = getIntent();
		
		Destination = i.getExtras().getString("str");
		Double lat = i.getExtras().getDouble("lng") ;
		Double lng =  i.getExtras().getDouble("lat") ; // 바뀜
    
		  tv_navi_meter = (TextView)findViewById(R.id.tv_navi_meter);
		  iv_navi_leftright = (ImageView)findViewById(R.id.iv_navi_leftright);
		  tv_niavi_do = (TextView)findViewById(R.id.tv_niavi_do);
		  tv_navi_tofrom = (TextView)findViewById(R.id.tv_navi_tofrom);
		  tv_navi_test = (TextView)findViewById(R.id.tv_navi_test);
		
//		double lat = 35.86953379;
//		double lng = 128.60314263;
		
//        tv_main_test=(TextView)findViewById(R.id.tv_main_test);
//        tv_main_test2=(TextView)findViewById(R.id.tv_main_test2);
//        
//        Log.i("ASDASD",Destination);
//        tv_main_gpsval=(TextView)findViewById(R.id.tv_main_gpsval);

        
//        endX = 813960.625;
//        endY = 736660;
        
//        endX = lng;
//        endY = lat;
        
        Log.i("TMP ADR","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+lng+"&y="+lat+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml");
        ( new ParseURL() ).execute(new String[]{"GPS","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+lng+"&y="+lat+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml"});
        
        Log.i("DST XY" ,String.valueOf(lng) + "   " + String.valueOf(lat));

        
 
        
        
    }

    
    
    
    @Override
	protected void onDestroy() {
    	listener.endGps();
    	myTTS.shutdown();
		super.onDestroy();
	}




	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
        	Log.i("KEYDOWN","KEYCODE_VOLUME_DOWN");
        	
        	startActivity(new Intent(getApplicationContext(),CheckVideo.class));
        	
        }
    	else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
        	Log.i("KEYDOWN","KEYCODE_VOLUME_UP");
        }
    	else if (keyCode == KeyEvent.KEYCODE_BACK){
        	Log.i("KEYDOWN","KEYCODE_BACK");
        	onBackPressed();
        }
        
        return true;
    }

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }

    private void getMyLocation() {
    	
    	listener = new MyLocationListener(this.getApplicationContext(),onchangedXYListener);

        appendText("내 위치를 요청 했습니다.");
    }


    private void appendText(String msg) {
//        tv_main_gpsval.setText(msg + "\n");
    }

    int k =0;
    
    
//    changeGPSType(longitude, latitude); // 호출
    

    
//인터페이스로  
    onChangedXYListener onchangedXYListener = new onChangedXYListener() {
		
		@Override
		public void onChangedXY(double lat_, double lon_) {
			changeGPSType(lon_,lat_);
		}
	};

    public void changeGPSType(double dx,double dy)
    {
        Log.i("GPS VAL",dx + " " + dy);
        
        
         ( new ParseURL() ).execute(new String[]{"GPS","http://apis.daum.net/maps/transcoord?apikey=4ea71af07ab0504cf561d740334d4ce0&x="+dx+"&y="+dy+"&fromCoord=wgs84&toCoord=wcongnamul&output=xml"});

    }

    public void bt_main_getgps(View v)
    {
        getMyLocation();
    }

    public void bt_main_removegps(View v)
    {
        tv_main_gpsval.setText("GPS 종료");
//    	listener.endGps();
    }


    private void getRoute(double x1,double y1, double x2, double y2) {
//        Log.i("JINIB","getRoute진입시작");
//        ( new ParseURL() ).execute(new String[]{"ROUTE","http://map.daum.net/route/walk.json?callback=jQuery18107084809814114124_1432346490191&walkMode=RECOMMENDATION&walkOption=NONE&sName=%EB%8C%80%EA%B5%AC%EC%97%AD&sX="+x1+"&sY="+y1+"&eName=%EC%A4%91%EC%95%99%EB%A1%9C&eX="+x2+"&eY="+y2+"&ids=P21367546%2CP12728831"});
        ( new ParseURL() ).execute(new String[]{"ROUTE","http://map.daum.net/route/walkset.json?callback=jQuery1810362975598545745_1435566230779&sName=%EA%B2%BD%EB%B6%81+%EA%B5%AC%EB%AF%B8%EC%8B%9C+%EA%B1%B0%EC%9D%98%EB%8F%99+468&sX="+x1+"&sY="+y1+"+&eName=%EA%B2%BD%EB%B6%81+%EA%B5%AC%EB%AF%B8%EC%8B%9C+%EA%B1%B0%EC%9D%98%EB%8F%99+468&eX="+x2+"+&eY="+y2+"&ids=%2C"});


    }

    public class ParseURL extends AsyncTask<String, Void, String> {
        String retStr="";
        String retStrForGPS="";
        @Override
        protected String doInBackground(String... strings) {
//            Log.d("MAIN","CALL");

            TagString=strings[0];

//            boolean isFirst = true;

            nowRotaCode="NONE";
            nextRotaCode="NONE";

            if(strings[0].compareTo("ROUTE")==0) {
                try {
//                    Log.i("JINIB","getRoute진입중1");

                    String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
                    String strBuf = "";
                    strBuf = (Jsoup.connect(strings[1]).timeout(20000).ignoreContentType(true).execute().body());





                    String[] strSPLrouteMode = strBuf.split("routeMode");
                    ArrayList<OneOfRoute> oneOfRouteList = new ArrayList<OneOfRoute>();
                    Log.i("strBuf",strBuf);

                    String[] strSPLgroupId = strSPLrouteMode[1].split("groupId");
                    String[] strEnd = strSPLrouteMode[1].split("END");


                    String[] xx = strEnd[1].split("x\":");
                    String[] yy = strEnd[1].split("y\":");

                    if(xx[0].contains(","))
                        xx[0] = xx[1].substring(0, xx[1].indexOf(","));
                    else
                        xx[0] = xx[1].substring(0, xx[1].indexOf("}"));

                    if(xx[0].contains("}"))
                        xx[0]=xx[0].substring(0,xx[0].length()-1);

                    yy[0] = yy[1].substring(0, yy[1].indexOf(","));
//                    int count = 0;

                    ///////////////xxxxxxxxyyyy 끝일때 목적지로추가해주면될차례



                    Log.i("dlfghldyd",String.valueOf(strSPLgroupId.length));

                    befBungiNum = curBungiNum;
                    int tmpLen = curBungiNum = strSPLgroupId.length;

                    for (int i = 1; i < strSPLgroupId.length - 1 ; i++) {
//                        Log.d("MAIN", "PARSER " + i + " ED");
//                    if(count>=2)
//                        break;
                        //count++;
                        //for문 length-1 한 이유가 마지막라인은 도착이므로

                        String fullstr = strSPLgroupId[i];

                        String[] guidecode = fullstr.split("guideCode\":\"");
                        String[] guidement = fullstr.split("guideMent\":\"");
                        String[] rotationCode = fullstr.split("rotationCode\":\"");
                        String[] x = fullstr.split("x\":");
                        String[] y = fullstr.split("y\":");




                        guidecode[0] = guidecode[1].substring(0, guidecode[1].indexOf("\""));
                        guidement[0] = guidement[1].substring(0, guidement[1].indexOf("\""));
                        rotationCode[0] = rotationCode[1].substring(0, rotationCode[1].indexOf("\""));


//                        Log.i("dlfghldyd",x[0]);

                        if(x[0].contains(","))
                            x[0] = x[1].substring(0, x[1].indexOf(","));
                        else
                            x[0] = x[1].substring(0, x[1].indexOf("}"));

                        if(x[0].contains("}"))
                            x[0]=x[0].substring(0,x[0].length()-1);

                        y[0] = y[1].substring(0, y[1].indexOf(","));


                        retStr="";
                        retStr += guidecode[0] + "@";
                        retStr += guidement[0] + "@";
                        retStr += rotationCode[0] + "@";
                        retStr += x[0] + "@";
                        retStr += y[0] + "@";

                            befRotaCode = nextRotaCode;
                        if(i==1)
                            nowRotaCode = rotationCode[0];
                        if(i==2)
                            nextRotaCode = rotationCode[0];
                        if(i==1)
                            bungiLength = guidement[0];


                        if(tmpLen==3 && i!=2)
                        {
                            firX = Double.parseDouble( x[0] );
                            firY = Double.parseDouble( y[0] );
                        }

                        if(i==2)
                        {
                            firX = Double.parseDouble( x[0] );
                            firY = Double.parseDouble( y[0] );
                        }

                        Log.i("retStr",i+"번쨰  " + retStr);
                    }

//                    tv_main_gpsval.setText(retStr);

//                    Log.i("JINIB","getRoute진입중2");


                } catch (Throwable t) {
                    t.printStackTrace();
                    Log.d("MAIN", "ERROR");
                }
            }
            if(strings[0].compareTo("GPS")==0) {
                try {
//                    Log.i("JINIB","GPSC진입중1");
                    String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
                    String strBuf = "";
                    strBuf = (Jsoup.connect(strings[1]).timeout(20000).ignoreContentType(true).execute().body());

                    double xx;
                    double yy;

                    String []tmpStrArr = strBuf.split("'");
                    xx = Double.parseDouble( tmpStrArr[1] );
                    yy = Double.parseDouble( tmpStrArr[3] );

                    retStrForGPS = xx + "@" + yy;
//                    Log.i("JINIB","GSPC진입중2");

                } catch (Throwable t) {
                    t.printStackTrace();
                    Log.d("MAIN", "ERROR");
                }
            }

            return null;
        }

        int bungi = 0;
        
        
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            Log.d("TagString",TagString);
            if(TagString.compareTo("ROUTE")==0)
            {
//                Log.i("JINIB","getRoute진입완료");

                //firX curX befX

                Log.i("RSTT1", "firX : " + firX + "  firY : " + firY + "   befX : " + befX + "   befY : " + befY + "   curX : " + curX + "   curY : "+ curY);


                if(firX == befX && firY == befY)
                {
                    appendText("움직임없음");
                    return;
                }

                double a = Math.sqrt ( Math.pow(firX - befX,2) + Math.pow(firY - befY,2) );
                double b = Math.sqrt ( Math.pow(curX - firX,2) + Math.pow(curY - firY,2) );
                double c = Math.sqrt ( Math.pow(curX - befX,2) + Math.pow(curY - befY,2) );


                curResult = Math.acos( ( c*c + a*a - b*b ) / ( 2 * c * a )   )  * 180/Math.PI;


                if(curResult >60)
                    checkReflashNum++;
                else
                    checkReflashNum=0;



                Log.i("RSTT2", "a : " + a +  "b : " + b + "    c : "+ c + "      rst : " + curResult);


                befX = curX;
                befY = curY;

                
                
                
                
				final Handler handler = new Handler();
				new Thread(new Runnable() {
					@Override
					public void run() {
						handler.post(new Runnable() {
							public void run() {

//								tv_navi_meter
//								  iv_navi_leftright
//								  tv_niavi_do
//								  tv_navi_tofrom
								
								
								
								Log.i("ASDASD","asdasdasd");
								
								
								
								

								bungi++;
								
								
							 
								
				                if(curBungiNum != befBungiNum)
				                {
//				                    appendText(befRotaCode + " 방향으로 턴입니다");

				                	
			                		if(befRotaCode.compareTo("TURN_LEFT")==0)
			                		{
					                	myTTS.speak("현재 지점에서 왼쪽 방향으로 턴입니다", TextToSpeech.QUEUE_ADD, null);
			                		}
			                		else if(befRotaCode.compareTo("TURN_RIGHT")==0)
			                		{
					                	myTTS.speak("현재 지점에서 오른쪽 방향으로 턴입니다", TextToSpeech.QUEUE_ADD, null);
			                		}

				                	
				                	
				                }
				                else
				                {
//				                	if(bungi>10)
//				                	{
				                		bungi=0;
					                	if(checkReflashNum>=maxCheckReflashNum)
					                	{
					                		tv_navi_meter.setText(String.valueOf(bungiLength)+"후");
					                		
					                		tv_navi_test.setText(nextRotaCode);
					                		
					                		if(nextRotaCode.compareTo("TURN_LEFT")==0)
					                		{
					                			iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.left));
					                		}
					                		else if(nextRotaCode.compareTo("TURN_RIGHT")==0)
					                		{
					                			iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.right));
					                		}
					                		
					                		tv_niavi_do.setText(String.valueOf((int)(curResult))+"º\n틀어졌습니다.");					    
					                		tv_navi_tofrom.setText(String.valueOf(curX)+"\n"+String.valueOf(curY));

					                		if(SC.doNum>2)
					                		{
					                			SC.doNum=0;
					                			
					                			
					                			myTTS.speak(String.valueOf((int)(curResult))+"도 틀어졌습니다.", TextToSpeech.QUEUE_ADD, null);
					                		}
					                		SC.doNum++;
					                		
					                		
					                		if(SC.meterNum>3)
					                		{
					                			SC.meterNum=0;
					                			myTTS.speak(String.valueOf(bungiLength), TextToSpeech.QUEUE_ADD, null);
					                		}
					                		SC.meterNum++;
					                		
					                		tv_navi_test.setText("미터 : " + String.valueOf(SC.meterNum) +"\n도넘 : " + String.valueOf(SC.doNum));
					                		
//					                		appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode );
//					                		myTTS.speak("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode , TextToSpeech.QUEUE_ADD, null);
					                	}
					                	else
					                	{
					                		SC.doNum=0;
					                		tv_navi_meter.setText(String.valueOf(bungiLength)+"후");

					                		tv_navi_test.setText(nextRotaCode);

					                		
					                		if(nextRotaCode.compareTo("TURN_LEFT")==0)
					                		{
					                			iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.left));
					                		}
					                		else if(nextRotaCode.compareTo("TURN_RIGHT")==0)
					                		{
					                			iv_navi_leftright.setImageDrawable(getResources().getDrawable(R.drawable.right));
					                		}
					                		
					                		tv_niavi_do.setText("올바른 방향입니다.\n"+String.valueOf((int)(curResult))+"º");
					                		
					                		tv_navi_tofrom.setText(String.valueOf(curX)+"\n"+String.valueOf(curY));

					                		if(SC.meterNum>2)
					                		{
					                			SC.meterNum=0;
					                			myTTS.speak(String.valueOf(bungiLength), TextToSpeech.QUEUE_ADD, null);
					                		}
					                		SC.meterNum++;

					                		tv_navi_test.setText("미터 : " + String.valueOf(SC.meterNum) +"\n도넘 : " + String.valueOf(SC.doNum));

					                		
//					                		appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode );
//					                		myTTS.speak("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode, TextToSpeech.QUEUE_ADD, null);				                		
					                	}
//				                	}
				                }
								
								
						        
							}
						});
					}
				}).start();
                
                
                
                
                
                
                
                // 3번 방향 60도벗어낫을경우 틀어짐알려줌
                if(checkReflashNum>=maxCheckReflashNum)
                {
                    appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,틀어짐 : " + curResult + "  , 다음방향" + nextRotaCode );
                    return;
                }

                // 분기도달시
                if(curBungiNum != befBungiNum)
                {
                    appendText(befRotaCode + " 방향으로 턴입니다");
                    return;
                }

//                setDirection("curResult : " + curResult);
                appendText("curResult : " + curResult);

                appendText("현재방향 : " + nowRotaCode + "  ,남은거리 : " + bungiLength + "\n" + "  ,옳바른 방향입니다 : (" + curResult + ")  , 다음방향" + nextRotaCode );


            }
            else if(TagString.compareTo("GPS")==0)
            {
            	
            	
            	
//                Log.i("JINIB","GPSC진입완료");

                String [] tmpStrArr = retStrForGPS.split("@");

                Log.i("retStrForGPS" , retStrForGPS);

                double[] tmpDoubleArr = new double[2];

                
                if(isFirst==true)
                {
                	endX = Double.parseDouble(tmpStrArr[0]);
                	endY = Double.parseDouble(tmpStrArr[1]);
                	
                	isFirst=false;
                	bt_main_getgps(null);
                	return ;
                }

                //바뀐 x,y값
                curX = tmpDoubleArr[0] = Double.parseDouble(tmpStrArr[0]);
                curY = tmpDoubleArr[1] = Double.parseDouble(tmpStrArr[1]);

//                appendText(tmpStrArr[0]+"  ,  "+tmpStrArr[1]);



                getRoute(tmpDoubleArr[0],tmpDoubleArr[1],endX,endY);


            }

        }
    }

    private void setDirection(String retStr) {

        Log.i("THIS",retStr);
    }


	@Override
	public void onInit(int status) {
        String tmpStr = "목적지 " + Destination+ "으로 길 안내를 시작합니다";
        myTTS.speak(tmpStr, TextToSpeech.QUEUE_ADD, null);
		
	}
    
}


// 버튼누름 - getMyLocation - GPS 가동 - changeGPSType - getRoute - 현 전 첫 관계로 세타계산