package MainSever;

import java.io.DataOutputStream;



public class MainClass{
	
	public static int subPort=5000;
	private static int HowManySubSuver = 2;
	private static int subSuverPort[];
	private static int temp=2;
	private final static int QUEUESIZE=300;
	public static int port=5000;
	public static void main(String[] args) {
    	DB Db=new DB();
    	SocketThread soccket=new SocketThread(Db);
    	soccket.start();
    	//WithSubClassSocket subSoccket=new WithSubClassSocket();
    	//subSoccket.start();
    	subSuverPort=new int[HowManySubSuver+1];
    	subSuverPort[0]=5001;
    	subSuverPort[1]=5002;
    	temp=2;
    	for(;;)
    	{
    		//System.out.println(soccket.top+"  "+soccket.rear);
    		if(soccket.HasMessage())
	        {
	        	String strmessage=soccket.queue[soccket.rear%QUEUESIZE];
	        	char message[]=new char[128];
	        	int i;
	        	for(i=0;i<strmessage.length()&&i<128;i++)
                	message[i]=strmessage.charAt(i);
	        	
	        	System.out.println("msg : "+ new String(message));
	        	
	        	
	        	//OUT �α׾ƿ� ������ DB�� isLogin 0���� ����
	        	if(message[0]=='O'&&message[1]=='U'&&message[2]=='T')
	        	{
	        		String strName="";
	        		for(i=21; message[i]!='#';i++)
	        			strName+=message[i];
	        		//if(message[21]!='0')
	        		//	Db.LoginOut(strName);
	        	}
	        	
	        	//A_ALOGIN#
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='A'&&message[3]=='L'&&message[4]=='O'&&message[5]=='G')
	        	{
	        		String sendId="";

	        		for(i=21;message[i]!='#';i++)
	        			sendId+=message[i];
	        		
	        		
	        		soccket.ChangeName(sendId);
        			
        			
        			strmessage="";
        			//21���� �α��� ���ɿ����� '1', 41������ ��Ʈ��ȣ
        			message[21]='1';
        			char arrPort[]=new char[128];
        			int num=subSuverPort[temp%HowManySubSuver];//������ �ΰ��� �����ϱ� 0~1�� �Դٰ����Ѵ�
        			temp++;
        			String strPort = Integer.toString(num);//����
        			
        			arrPort=strPort.toCharArray();
        			System.out.println("port : "+strPort+"  "+subSuverPort[HowManySubSuver%temp]);
        			
        			for(i=41;i<45;i++)
        				message[i]=strPort.charAt(i-41);
        			
        			for(i=0;i<128;i++)
        				strmessage+=message[i];
        			
        			
        			soccket.SendMessage(strmessage);
	        		
	        		
	        	}
	        	//A_LOGIN#
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='L'&&message[3]=='O'&&message[4]=='G')
	        	{
	        		//��� ���Ժκ�
	        		String sendId="";
	        		String sendPw="";
	        		int value=0;
	        		
	        		for(i=21;message[i]!='#';i++)
	        			sendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			sendPw+=message[i];
	        		value=Db.Login(sendId,sendPw);
	        		
	        		
	        		//1 ������ �α���.
	        		//���� 1�̶��
	        		if(value==1)
	        		{
	        			soccket.ChangeName(sendId);
	        			
	        			
	        			strmessage="";
	        			//21���� �α��� ���ɿ����� '1', 41������ ��Ʈ��ȣ
	        			message[21]='1';
	        			char arrPort[]=new char[128];
	        			int num=subSuverPort[temp%HowManySubSuver];//������ �ΰ��� �����ϱ� 0~1�� �Դٰ����Ѵ�
	        			temp++;
	        			String strPort = Integer.toString(num);//����
	        			
	        			arrPort=strPort.toCharArray();
	        			System.out.println("port : "+strPort+"  "+subSuverPort[HowManySubSuver%temp]);
	        			
	        			for(i=41;i<45;i++)
	        				message[i]=strPort.charAt(i-41);
	        			
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			
	        			
	        			soccket.SendMessage(strmessage);
	        			
	        			//subPort++;
	        			//���꼭���� ����
	        			//������Ʈ�� ���� ++
	        		}
	        		else if(value==2)
	        		{
	        			strmessage="";
	        			message[21]='2';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        		}
	        		else if(value==4)
	        		{
	        			strmessage="";
	        			message[21]='4';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        		}
	        		else{//Ʋ����й�ȣ
	        			strmessage="";
	        			message[21]='3';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        			
	        		}
	        	}
	        	//A_IDCHECK
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='I'&&message[3]=='D'&&message[4]=='C')
	        	{
	        		String strTemp="";
	        		
	        		for(i=21;message[i]!='#';i++)
	        			strTemp+=message[i];
	        		
	        		int idCheck= Db.IdCheck(strTemp);
	        		System.out.println("idCheck: "+idCheck);
	        		if(idCheck==1)//���̵� ��� ����
	        		{
	        			strmessage="";
	        			message[21]='1';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        		}
	        		else//���̵� ��� �Ұ���
	        		{
	        			strmessage="";
	        			message[21]='2';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        		}
	        		
	        	}
	        	//A_JOIN_US
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='J'&&message[3]=='O'&&message[4]=='I')
	        	{
	        		String strTemp="";
	        		String sendId="";
	        		String sendPw="";
	        		int value=0;
	        		
	        		for(i=21;message[i]!='#';i++)
	        			sendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			sendPw+=message[i];
	        		value=Db.JoinUs(sendId,sendPw);
	        		if(value==1)//���� ����
	        		{
	        			message[21]='1';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strTemp+=message[i];
	        			soccket.SendMessage(strTemp);
	        			soccket.SendMessage(strTemp);
	        		}
	        		else//����
	        		{
	        			message[21]='2';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strTemp+=message[i];
	        			soccket.SendMessage(strTemp);
	        		}
	        	}
	        	//A_SET_FRIENDS
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='S'&&message[3]=='E'&&message[4]=='T'&&message[5]=='_'&&message[6]=='F'&&message[7]=='R')
	        	{
	        		int j;
	        		String strMessage="";
	        		char cMsg[]=new char[60];
	        		for(i=21;message[i]!='#';i++)
	        			strMessage+=message[i];
	        		
	        		cMsg=Db.SetFriends(strMessage);
	        		
	        		int k;
	    			for(int tempNum=0;;tempNum+=40)//0~ id, 20~ ���ӿ���       �迭�� 600�̴ϱ� 15����� ���� �� �ִ�.
	    			{
	    				strMessage="";
	    				for(i=0;i<40;i++)
	    					strMessage+=cMsg[i+tempNum];
	    				
	    				soccket.SendMessage(strMessage);
	    				if(cMsg[tempNum]=='0')
	    					break;
	    			}
	    			
	        	}
	        	
	        	
	        	////////////////////////////////////////
	        	////////////////////////////////////////
	        	////////////////////////////////////////
	        	
	        	//A_SET_WAY
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='S'&&message[3]=='E'&&message[4]=='T'&&message[5]=='_'&&message[6]=='W'&&message[7]=='A')
	        	{
	        		int j;
	        		String strMessage="";
	        		char cMsg[]=new char[60];
	        		for(i=21;message[i]!='#';i++)
	        			strMessage+=message[i];
	        		
	        		cMsg=Db.SetWay(strMessage);
	        		
	        		int k;
	    			for(int tempNum=0;;tempNum+=40)//0~ id, 20~ ���ӿ���       �迭�� 600�̴ϱ� 15����� ���� �� �ִ�.
	    			{
	    				strMessage="";
	    				for(i=0;i<40;i++)
	    					strMessage+=cMsg[i+tempNum];
	    				
	    				soccket.SendMessage(strMessage);
	    				if(cMsg[tempNum]=='0')
	    					break;
	    			}
	    			
	        	}
	        	
	        	//A_LOCATION
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='L'&&message[3]=='O'&&message[4]=='C'&&message[5]=='A'&&message[6]=='T')
	        	{
	        		String strFriendId="";
	        		String myId="";
	        		for(i=21;message[i]!='#';i++)
	        			strFriendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			myId+=message[i];
	        		//String friendId=Db.GetNumGiveId(strNum);
	        		
	        		if("".equals(strFriendId))
	        			soccket.SendMessage("error");
	        		DataOutputStream val = soccket.GetIdGiveAddr(strFriendId);
	        		
	        		String strMessage="";
	        		String strTemp="B_LOC";
	        		char cTemp[]=new char[50];
	        		for(i=0;i<strTemp.length();i++)
        				cTemp[i]=strTemp.charAt(i);
	        		
	        		//if("".equals(val)){//���� ������ ���� ���̵� �䱸��(����)
	        		if("".equals(val)){
	        			cTemp[21]='0';
	        			cTemp[22]='#';
	        			
	        			for(i=0;i<49;i++)
	        				strMessage+=cTemp[i];
	        			soccket.SendMessage(strMessage);
	        		}
	        		else
	        		{
	        			soccket.addr[soccket.rear%QUEUESIZE]=val;//������ �ּҸ� ģ����
	        			
	        			for(i=21;i<myId.length()+21;i++)
	        				cTemp[i]=myId.charAt(i-21);
	        			cTemp[i]='#';
	        		
	        			for(i=0;i<49;i++)
	        				strMessage+=cTemp[i];
	        		
	        			System.out.println("������ B_LOC: "+val+" ������: "+strMessage);
	        			soccket.SendMessage(strMessage);
	        		}
	        	}
	        	//B_LOC
	        	if(message[0]=='B'&&message[1]=='_'&&message[2]=='L'&&message[3]=='O'&&message[4]=='C')
	        	{
	        		String strId="";

	        		for(i=61;message[i]!='#';i++)
	        			strId+=message[i];
	        		
	        		
	        		DataOutputStream val = soccket.GetIdGiveAddr(strId);
	        		soccket.addr[soccket.rear%QUEUESIZE]=val;
	        		//ģ�� �ּҷ� ���ÿϷ� ������ ��
	        		String strMessage="";
	        		String strTemp="A_LOCATION";
	        		
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			message[i]=strTemp.charAt(i);
	        		message[i]='#';
	        		
	        		for(i=0;i<60;i++)
	        			strMessage+=message[i];
	        		
	        		soccket.SendMessage(strMessage);
	        	}
	        	//A_DELETEFRIEND
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='D'&&message[3]=='E'&&message[4]=='L'&&message[5]=='E'&&message[6]=='T'&&message[7]=='E'&&message[8]=='F')
	        	{
	        		String strFriendId="";
	        		String myId="";
	        		for(i=21;message[i]!='#';i++)
	        			strFriendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			myId+=message[i];
	        		
	        		//String friendId=Db.GetNumGiveId(strNum);
	        		
	        	
	        		char val=Db.DeleteFriend(strFriendId, myId);
	        		
	        		String strMessage="";
	        		String strTemp="A_DELETEFRIEND";
	        		char cTemp[]=new char[50];
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			cTemp[i]=strTemp.charAt(i);
	        		cTemp[21]=val;
	        		cTemp[22]='#';
	        		
	        		for(i=0;i<49;i++)
	        			strMessage+=cTemp[i];
	        		
	        		soccket.SendMessage(strMessage);
	        	}
	        	
	        	//A_WAYDELETE
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='W'&&message[3]=='A'&&message[4]=='Y'&&message[5]=='D'&&message[6]=='E'&&message[7]=='L'&&message[8]=='E')
	        	{
	        		String id="";
	        		String wayName="";
	        		for(i=21;message[i]!='#';i++)
	        			id+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			wayName+=message[i];
	        		
	        		//String friendId=Db.GetNumGiveId(strNum);
	        		
	        	
	        		char val=Db.DeleteWayOfDB(id, wayName);
	        		
	        		String strMessage="";
	        		String strTemp="A_WAYDELETE";
	        		char cTemp[]=new char[50];
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			cTemp[i]=strTemp.charAt(i);
	        		cTemp[21]=val;
	        		cTemp[22]='#';
	        		
	        		for(i=0;i<49;i++)
	        			strMessage+=cTemp[i];
	        		
	        		soccket.SendMessage(strMessage);
	        	}
	        	
	        	//A_ADDFRIEND
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='A'&&message[3]=='D'&&message[4]=='D'&&message[5]=='F'&&message[6]=='R'&&message[7]=='I'&&message[8]=='E')
	        	{
	        		String strFriendId="";
	        		String myId="";
	        		for(i=21;message[i]!='#';i++)
	        			strFriendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			myId+=message[i];
	        	
	        		char val=Db.AddFriend(strFriendId, myId);
	        		
	        		String strMessage="";
	        		String strTemp="A_ADDFRIEND";
	        		char cTemp[]=new char[50];
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			cTemp[i]=strTemp.charAt(i);
	        		cTemp[21]=val;
	        		cTemp[22]='#';
	        		
	        		for(i=0;i<49;i++)
	        			strMessage+=cTemp[i];
	        		
	        		soccket.SendMessage(strMessage);
	        	}
	        
	        	//A_ADDWAY
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='A'&&message[3]=='D'&&message[4]=='D'&&message[5]=='W'&&message[6]=='A'&&message[7]=='Y')
	        	{
	        		String id="";
	        		String destination="";
	        		String lat="";
	        		String lon="";
	        		//id
	        		for (i = 21; message[i]!='#'; i++)
	        			id+= message[i];
	        		// ������
	        		for (i = 41; message[i]!='#'; i++)
	        			destination+=  message[i];
	        		// ��ǥ
	        		for (i = 61; message[i]!='#'; i++)
	        			lat+=message[i];
	        		// ��ǥ
	        		for (i = 81; message[i]!='#'; i++)
	        			lon+=message[i];
	        		
	        		char val=Db.SaveAddWay(id, destination, lat, lon);
	        		
	        		String strMessage="";
	        		String strTemp="A_ADDWAY";
	        		char cTemp[]=new char[51];
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			cTemp[i]=strTemp.charAt(i);
	        		cTemp[21]=val;
	        		cTemp[22]='#';
	        		
	        		for(i=0;i<50;i++)
	        			strMessage+=cTemp[i];
	        		
	        		soccket.SendMessage(strMessage);
	        			
	        	}
	        	
	        	
	        
	        	soccket.rear++;
	        }
    	}
    	
    }
}
