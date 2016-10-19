package MainSever;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

//클라이언트 소켓. 다음서버로 준다
public class WithSubServer_Client extends Thread{

	private final static int QUEUESIZE=50;
	private volatile static String queue[]=new String[50];
	private DataInputStream input;
	private static DataOutputStream output;
	private DataOutputStream out;
	private Socket socket;
	private volatile static int top=0,rear=0;
	private boolean stopThread;
private int stairsCheck=0;
public WithSubServer_Client(){
	
}

	public void run(){
		try {
			this.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String host="192.168.43.30";
		String host="192.168.43.183";
    	stopThread=true;
    	try{
    		socket = new Socket(host,6002);
    		input = new DataInputStream(socket.getInputStream());
    		output = new DataOutputStream(socket.getOutputStream());
    		
    		SendMessage("서브서버1 보내기");
            while(input != null&&stopThread){
            	queue[top%QUEUESIZE]=input.readUTF();
            	
            	top++;
            }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public boolean HasMessage(){
		if(top!=rear)//메시지 있다
			return false;
		else//메시지 없다
			return true;
	}
	public void SendMessage(String strMessage){//메시지 보내기
		try{
			//beforMeg=strMessage;
			out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(strMessage);
				System.out.println("클라: "+strMessage+"를 서버로 보낸다");
		}catch(Exception e){
    		e.printStackTrace();
		}
	}
	public char[] GetMessage(){//메시지 받기
		//queue[rear%QUEUESIZE]을 캐릭터에 받아서 반환한다.
		char[] charArray=new char[128];
		int i;
			//SendMessage("안드로이드가 받았다: "+queue[rear]);//
		
		System.out.println("클라: 받는다. "+queue[rear%QUEUESIZE]+" "+top+" "+rear);
			for(i=0;i<queue[rear%QUEUESIZE].length();i++)
				charArray[i]=queue[rear%QUEUESIZE].charAt(i);
			rear++;
		return charArray;
	}
	
}
