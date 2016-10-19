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
	        	
	        	
	        	//OUT 로그아웃 했을때 DB에 isLogin 0으로 수정
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
        			//21번에 로그인 가능여부인 '1', 41번부터 포트번호
        			message[21]='1';
        			char arrPort[]=new char[128];
        			int num=subSuverPort[temp%HowManySubSuver];//지금은 두개로 했으니깐 0~1을 왔다갔다한다
        			temp++;
        			String strPort = Integer.toString(num);//수정
        			
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
	        		//디비 삽입부분
	        		String sendId="";
	        		String sendPw="";
	        		int value=0;
	        		
	        		for(i=21;message[i]!='#';i++)
	        			sendId+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			sendPw+=message[i];
	        		value=Db.Login(sendId,sendPw);
	        		
	        		
	        		//1 보내면 로그인.
	        		//만약 1이라면
	        		if(value==1)
	        		{
	        			soccket.ChangeName(sendId);
	        			
	        			
	        			strmessage="";
	        			//21번에 로그인 가능여부인 '1', 41번부터 포트번호
	        			message[21]='1';
	        			char arrPort[]=new char[128];
	        			int num=subSuverPort[temp%HowManySubSuver];//지금은 두개로 했으니깐 0~1을 왔다갔다한다
	        			temp++;
	        			String strPort = Integer.toString(num);//수정
	        			
	        			arrPort=strPort.toCharArray();
	        			System.out.println("port : "+strPort+"  "+subSuverPort[HowManySubSuver%temp]);
	        			
	        			for(i=41;i<45;i++)
	        				message[i]=strPort.charAt(i-41);
	        			
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			
	        			
	        			soccket.SendMessage(strmessage);
	        			
	        			//subPort++;
	        			//서브서버로 연결
	        			//서브포트로 이후 ++
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
	        		else{//틀린비밀번호
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
	        		if(idCheck==1)//아이디 사용 가능
	        		{
	        			strmessage="";
	        			message[21]='1';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strmessage+=message[i];
	        			soccket.SendMessage(strmessage);
	        		}
	        		else//아이디 사용 불가능
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
	        		if(value==1)//가입 성공
	        		{
	        			message[21]='1';
	        			message[22]='#';
	        			for(i=0;i<128;i++)
	        				strTemp+=message[i];
	        			soccket.SendMessage(strTemp);
	        			soccket.SendMessage(strTemp);
	        		}
	        		else//실패
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
	    			for(int tempNum=0;;tempNum+=40)//0~ id, 20~ 접속여부       배열이 600이니깐 15명까지 보낼 수 있다.
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
	    			for(int tempNum=0;;tempNum+=40)//0~ id, 20~ 접속여부       배열이 600이니깐 15명까지 보낼 수 있다.
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
	        		
	        		//if("".equals(val)){//접속 중이지 않은 아이디 요구시(에러)
	        		if("".equals(val)){
	        			cTemp[21]='0';
	        			cTemp[22]='#';
	        			
	        			for(i=0;i<49;i++)
	        				strMessage+=cTemp[i];
	        			soccket.SendMessage(strMessage);
	        		}
	        		else
	        		{
	        			soccket.addr[soccket.rear%QUEUESIZE]=val;//목적지 주소를 친구로
	        			
	        			for(i=21;i<myId.length()+21;i++)
	        				cTemp[i]=myId.charAt(i-21);
	        			cTemp[i]='#';
	        		
	        			for(i=0;i<49;i++)
	        				strMessage+=cTemp[i];
	        		
	        			System.out.println("보내기 B_LOC: "+val+" 데이터: "+strMessage);
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
	        		//친구 주소로 셋팅완료 보내면 댐
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
	        		// 목적지
	        		for (i = 41; message[i]!='#'; i++)
	        			destination+=  message[i];
	        		// 좌표
	        		for (i = 61; message[i]!='#'; i++)
	        			lat+=message[i];
	        		// 좌표
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
