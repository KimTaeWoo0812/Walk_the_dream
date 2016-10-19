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
    		if(receiver.L_HasMessage())//������ ��� �޽��� Ȯ�� �κ�
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
	        		//B_LOC�� ���⼭ ȣ���ϰ�, A_LOCATION���� �̰��� ȣ���Ѵ�.
	        		
	        		//0~ A_L# 
	        		//21~ ���� ���� ��ȣ#
	        		//41~ ������ id#
	        		//61~ ��ġ�� �䱸�� id#
	        		//81~ ã�ҳ�?
	        		//101~ �ּ�
	        		if(message[21]==startServerNum)
	        		{
	        			//���Ⱑ ������ ���̴�. 
	        			if(message[81]=='1')
	        			{
	        				//ã�Ҵ�. ó���ؼ� �� ó�� ���� ������ ������ ���
	        				String strId="";

	    	        		for(i=41;message[i]!='#';i++)
	    	        			strId+=message[i];
	    	        		
	    						DataOutputStream val = soccket.GetIdGiveAddr(strId);
	    						soccket.addr[soccket.rear % QUEUESIZE] = val;
	    						// ģ�� �ּҷ� ���ÿϷ� ������ ��
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

	    						System.out.println("A_L# ����� �� ������ : "+strMessage);
	    						soccket.SendMessage(strMessage);
	    					
	    						receiver.lRear++;
	        				continue;
	        				
	        				
	        			}
	        			else
	        			{
	        				//��ã�Ҵ�. ���������� ���� ģ���̴�
	        				String strMessage="";
	    	        		String strTemp="A_LOCATION#";
	    	        		char cTemp[]=new char[128];
	    	        		for(i=0;i<strTemp.length();i++)
	            				cTemp[i]=strTemp.charAt(i);
	    	        		
	    	        			cTemp[21]='0';
	    	        			cTemp[22]='#';
	    	        			
	    	        			for(i=0;i<128;i++)
	    	        				strMessage+=cTemp[i];
	    	        			System.out.println("A_L# ã���� ���� : "+strMessage);
	    	        			soccket.SendMessage(strMessage);
	    	        		
	        			}
					} else if (message[21] != startServerNum) {
						if (message[81] == '1') {
							// �̹� ã������ �ٷ� ���������� �ѱ��
						//	strMsg = "";
						//	for (i = 0; i < 128; i++)
						//		strMsg += message[i];
							isFind = '1';
						//	receiver.SendMesToNextServer(strMsg);
							System.out.println("A_L# �̹� ���� �ִ�");
						}

						else {
							System.out.println("A_L# �˻��غ��� ��");
							// �˻��غ��� ���� ������ �ѱ��
							id="";
							for (i = 41; message[i] != '#'; i++)
								id += message[i];
							String Lid = soccket.Find_ID_AtTheHashmap(id);

							char check = Lid.charAt(0);
							if (check == '%') {
								// ����
								System.out.println("A_L# �˻��غ��� ��   ����  "+Lid+"  "+id);
								isFind = '0';
							}

							else {
								// ã�Ҵ�
								isFind = '1';
								DataOutputStream val = soccket
										.GetIdGiveAddr(Lid);

								System.out.println("A_L# �˻��غ��� ��   ã�Ҵ� : "+val);
								
								//ã�Ҵ�. ó���ؼ� �� ó�� ���� ������ ������ ���
		        				//soccket.addr[soccket.rear%QUEUESIZE]=val;//������ �ּҸ� ģ����
			        			
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
			        		
			        			System.out.println("������ B_LOC: "+val+" ������: "+strMessage);
								
								
			        			try{
			        				val.writeUTF(strMessage);//
			        			}catch(Exception e){
			        	    		e.printStackTrace();
			        			}
			        			
			        			receiver.lRear++;
			        			continue;
			        			
							}

						}

						// message�� �־ ������

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
	        	
	        	
	        	//OUT �α׾ƿ� ������ DB�� isLogin 0���� ����
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

	        			String strPort = Integer.toString(subPort); 
	        			arrPort=strPort.toCharArray();
	        			
	        			for(i=41;i<45;i++)
	        				message[i]=arrPort[i-41];
	        			
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
	        	//A_SAVESTA
	        	if(message[0]=='A'&&message[1]=='_'&&message[2]=='S'&&message[3]=='A'&&message[4]=='V'&&message[5]=='E'&&message[6]=='S'&&message[7]=='T')
	        	{
	    			//0~ A_SAVESTA
	    			//21~ ��ǥ
	    			//41~ ��ǥ
	    			//61~ ����
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
	        		//!@
	        		String strMessage="";
	        		String strTemp="B_LOC";
	        		char cTemp[]=new char[128];
	        		for(i=0;i<strTemp.length();i++)
        				cTemp[i]=strTemp.charAt(i);
	        		//!@
	        		//if("".equals(val)){//���� ������ ���� ���̵� �䱸��(����)
	        		if("".equals(val) || val==null){
	        			//0~ A_L# 
		        		//21~ ���� ���� ��ȣ#
		        		//41~ ������ id#
		        		//61~ ��ġ�� �䱸�� id#
		        		//81~ ã�ҳ�?
		        		//101~ �ּ�
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
	        			soccket.addr[soccket.rear%QUEUESIZE]=val;//������ �ּҸ� ģ����
	        			
	        			for(i=21;i<myId.length()+21;i++)
	        				cTemp[i]=myId.charAt(i-21);
	        			cTemp[i]='#';
	        			cTemp[41]='0';
	        			cTemp[42]='#';
	        			cTemp[61]=StartServerNum;
	        			cTemp[62]='#';
	        			for(i=0;i<128;i++)
	        				strMessage+=cTemp[i];
	        			System.out.println("������ B_LOC: : "+StartServerNum);
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
	        		
					if (message[81] == '0') {
						DataOutputStream val = soccket.GetIdGiveAddr(strId);
						soccket.addr[soccket.rear % QUEUESIZE] = val;
						// ģ�� �ּҷ� ���ÿϷ� ������ ��
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
	        			//101~ ��ǥ
	        			//111~ ��ǥ
	        			for(i=41;i<strT.length()+41;i++)
	        				cT[i]=strT.charAt(i-41);
	        			cT[i]='#';
	        			
	        			
	        			//�޽��� 21~ 41~ ��ǥ�� ������ ,cT 101~ 110~�� ��ǥ�� ������ ���
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
