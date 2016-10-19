package MainSever;

import java.io.DataOutputStream;



public class MainClass{
	
	public static int port=5000;
	public static int subPort=5001;
	private final static int QUEUESIZE=300;
	public static char StartServerNum='1';
	public static void main(String[] args) {
    	DB Db=new DB();
    	SocketThread soccket=new SocketThread(Db);
    	soccket.start();
    	
    	WithSubServer_Server receiver=new WithSubServer_Server(Db);
    	receiver.start();
    	
    	for(;;)
    	{
    		//A_L#
    		if(receiver.L_HasMessage())//서버간 통신 메시지 확인 부분
    		{
    			
    			String strmessage=receiver.lQueue[receiver.lRear%QUEUESIZE];
    			char message[]=new char[128];
	        	int i;
	        	for(i=0;i<strmessage.length()&&i<128;i++)
                	message[i]=strmessage.charAt(i);
    			
	        	System.out.println("with server msg : "+ new String(message));
	        	String strMsg="A_L#";
        		char startServerNum=StartServerNum;
        		String id="";
        		char isFind='0';
	        	//A_L#
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='L')
	        	{
	        		//B_LOC를 여기서 호출하고, A_LOCATION에서 이것을 호출한다.
	        		
	        		//0~ A_L# 
	        		//21~ 시작 서버 번호#
	        		//41~ 목적지 id#
	        		//61~ 위치를 요구한 id#
	        		//81~ 찾았나?
	        		//101~ 주소
	        		if(message[21]==startServerNum)
	        		{
	        			//여기가 시작한 곳이다. 
	        			if(message[81]=='1')
	        			{
	        				//찾았다. 처리해서 맨 처음 받은 곳으로 보내면 댄다
	        				String strId="";

	    	        		for(i=41;message[i]!='#';i++)
	    	        			strId+=message[i];
	    	        		
	    						DataOutputStream val = soccket.GetIdGiveAddr(strId);
	    						soccket.addr[soccket.rear % QUEUESIZE] = val;
	    						// 친구 주소로 셋팅완료 보내면 댐
	    						String strMessage = "";
	    						String strTemp = "A_LOCATION#";

	    						for (i = 0; i < strTemp.length(); i++)
	    							message[i] = strTemp.charAt(i);
	    						char lat[]=new char[40];
	    						char lat2[]=new char[40];
	    						for(i=101;message[i]!='#';i++)
	    							lat[i-101]=message[i];
	    						lat[i-101]='#';
	    						for(i=111;message[i]!='#';i++)
	    							lat2[i-111]=message[i];
	    						lat2[i-111]='#';
	    						
	    						for(i=21;lat[i-21]!='#';i++)
	    							message[i]=lat[i-21];
	    						message[i]='#';
	    						for(i=41;lat2[i-41]!='#';i++)
	    							message[i]=lat2[i-41];
	    						message[i]='#';
	    						for (i = 0; i < 128; i++)
	    							strMessage += message[i];

	    						System.out.println("A_L# 출발지 값 있을때 : "+strMessage);
	    						soccket.SendMessage(strMessage);
	    					
	    						receiver.lRear++;
	        				continue;
	        				
	        				
	        			}
	        			else
	        			{
	        				//못찾았다. 접속중이지 않은 친구이다
	        				String strMessage="";
	    	        		String strTemp="A_LOCATION#";
	    	        		char cTemp[]=new char[128];
	    	        		for(i=0;i<strTemp.length();i++)
	            				cTemp[i]=strTemp.charAt(i);
	    	        		
	    	        			cTemp[21]='0';
	    	        			cTemp[22]='#';
	    	        			
	    	        			for(i=0;i<128;i++)
	    	        				strMessage+=cTemp[i];
	    	        			System.out.println("A_L# 찾을수 없다 : "+strMessage);
	    	        			soccket.SendMessage(strMessage);
	    	        		
	        			}
					} else if (message[21] != startServerNum) {
						if (message[81] == '1') {
							// 이미 찾았으니 바로 다음서버로 넘긴다
						//	strMsg = "";
						//	for (i = 0; i < 128; i++)
						//		strMsg += message[i];
							isFind = '1';
						//	receiver.SendMesToNextServer(strMsg);
							System.out.println("A_L# 이미 값이 있다");
						}

						else {
							System.out.println("A_L# 검색해보러 옴");
							// 검색해보고 다음 서버로 넘긴다
							id="";
							for (i = 41; message[i] != '#'; i++)
								id += message[i];
							String Lid = soccket.Find_ID_AtTheHashmap(id);

							char check = Lid.charAt(0);
							if (check == '%') {
								// 없다
								System.out.println("A_L# 검색해보러 옴   없다  "+Lid+"  "+id);
								isFind = '0';
							}

							else {
								// 찾았다
								isFind = '1';
								DataOutputStream val = soccket
										.GetIdGiveAddr(Lid);

								System.out.println("A_L# 검색해보러 옴   찾았다 : "+val);
								
								//찾았다. 처리해서 맨 처음 받은 곳으로 보내면 댄다
		        				//soccket.addr[soccket.rear%QUEUESIZE]=val;//목적지 주소를 친구로
			        			
		        				String strMessage="";
		        				char cTemp[]=new char[128];
		        				
		        				cTemp[0]='B';
		        				cTemp[1]='_';
		        				cTemp[2]='L';
		        				cTemp[3]='O';
		        				cTemp[4]='C';
		        				cTemp[5]='#';
		        				
		        				for(i=21;message[i]!='#';i++)
		        					cTemp[i]=message[i+40];
		        				cTemp[i]='#';
		        				cTemp[41]='1';
		        				cTemp[61]=message[21];
			        			for(i=0;i<128;i++)
			        				strMessage+=cTemp[i];
			        		
			        			System.out.println("보내기 B_LOC: "+val+" 데이터: "+strMessage);
								
								
			        			try{
			        				val.writeUTF(strMessage);//
			        			}catch(Exception e){
			        	    		e.printStackTrace();
			        			}
			        			
			        			receiver.lRear++;
			        			continue;
			        			
							}

						}

						// message에 넣어서 보낸다

						strMsg = "";
						for (i = 0; i < 128; i++)
							strMsg += message[i];

						message[81] = isFind;
						receiver.SendMesToNextServer(strMsg);

						// for(i=0;i<strMsg.length();i++)

					}
				}

				receiver.lRear++;
			}
    		
    		
    		
    		
    		
    		
    		
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
	        		if(message[21]!='0')
	        			Db.LoginOut(strName);
	        	}
	        	//A_CHANGE_NAME#
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='C'&&message[3]=='H'&&message[4]=='A'&&message[5]=='N'&&message[6]=='G'&&message[7]=='E'&&message[8]=='_')
	        	{
	        		String sendId="";
	        		for(i=21;message[i]!='#';i++)
	        			sendId+=message[i];
	        		soccket.ChangeName(sendId);
	        		Db.ChangeLogin(sendId);
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

	        			String strPort = Integer.toString(subPort); 
	        			arrPort=strPort.toCharArray();
	        			
	        			for(i=41;i<45;i++)
	        				message[i]=arrPort[i-41];
	        			
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
	        	//A_SAVESTA
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='S'&&message[3]=='A'&&message[4]=='V'&&message[5]=='E'&&message[6]=='S'&&message[7]=='T')
	        	{
	    			//0~ A_SAVESTA
	    			//21~ 좌표
	    			//41~ 좌표
	    			//61~ 유형
	        		String type="";
	        		String lat="";
	        		String lon="";

	        		for(i=21;message[i]!='#';i++)
	        			lat+=message[i];
	        		
	        		for(i=41;message[i]!='#';i++)
	        			lon+=message[i];
	        		
	        		type+=message[61];
	        		
	        		Db.SaveStairs(lat, lon, type);
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
	        		//!@
	        		String strMessage="";
	        		String strTemp="B_LOC";
	        		char cTemp[]=new char[128];
	        		for(i=0;i<strTemp.length();i++)
        				cTemp[i]=strTemp.charAt(i);
	        		//!@
	        		//if("".equals(val)){//접속 중이지 않은 아이디 요구시(에러)
	        		if("".equals(val) || val==null){
	        			//0~ A_L# 
		        		//21~ 시작 서버 번호#
		        		//41~ 목적지 id#
		        		//61~ 위치를 요구한 id#
		        		//81~ 찾았나?
		        		//101~ 주소
	        			//A_L#
	        			char cT[]=new char[128];
	        			
	        			cT[0]='A';
	        			cT[1]='_';
	        			cT[2]='L';
	        			cT[3]='#';
	        			cT[21]=StartServerNum;
	        			cT[81]='0';
	        			String strT=strFriendId;
	        			
	        			for(i=41;i<strFriendId.length()+41;i++)
	        				cT[i]=strFriendId.charAt(i-41);
	        			cT[i]='#';
	        			
	        			for(i=61;i<myId.length()+61;i++)
	        				cT[i]=myId.charAt(i-61);
	        			cT[i]='#';
	        			
	        			strT="";
	        			
	        			for(i=0;i<128;i++)
	        				strT+=cT[i];
	        			
	        			receiver.SendMesToNextServer(strT);
	        			
	        			
	        		}
	        		else
	        		{
	        			soccket.addr[soccket.rear%QUEUESIZE]=val;//목적지 주소를 친구로
	        			
	        			for(i=21;i<myId.length()+21;i++)
	        				cTemp[i]=myId.charAt(i-21);
	        			cTemp[i]='#';
	        			cTemp[41]='0';
	        			cTemp[42]='#';
	        			cTemp[61]=StartServerNum;
	        			cTemp[62]='#';
	        			for(i=0;i<128;i++)
	        				strMessage+=cTemp[i];
	        			System.out.println("보내기 B_LOC: : "+StartServerNum);
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
	        		
					if (message[81] == '0') {
						DataOutputStream val = soccket.GetIdGiveAddr(strId);
						soccket.addr[soccket.rear % QUEUESIZE] = val;
						// 친구 주소로 셋팅완료 보내면 댐
						String strMessage = "";
						String strTemp = "A_LOCATION";

						for (i = 0; i < strTemp.length(); i++)
							message[i] = strTemp.charAt(i);
						message[i] = '#';
						for (i = 0; i < 128; i++)
							strMessage += message[i];

						soccket.SendMessage(strMessage);
					}
					else//!@
					{
						char cT[]=new char[128];
	        			
	        			cT[0]='A';
	        			cT[1]='_';
	        			cT[2]='L';
	        			cT[3]='#';
	        			cT[21]=message[101];
	        			cT[22]='#';
	        			cT[81]='1';
	        			cT[82]='#';
	        			String strT=strId;
	        			//101~ 좌표
	        			//111~ 좌표
	        			for(i=41;i<strT.length()+41;i++)
	        				cT[i]=strT.charAt(i-41);
	        			cT[i]='#';
	        			
	        			
	        			//메시지 21~ 41~ 좌표가 있으니 ,cT 101~ 110~에 좌표를 넣으면 댄다
	        			for(i=21;message[i]!='#';i++)
	        				cT[i+80]=message[i];
	        			cT[i+80]='#';
	        			for(i=41;message[i]!='#';i++)
	        				cT[i+70]=message[i];
	        			cT[i+70]='#';
	        			
	        			
	        			strT="";
	        			
	        			for(i=0;i<128;i++)
	        				strT+=cT[i];
	        			
	        			receiver.SendMesToNextServer(strT);
	        			
						
						
					}
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
	        	
	        	//A_WAYLOC#
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='W'&&message[3]=='A'&&message[4]=='Y'&&message[5]=='L'&&message[6]=='O'&&message[7]=='C'&&message[8]=='#')
	        	{
	        		String id="";
	        		String wayName="";
	        		for(i=21;message[i]!='#';i++)
	        			id+=message[i];
	        		for(i=41;message[i]!='#';i++)
	        			wayName+=message[i];
	        		
	        		//String friendId=Db.GetNumGiveId(strNum);
	        		
	        	
	        		String val=Db.FindWayOfDB(id, wayName);
	        		
	        		//if(val.charAt(0)=='0')
	        		
	        		String strMessage="";
	        		String strTemp="A_WAYLOC#";
	        		char cTemp[]=new char[128];
	        		
	        		for(i=0;i<strTemp.length();i++)
	        			cTemp[i]=strTemp.charAt(i);

	        		//21~ lat 41~ lon
	        		if(val.charAt(0)=='0')
	        		{
	        			cTemp[21]='0';
	        		}
	        		else
	        		{
	        			String lat="";
	        			String lon="";
	        			System.out.println(val);
	        			for(i=0;val.charAt(i)!='#';i++)
	        				cTemp[i+21]=val.charAt(i);
	        			cTemp[i+21]='#';
	        		
	        			int j=i+1;
	        			for(i=j;i<val.length();i++)
	        				cTemp[i-j+41]=val.charAt(i);
	        			cTemp[i-j+41]='#';
	        		}
	        		
	        		for(i=0;i<128;i++)
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
