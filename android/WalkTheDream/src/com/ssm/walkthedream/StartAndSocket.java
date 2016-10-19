package com.ssm.walkthedream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
//Ŭ���̾�Ʈ ����
public class StartAndSocket extends Thread{

public final static int QUEUESIZE=100;
public static String queue[]=new String[100];
public DataInputStream input;
public DataOutputStream output;
public DataOutputStream out;
public Socket socket;
public String str;
public static int top=0,rear=0;
private static StartAndSocket _shared=null;
private boolean stopThread;
private static String beforMeg="";
public volatile static int hTop=0;
public volatile static int hRear=0;
public volatile static int hQueue[]=new int[20];
public static double latitude;
public static double longitude;
public int stairsCheck=0;
public static int port=5000;
//public String beforMeg="";
//public int reCount=0;
public static BluetoothSocket mSocket = null;
public static OutputStream mOutputStream = null;
public static InputStream mInputStream = null;
public static BluetoothAdapter btAdapter;


public StartAndSocket(int port){
	this.port=port;
}

	public void run(){
//		String host="192.168.43.30";
		String host="192.168.43.183";
    	stopThread=true;
    	try{
    		socket = new Socket(host,port);
    		input = new DataInputStream(socket.getInputStream());
    		output = new DataOutputStream(socket.getOutputStream());
    		
            while(input != null&&stopThread){
            	queue[top%QUEUESIZE]=input.readUTF();
            	
            	top++;
            }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
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
	public void CloseSocket() throws Exception{
		input.close();
		output.close();
		socket.close();
		stopThread=false;
	}

	
	
}


