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
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class SocketThread extends Thread{
	private final static int QUEUESIZE=300;
	public HashMap<String, DataOutputStream> clients;
    private ServerSocket serverSocket;
    public volatile String queue[]=new String[QUEUESIZE];
    public volatile DataOutputStream addr[]=new DataOutputStream[QUEUESIZE];
    public volatile int top=0,rear=0;
    public int port;
    public String name="0";
    DB Db;
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();//스레드풀 생성.
    
    public SocketThread(DB db)
    {
    	Db=db;
    	clients = new HashMap<String, DataOutputStream>();
        // 여러 스레드에서 접근할 것이므로 동기화
        Collections.synchronizedMap(clients);
    }
    @Override
    public void run(){
        
        Socket socket;
        System.out.println("server socket run");
   
        try {
            // 리스너 소켓 생성
            serverSocket = new ServerSocket(5002);
            System.out.println("서버가 시작되었습니다.");
 
            // 클라이언트와 연결되면
            while (true) {
                 //통신 소켓을 생성하고 스레드 생성(소켓은 1:1로만 연결된다)
            	System.out.println("Csocket "+top+" "+rear+" "+this.toString());
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                //receiver.start();
                executor.execute(receiver);
                System.out.println(getTime()+" 접속성공!");
        			
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public synchronized void SendMessage(String strMessage){//메시지 보내기
		try{
			addr[rear%QUEUESIZE].writeUTF(strMessage);//
			System.out.println(addr[rear%QUEUESIZE]+" 에게 보내는 함수 실행: "+strMessage);
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
	/**해쉬맵에서 아이디를 검색해서 찾으면 아이디를 넘기고, 없으면 0을 넘긴다.
	 */
	
	public String Find_ID_AtTheHashmap(String id)
	{
		Set key = clients.keySet();

		  for (Iterator iterator = key.iterator(); iterator.hasNext();)
		  {
			  String keyName = (String) iterator.next();
			  if(id.equals(keyName))
			  {
				  return keyName;
			  }
		  }
		return "1";
	}
	
	
	public DataOutputStream ChangeName12(String string){//아이디를 받고 클라이언트 번호를 준다
		Iterator iterator = clients.keySet().iterator();
		DataOutputStream val = null;
		while (iterator.hasNext()) {
            try {
            	val=clients.get(string);
            	
            } catch (Exception e) {
            }
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
    public boolean HasMessage(){
    	if(top!=rear)//메시지 있다
    		return true;
    	
    	else//메시지 없다
    		return false;
    }
    
    class ServerReceiver extends Thread {
		Socket socket;

    	DataInputStream input;
    	DataOutputStream output;
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
                	queue[top%QUEUESIZE]=temp;
                	addr[top%QUEUESIZE]=output;
                	//클라이언트의 현재 위치를 받으면,
                	if(temp.charAt(0)=='B'&&temp.charAt(1)=='_'&&temp.charAt(2)=='S'&&temp.charAt(3)=='T'&&temp.charAt(4)=='A')
                	{
                		//DB에 조회하고 있으면 결과를 준다
                		String strLat="";
                		String strLon="";
                		//Double.valueOf(str).doubleValue();
                		
                		for(int i=21;temp.charAt(i)!='#';i++)
                		{
                			strLat+=temp.charAt(i);
                			strLon+=temp.charAt(i+20);
                		}
                		double lat=Double.valueOf(strLat).doubleValue();
                		double lon=Double.valueOf(strLon).doubleValue();
                		
                		if ('0' != temp.charAt(21))
						{
							char cTemp = Db.Stairs(lat, lon);
							String strTemp = "B_STAIRS#";
							char msg[] = new char[128];
							for (int i = 0; i < strTemp.length(); i++)
								msg[i] = strTemp.charAt(i);
							msg[21] = cTemp;
							strTemp = "";
							for (int i = 0; i < 128; i++)
								strTemp += msg[i];

							if (cTemp == '1' || cTemp == '2')// 올라가는거
								output.writeUTF(strTemp);
						}
                		continue;
                	}
                	
                	if("".equals(queue[top%QUEUESIZE])){
                	
                		System.out.println("NULL이 들어왔다!!");
                		//A_AGAIN
                		SendMessage("A_AGAIN");
                		//continue;
                	}
                	//if(queue[top].length()>3)
                	top++;
                	
                }
            } catch (IOException e) {
			} finally {
                // 접속이 종료되면
				String strTemp = "OUT";
    			char cTemp[]=new char[40];
    			int j;
    			
    			for(j=0;j<strTemp.length();j++)
    				cTemp[j]=strTemp.charAt(j);
    			String strMsg="";
    			
    			name+='#';
				for(j=21;j<name.length()+21;j++)
					cTemp[j]=name.charAt(j-21);
				//cTemp[j]='#';
				for(j=0;j<40;j++)
					strMsg+=cTemp[j];
            	queue[top%QUEUESIZE]=strMsg;
            	addr[top%QUEUESIZE]=output;
            	top++;
                clients.remove(name);
                Db.LoginOut(name);
                System.out.println(getTime()+" "+name + "[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "님이  나갔습니다.");
                System.out.println("현재 " + clients.size() + "명이 접속 중입니다.");
                // 이 방식으로 모두에게 정보 전송
              // if(name=="2")
             //   {
               // Iterator<String> it = clients.keySet().iterator();
                //DataOutputStream dos = clients.get(it.next());
             //   try {
                	//dos.writeUTF("2323");
                	//System.out.println("gggggggg");
                	//} catch (IOException e) {
                	//e.printStackTrace();
                	//System.out.println("aaaaaaaaa");
                	//}
              //  }	
                	
            }
        }
        
        
}
}