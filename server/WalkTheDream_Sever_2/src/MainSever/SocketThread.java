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
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();//������Ǯ ����.
    
    public SocketThread(DB db)
    {
    	Db=db;
    	clients = new HashMap<String, DataOutputStream>();
        // ���� �����忡�� ������ ���̹Ƿ� ����ȭ
        Collections.synchronizedMap(clients);
    }
    @Override
    public void run(){
        
        Socket socket;
        System.out.println("server socket run");
   
        try {
            // ������ ���� ����
            serverSocket = new ServerSocket(5002);
            System.out.println("������ ���۵Ǿ����ϴ�.");
 
            // Ŭ���̾�Ʈ�� ����Ǹ�
            while (true) {
                 //��� ������ �����ϰ� ������ ����(������ 1:1�θ� ����ȴ�)
            	System.out.println("Csocket "+top+" "+rear+" "+this.toString());
                socket = serverSocket.accept();
                ServerReceiver receiver = new ServerReceiver(socket);
                //receiver.start();
                executor.execute(receiver);
                System.out.println(getTime()+" ���Ӽ���!");
        			
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public synchronized void SendMessage(String strMessage){//�޽��� ������
		try{
			addr[rear%QUEUESIZE].writeUTF(strMessage);//
			System.out.println(addr[rear%QUEUESIZE]+" ���� ������ �Լ� ����: "+strMessage);
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
	/**�ؽ��ʿ��� ���̵� �˻��ؼ� ã���� ���̵� �ѱ��, ������ 0�� �ѱ��.
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
	
	
	public DataOutputStream ChangeName12(String string){//���̵� �ް� Ŭ���̾�Ʈ ��ȣ�� �ش�
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
    	if(top!=rear)//�޽��� �ִ�
    		return true;
    	
    	else//�޽��� ����
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
                	queue[top%QUEUESIZE]=temp;
                	addr[top%QUEUESIZE]=output;
                	//Ŭ���̾�Ʈ�� ���� ��ġ�� ������,
                	if(temp.charAt(0)=='B'&&temp.charAt(1)=='_'&&temp.charAt(2)=='S'&&temp.charAt(3)=='T'&&temp.charAt(4)=='A')
                	{
                		//DB�� ��ȸ�ϰ� ������ ����� �ش�
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

							if (cTemp == '1' || cTemp == '2')// �ö󰡴°�
								output.writeUTF(strTemp);
						}
                		continue;
                	}
                	
                	if("".equals(queue[top%QUEUESIZE])){
                	
                		System.out.println("NULL�� ���Դ�!!");
                		//A_AGAIN
                		SendMessage("A_AGAIN");
                		//continue;
                	}
                	//if(queue[top].length()>3)
                	top++;
                	
                }
            } catch (IOException e) {
			} finally {
                // ������ ����Ǹ�
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
                        + socket.getPort() + "]" + "����  �������ϴ�.");
                System.out.println("���� " + clients.size() + "���� ���� ���Դϴ�.");
                // �� ������� ��ο��� ���� ����
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