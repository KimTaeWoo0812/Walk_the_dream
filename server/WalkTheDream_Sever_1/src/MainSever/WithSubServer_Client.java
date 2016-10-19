package MainSever;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

//Ŭ���̾�Ʈ ����. ���������� �ش�
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
    		
    		SendMessage("���꼭��1 ������");
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
	public void SendMessage(String strMessage){//�޽��� ������
		try{
			//beforMeg=strMessage;
			out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(strMessage);
				System.out.println("Ŭ��: "+strMessage+"�� ������ ������");
		}catch(Exception e){
    		e.printStackTrace();
		}
	}
	public char[] GetMessage(){//�޽��� �ޱ�
		//queue[rear%QUEUESIZE]�� ĳ���Ϳ� �޾Ƽ� ��ȯ�Ѵ�.
		char[] charArray=new char[128];
		int i;
			//SendMessage("�ȵ���̵尡 �޾Ҵ�: "+queue[rear]);//
		
		System.out.println("Ŭ��: �޴´�. "+queue[rear%QUEUESIZE]+" "+top+" "+rear);
			for(i=0;i<queue[rear%QUEUESIZE].length();i++)
				charArray[i]=queue[rear%QUEUESIZE].charAt(i);
			rear++;
		return charArray;
	}
	
}
