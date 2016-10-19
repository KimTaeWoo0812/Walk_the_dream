package com.ssm.walkthedream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
//Ŭ���̾�Ʈ ����
public class AndroidSocket extends Thread{

public final static int QUEUESIZE=100;
public static String queue[]=new String[100];
public DataInputStream input;
public DataOutputStream output;
public DataOutputStream out;
public Socket socket;
public String str;
public static int top=0,rear=0;
private static AndroidSocket _shared=null;
private boolean stopThread;
private static String beforMeg="";
public volatile static int hTop=0;
public volatile static int hRear=0;
public volatile static int hQueue[]=new int[20];
public static double latitude;
public static double longitude;
public int stairsCheck=0;
public static int port=5000;
StairsThread stairsThread;
//public String beforMeg="";
//public int reCount=0;
public static BluetoothSocket mSocket = null;
public static OutputStream mOutputStream = null;
public static InputStream mInputStream = null;
public static BluetoothAdapter btAdapter;

//�ɼ�
public static boolean OPTION_TTS = true;
public static boolean OPTION_Bluetooth = true;
public static boolean BluetoothConnected = false;
public static boolean OPTION_NAVI_TYPE = true;
public String id="";
public static synchronized AndroidSocket shared(){
   if(_shared==null){
      _shared=new AndroidSocket();
      _shared.start();
      
   }
   
   return _shared;
}

   public void run(){
      String host="192.168.43.30";
      //String host="192.168.200.28";
	   //String host="203.244.147.205";
       stopThread=true;
       try{
          //socket = new Socket(host,SavePort.port);
    	   socket = new Socket(host,port);
          input = new DataInputStream(socket.getInputStream());
          output = new DataOutputStream(socket.getOutputStream());
          stairsThread=new StairsThread();
          stairsThread.start();
          
          
          //A_CHANGE_NAME#
          String temp="A_CHANGE_NAME#";
          char cTemp[]=new char[128];
          int i;
          
          for(i=0;i<temp.length();i++)
             cTemp[i]=temp.charAt(i);
          
          for(i=0;i<id.length();i++)
             cTemp[i+21]=id.charAt(i);
          cTemp[i+21]='#';
          
          String strMsg="";
          
          for(i=0;i<128;i++)
             strMsg+=cTemp[i];
          
          SendMessage(strMsg);
          
            while(input != null&&stopThread){
               queue[top%QUEUESIZE]=input.readUTF();
               //B_LOC  ��ġ �䱸�Ҷ�
               if(queue[top%QUEUESIZE].charAt(0)=='B'&&queue[top%QUEUESIZE].charAt(1)=='_'&&queue[top%QUEUESIZE].charAt(2)=='L'&&queue[top%QUEUESIZE].charAt(3)=='O')
               {
            	 //String tmpStr = String.valueOf(a.lat);
                   //��ġ�ҷ����°� �־ ���� id������ �Բ� B_LOC�� ������ ������.
                   String strTemp="B_LOC";
                   char cMsg[]=new char[128];
//                   int i;
                   SendMessage("  "+queue[top%QUEUESIZE]);
                   for(i=0;i<strTemp.length();i++)
                      cMsg[i]=strTemp.charAt(i);
                   strTemp=Double.toString(latitude);
                   for(i=21;i<strTemp.length()+21;i++)
                      cMsg[i]=strTemp.charAt(i-21);
                   cMsg[i]='#';
                   strTemp=Double.toString(longitude);
                   for(i=41;i<strTemp.length()+41;i++)
                      cMsg[i]=strTemp.charAt(i-41);
                   cMsg[i]='#';
                   
                   for(i=61;queue[top%QUEUESIZE].charAt(i-40)!='#';i++)
                      cMsg[i]=queue[top%QUEUESIZE].charAt(i-40);
                   cMsg[i]='#';
                   cMsg[81]=queue[top%QUEUESIZE].charAt(41);
                   cMsg[82]='#';
                   cMsg[101]=queue[top%QUEUESIZE].charAt(61);
                   cMsg[102]='#';
                   strTemp="";
                   for(i=0;i<128;i++)
                      strTemp+=cMsg[i];
                   
                   SendMessage(strTemp);
                   continue;
               }
               
               //B_STAIRS# ��ֹ� �޴°�
               else if(queue[top%QUEUESIZE].charAt(0)=='B'&&queue[top%QUEUESIZE].charAt(1)=='_'&&queue[top%QUEUESIZE].charAt(2)=='S'&&queue[top%QUEUESIZE].charAt(3)=='T')
               {
            	   Log.i("msg:",queue[top%QUEUESIZE]);
                  if(queue[top%QUEUESIZE].charAt(21)=='1')//�ö󰡴� ���
                  {
                     //sendBroadcast(new Intent("arabiannight.tistory.com.sendreciver.stairsUps"));
                      hQueue[hTop%20]=10;
                      hTop++;
                  }
                  else if(queue[top%QUEUESIZE].charAt(21)=='2')//�������� ���
                  {
                     hQueue[hTop%20]=11;
                     hTop++;
                  }
                  
                  stairsCheck=1;
                  continue;
               }
               top++;
            }
       }catch(Exception e){
          e.printStackTrace();
       }
      
   }
   public void GiveGps()
   {
      
   }
   
   public boolean HasMessage(){
      if(top!=rear)//�޽��� �ִ�
         return false;
      else//�޽��� ����
         return true;
   }
   public synchronized void SendMessage(String strMessage){//�޽��� ������
      try{
         //beforMeg=strMessage;
         out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(strMessage);
      }catch(Exception e){
          e.printStackTrace();
      }
   }
   /*
    * Scanner sc = new Scanner(System.in);//������ �ٸ����
        String msg = "";

        while (output != null) {
            try {
                msg = sc.nextLine();
                if(msg.equals("exit"))
                    System.exit(0);
                 
                output.writeUTF("[" + name + "]" + msg);
            } catch (IOException e) {
            }
        }
    */
   public char[] GetMessage(){//�޽��� �ޱ�
      //queue[rear%QUEUESIZE]�� ĳ���Ϳ� �޾Ƽ� ��ȯ�Ѵ�.
      char[] charArray=new char[128];
      int i;
         //SendMessage("�ȵ���̵尡 �޾Ҵ�: "+queue[rear]);//
         
         for(i=0;i<queue[rear%QUEUESIZE].length();i++)
            charArray[i]=queue[rear%QUEUESIZE].charAt(i);
         if(charArray[0]=='A'&&charArray[1]=='_'&&charArray[2]=='A'&&charArray[3]=='G'&&charArray[4]=='A')
         {
            SendMessage(beforMeg);
            charArray=GetMessage();
         }
         rear++;
      return charArray;
   }

	public void CloseSocket() throws Exception {
		stopThread = false;
		input.close();
		output.close();
		out.close();
		_shared = null;
		if (OPTION_Bluetooth && BluetoothConnected)
			mOutputStream.close();
		socket.close();
	}
   
   //���� ��ġ�� ������ ��� ����
   public class StairsThread extends Thread{
      String temp;
      int i;
      public StairsThread(){
         temp="B_STAIRS#";
      }
      public void run(){
         for(;stopThread;){
            try {
               this.sleep(10000);
               
               char msg[]=new char[128];
               for(i=0;i<temp.length();i++)
                  msg[i]=temp.charAt(i);
               
               String lat="";
               String lon="";
               for(i=21;i<Double.toString(latitude).length()+21;i++)
               {
                  msg[i]=Double.toString(latitude).charAt(i-21);
                  msg[i+20]=Double.toString(longitude).charAt(i-21);
               }
               msg[i]='#';
               msg[i+20]='#';
               temp="";
               for(i=0;i<128;i++)
                  temp+=msg[i];
               
               SendMessage(temp);
               
               if(stairsCheck==1)
               {
                  stairsCheck=0;
                  this.sleep(10000);
               }
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }
   
   //������� ������ �߽�
   public void sendData(String msg) {
        try{
            // getBytes() : String�� byte�� ��ȯ
            // OutputStream.write : �����͸� ������ write(byte[]) �޼ҵ带 �����. byte[] �ȿ� �ִ� �����͸� �ѹ��� ����� �ش�.  
           mOutputStream.write(msg.getBytes());  // ���ڿ� ����.
          
        }catch(Exception e) { 
            e.printStackTrace();
        }
    }
   public void StartBluetoothReceiveThread()
   {
	   Log.i("#666","666");
      BluetoothClassReceiveThread receiveClass=new BluetoothClassReceiveThread();
       receiveClass.start();
   }
   //������� ������ ����
   public class BluetoothClassReceiveThread extends Thread{
       
       public BluetoothClassReceiveThread(){
    	   Log.i("#555","555");
       }
        public void run() {
           
            // interrupt() �޼ҵ带 �̿� �����带 �����Ű�� �����̴�. 
            // interrupt() �޼ҵ�� �ϴ� ���� ���ߴ� �޼ҵ��̴�.  
            // isInterrupted() �޼ҵ带 ����Ͽ� ���߾��� ��� �ݺ����� ������ �����尡 �����ϰ� �ȴ�. 
            // InputStream.available() : �ٸ� �����忡�� blocking �ϱ� ������ ���� �� �ִ� ���ڿ� ������ ��ȯ��.
            while(!Thread.currentThread().isInterrupted()) {
                try {
                   byte[] byteReceive=new byte[60];
                   mInputStream.read(byteReceive);
                   
                   
                    final String d= new String(byteReceive, "US-ASCII");
                    //sendData("2\n\n");
                    if(d.charAt(0)=='1')//�ö󰡴� ���
                    {
                    	Log.i("#�޾Ҵ� 1","222");
                       hQueue[hTop%20]=1;
                     hTop++;
                    }
                    else if(d.charAt(0)=='2')//�������� ���
                    {
                    	Log.i("#�޾Ҵ� 2","333");
                       hQueue[hTop%20]=2;
                     hTop++;
                    }
                } catch (Exception e) {    // ������ ���� �� ���� �߻�. 
                   // Toast.makeText(getApplicationContext(), "������ ���� �� ������ �߻� �߽��ϴ�.", Toast.LENGTH_LONG).show();
                   e.printStackTrace();
                }
            }
        }
        
    }
   
}

