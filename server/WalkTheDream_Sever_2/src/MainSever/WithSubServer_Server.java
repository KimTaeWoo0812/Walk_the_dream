package MainSever;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

//������Ȱ. ���������κ��� �޴´�
public class WithSubServer_Server extends Thread{
	private final static int QUEUESIZE=50;
	public HashMap<String, DataOutputStream> clients;
    private ServerSocket serverSocket;
    public volatile String queue[]=new String[QUEUESIZE];
    public volatile DataOutputStream addr[]=new DataOutputStream[QUEUESIZE];
    public volatile int top=0,rear=0;
    private static DataInputStream input;
    private DataOutputStream output;
    public int port;
    public String name="0";
    public volatile int lTop=0,lRear=0;
    public volatile String lQueue[]=new String[QUEUESIZE];
    WithSubServer_Client Get;
    DB Db;
    
    public WithSubServer_Server(DB db)
    {
    	Get=new WithSubServer_Client();
    	Get.start();
    	Db=db;
    	clients = new HashMap<String, DataOutputStream>();
        // ���� �����忡�� ������ ���̹Ƿ� ����ȭ
        Collections.synchronizedMap(clients);
    
        
    }
    @Override
    public void run(){
        
        Socket socket;
        System.out.println("server-1 socket run");
   
        try {
            // ������ ���� ����
            serverSocket = new ServerSocket(6002);
            System.out.println("������ ��� ����.");
 
            // Ŭ���̾�Ʈ�� ����Ǹ�
            while (true) {
                 //��� ������ �����ϰ� ������ ����(������ 1:1�θ� ����ȴ�)
            	System.out.println("Csocket "+top+" "+rear+" "+this.toString());
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                receiver.start();
                
                // ServerGet getMsg = new ServerGet(socket);
        		//	getMsg.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public synchronized void SendMessage(String strMessage){//�޽��� ������
		try{
			addr[rear%QUEUESIZE].writeUTF("���� "+strMessage);//
			System.out.println(addr[rear%QUEUESIZE]+" ���� ������ �Լ� ����: "+strMessage);
			rear++;
		}catch(Exception e){
    		e.printStackTrace();
		}
	}
	public void ChangeName(String id)
	{
		clients.put(id, addr[rear%QUEUESIZE]);
		name=id;
		System.out.println("HashMap�� put.. ID: "+id+" addr: "+addr[rear%QUEUESIZE]);
	}
	public DataOutputStream GetIdGiveAddr(String getId)
	{
		DataOutputStream val = null;
			try {
            	val=clients.get(getId);
            	System.out.println("GetIdGiveAddr.. ID: "+getId+" addr: "+val);
            } catch (Exception e) {
            }
        
		
		return val;
	}
	static String getTime() {
		long time = System.currentTimeMillis(); 
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date(time));
	}
    public void sendToAll(String message) {
        Iterator<String> it = clients.keySet().iterator();
      
        while (it.hasNext()) {
            try {
            	DataOutputStream dos = clients.get(it.next());
                dos.writeUTF(message);
            	
            } catch (Exception e) {
            }
        }
    }
    public synchronized boolean L_HasMessage(){
    	if(lTop!=lRear)//�޽��� �ִ�
    		return true;
    	
    	else//�޽��� ����
    		return false;
    }
    public synchronized boolean HasMessage(){
    	if(top!=rear)//�޽��� �ִ�
    		return false;
    	
    	else//�޽��� ����
    		return true;
    }
    /**
	 * �޽����� ���� ������ ������
	 * @param msg
	 */
	public void SendMesToNextServer(String msg)
	{
		Get.SendMessage(msg);
	}
    class ServerReceiver extends Thread {
		Socket socket;

    	
        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("����");
			}
        }
        @Override
        public void run() {
        	System.out.println("input output ���� input: "+input);
                
            name="";
			
            try {
            	//socket.getInetAddress() ����� Ŭ���̾�Ʈ �ּ�
            	//socket.getPort() ����� Ŭ���̾�Ʈ ��Ʈ
            	//clients.size(); ���� ������ Ŭ���̾�Ʈ ��
            	//sendToAll(String); �޽��� ����
            	//dos.writeUTF(String); �޽��� ����
            	//input.readUTF(); ���� �޽���
                //clients.put(name, output);
                System.out.println(this.toString());
                // �޽����� �޴ºκ�
                String temp="";
                while (input != null) {

                	//System.out.println(output+"   "+input.readUTF());
                	temp=input.readUTF();
                	lQueue[lTop%QUEUESIZE]=temp;
                	addr[lTop%QUEUESIZE]=output;
                	System.out.println("���꼭��2 �޽����� �޾Ҵ�. "+lQueue[lRear%QUEUESIZE] +" "+lTop+" "+lRear);
                	
                	lTop++;
                	
                }
            } catch (IOException e) {
			} finally {
            }
        }
        
        
}
}