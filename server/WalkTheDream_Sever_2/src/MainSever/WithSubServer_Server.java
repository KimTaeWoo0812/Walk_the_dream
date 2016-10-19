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

//서버역활. 이전서버로부터 받는다
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
        // 여러 스레드에서 접근할 것이므로 동기화
        Collections.synchronizedMap(clients);
    
        
    }
    @Override
    public void run(){
        
        Socket socket;
        System.out.println("server-1 socket run");
   
        try {
            // 리스너 소켓 생성
            serverSocket = new ServerSocket(6002);
            System.out.println("서버간 통신 시작.");
 
            // 클라이언트와 연결되면
            while (true) {
                 //통신 소켓을 생성하고 스레드 생성(소켓은 1:1로만 연결된다)
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
	public synchronized void SendMessage(String strMessage){//메시지 보내기
		try{
			addr[rear%QUEUESIZE].writeUTF("서버 "+strMessage);//
			System.out.println(addr[rear%QUEUESIZE]+" 에게 보내는 함수 실행: "+strMessage);
			rear++;
		}catch(Exception e){
    		e.printStackTrace();
		}
	}
	public void ChangeName(String id)
	{
		clients.put(id, addr[rear%QUEUESIZE]);
		name=id;
		System.out.println("HashMap에 put.. ID: "+id+" addr: "+addr[rear%QUEUESIZE]);
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
    	if(lTop!=lRear)//메시지 있다
    		return true;
    	
    	else//메시지 없다
    		return false;
    }
    public synchronized boolean HasMessage(){
    	if(top!=rear)//메시지 있다
    		return false;
    	
    	else//메시지 없다
    		return true;
    }
    /**
	 * 메시지를 다음 서버로 보낸다
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
				System.out.println("에러");
			}
        }
        @Override
        public void run() {
        	System.out.println("input output 생성 input: "+input);
                
            name="";
			
            try {
            	//socket.getInetAddress() 연결된 클라이언트 주소
            	//socket.getPort() 연결된 클라이언트 포트
            	//clients.size(); 현재 접속한 클라이언트 수
            	//sendToAll(String); 메시지 전송
            	//dos.writeUTF(String); 메시지 전송
            	//input.readUTF(); 받은 메시지
                //clients.put(name, output);
                System.out.println(this.toString());
                // 메시지를 받는부분
                String temp="";
                while (input != null) {

                	//System.out.println(output+"   "+input.readUTF());
                	temp=input.readUTF();
                	lQueue[lTop%QUEUESIZE]=temp;
                	addr[lTop%QUEUESIZE]=output;
                	System.out.println("서브서버2 메시지를 받았다. "+lQueue[lRear%QUEUESIZE] +" "+lTop+" "+lRear);
                	
                	lTop++;
                	
                }
            } catch (IOException e) {
			} finally {
            }
        }
        
        
}
}