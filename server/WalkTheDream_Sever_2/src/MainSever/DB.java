package MainSever;


import java.sql.*;
import java.util.Arrays;

public class DB {
    static Connection con=null;
    static Statement stmt;
    ResultSet result;
    ResultSet result2;
	public DB(){
		
        try{
        	Class.forName("com.mysql.jdbc.Driver");
        	//Class.forName("org.gjt.mm.mysql.Driver");
        	
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","ekdrms52");
            //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","uroot","p");
            System.out.println("�����ͺ��̽� ���ӿ� �����Ͽ����ϴ�.");
            
            stmt = con.createStatement();
            //����
            /*
            stmt.executeUpdate("CREATE TABLE USER4 (" +
            		"num int NOT NULL, " +
            		"id varchar(20) NOT NULL, " +
            		"pw varchar(20) NOT NULL, " +
            		"isLogin int NOT NULL, " +
            		"PRIMARY KEY(num) " +");");
            	
            stmt.executeUpdate("CREATE TABLE friend (" +
            		"id varchar(20) NOT NULL, " +
            		"friend varchar(20) NOT NULL " +");");
            		
            	
            	//���� ���ְ��±� ����.
            stmt.executeUpdate("CREATE TABLE Way2 (" +
            		"id varchar(20) NOT NULL, " +//ID
            		"wayName varchar(20) NOT NULL, " +//������ �̸�
            		"lat varchar(20) NOT NULL, " +//����
            		"lon varchar(20) NOT NULL " +//�浵
            		  ");");
            		  
            stmt.executeUpdate("CREATE TABLE STAIRS (" +
            		"lat double NOT NULL, " +//ID
            		"lon double NOT NULL, " +//������ �̸�
            		"type int NULL " +//��ġ
            		  ");");
            */
			
            //stmt.executeUpdate("insert into STAIRS values('35.8702205','128.6047965','1');");
            //stmt.executeUpdate("insert into USER1 values('root2','root2','root2');");
         
           // stmt.executeUpdate("insert into OBSTACLE1 values(37.56647,126.97796);");
            //��ȸ
            //stmt.executeQuery("select * from user1;");
            result = stmt.executeQuery("select * from user4;");
            System.out.println("����:");
            while(result.next())
            {
            	System.out.print(result.getString(1)+"\t");
            	 System.out.print(result.getString(2)+"\t");
            	 System.out.print(result.getString(3)+"\t");
            	 System.out.print(result.getString(4)+"\n");
            }
            result = stmt.executeQuery("select * from friend;");
            System.out.println("ģ�����:");
            while(result.next())
            {
            	System.out.print(result.getString(1)+"\t");
            	 System.out.print(result.getString(2)+"\n");
            }
            result = stmt.executeQuery("select * from Way2;");
            System.out.println("���ְ��� ��:");
            while(result.next())
            {
            	System.out.print(result.getString(1)+"\t");
            	 System.out.print(result.getString(2)+"\t");
            	 System.out.print(result.getString(3)+"\t");
            	 System.out.print(result.getString(4)+"\n");
            }
            result = stmt.executeQuery("select * from STAIRS;");
            System.out.println("����� ��ֹ�:");
            while(result.next())
            {
            	System.out.print(result.getString(1)+"\t");
            	 System.out.print(result.getString(2)+"\t");
            	 System.out.print(result.getString(3)+"\n");
            }
            //con.close();//DB ����
        }catch(SQLException e){
            System.out.println("SQL err:"+e.toString());
            
        }catch(Exception e){
            System.out.println(e.toString());
        }
	}
	public int IdCheck(String id)
	{
		try {
			result=stmt.executeQuery("select Count(id) from user4 where id like '"+id+"' ;");
			String strTemp="";
			 while(result.next())
				strTemp=result.getString(1);
			 
			char cTemp=strTemp.charAt(0);
			System.out.println("���̵� üũ db ��ȸ  "+cTemp);
			if(cTemp=='0')
				return 1;
			else
				return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public int JoinUs(String id,String pw)
	{
		try {
			System.out.println("�Է��� id  "+id+"  ���   "+pw);
			
			result = stmt.executeQuery("select Count(id) from user4 ;");
			int  temp = 0;
			while(result.next())
				temp=result.getInt(1);
			//int uesrNum=cTemp-47;//������ȣ�� ����Ǿ���ϴϱ� -47
			System.out.println("�Ҵ�� ȸ�� ��ȣ  "+temp);
			String message="insert into USER4 values('"+temp+"','"+id+"','"+pw+"','0');";
			stmt.executeUpdate(message);
			result = stmt.executeQuery("select Count(id) from user4 where id like '"+id+"' ;");
			String strTemp="";
			while(result.next())
				temp=result.getInt(1);
			
			System.out.println("ȸ������ ��ȸ  "+temp);
			
			if(temp==1)
				return 1;
			else
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public synchronized void LoginOut(String id)
	{
		try {
			stmt.executeUpdate("update user4 set islogin='0' where id like '"+id+"';");//����
			System.out.println(id+"�� �α׾ƿ� �Ͽ����ϴ�.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ChangeLogin(String id)
	{
		String m="update user4 set islogin='1' where id like '"+id+"';";
		try {
			stmt.executeUpdate(m);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int Login(String id,String pw)
	{
		try {
			result = stmt.executeQuery("select Count(id) from user4 where id like '"+id+"' ;");
			String strTemp="";
			while(result.next())
				strTemp=result.getString(1);
			
			char cTemp=strTemp.charAt(0);
			if(cTemp=='0')//���� id
				return 2;
			
			int nTemp = 0;
			result = stmt.executeQuery("select isLogin from user4 where id like '"+id+"' ;");//���� ���� Ȯ��
			while(result.next())
				nTemp=result.getInt(1);
			if(nTemp==1)
				return 4;
			
			result = stmt.executeQuery("select pw from user4 where id like '"+id+"' ;");
			while(result.next())
				strTemp=result.getString(1);
			
			if(pw.charAt(0)=='!'&&pw.charAt(1)=='!')//�ڵ� �α��� �ϴ»��
			{
				String m="update user4 set islogin='1' where id like '"+id+"';";
				stmt.executeUpdate(m);
				return 1;
			}
			
			if(pw.equals(strTemp))//�α��� ����
			{
				String m="update user4 set islogin='1' where id like '"+id+"';";
				stmt.executeUpdate(m);
				return 1;
			}
			else
				return 3;//Ʋ�� ��й�ȣ
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	public String GetNumGiveId(String num)
	{
		String id="";
		try {
			result = stmt.executeQuery("select id from user4 where num like '"+num+"' ;");
			while(result.next())
				id=result.getString(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	public char DeleteFriend(String friendId, String myId)
	{
		int nTemp=0;
		int brforNum=0;
		try {
			result = stmt.executeQuery("select Count(id) from friend where id like '"+myId+"' and friend like '"+friendId+"' ;");//����Ȯ���� ���� ��ȸ
			while(result.next())
				brforNum=result.getInt(1);
			
			stmt.executeUpdate("delete from friend where id like '"+myId+"' and friend like '"+friendId+"' ;");//����
			result = stmt.executeQuery("select Count(id) from friend where id like '"+myId+"' and friend like '"+friendId+"' ;");//����Ȯ���� ���� ��ȸ
			
			while(result.next())
				nTemp=result.getInt(1);
			if(nTemp!=brforNum)//���� ����
				return '1';
			else
				return '0';//���� ����
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return '0';
		
	}
	
	//!@
	public String FindWayOfDB(String id, String wayName)
	{
		String lat="";
		String lon="";
		
		try {
			result = stmt.executeQuery("select lat,lon from way2 where id like '"+id+"' and wayName like '"+wayName+"' ;");//����Ȯ���� ���� ��ȸ
			while(result.next())
			{
				lat=result.getString(1);
				lon=result.getString(2);
			}
			
			lat+='#';
			int i;
			for(i=0;i<lon.length();i++)
				lat+=lon.charAt(i);
			
			return lat;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	
	public char DeleteWayOfDB(String id, String wayName)
	{
		int nTemp=0;
		int brforNum=0;
		try {
			result = stmt.executeQuery("select Count(id) from way2 where id like '"+id+"' and wayName like '"+wayName+"' ;");//����Ȯ���� ���� ��ȸ
			while(result.next())
				brforNum=result.getInt(1);
			
			stmt.executeUpdate("delete from way2 where id like '"+id+"' and wayName like '"+wayName+"' ;");//����
			result = stmt.executeQuery("select Count(id) from way2 where id like '"+id+"' and wayName like '"+wayName+"' ;");//����Ȯ���� ���� ��ȸ
			
			while(result.next())
				nTemp=result.getInt(1);
			if(nTemp!=brforNum)//���� ����
				return '1';
			else
				return '0';//���� ����
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return '0';
	}
	public char AddFriend(String friendId, String myId)
	{
		String strTemp="";
		int nTemp=0;
		int brforNum=0;
		try {
			result = stmt.executeQuery("select Count(id) from user4 where id like '"+friendId+"' ;");
			
			while(result.next())
				brforNum=result.getInt(1);
			System.out.println("ģ�� �߰��� ���� ID ��ȸ  "+nTemp);
			if(brforNum==0)//���� ID
				return '3';
			
			result = stmt.executeQuery("select Count(id) from friend where id like '"+myId+"' and friend like '"+friendId+"' ;");//���� �� ��ȸ
			while(result.next())
				brforNum=result.getInt(1);
		
			stmt.executeUpdate("insert into friend values('"+myId+"','"+friendId+"');");
			result = stmt.executeQuery("select Count(id) from friend where id like '"+myId+"' and friend like '"+friendId+"' ;");//���� Ȯ���� ���� ��ȸ
			while(result.next())
				nTemp=result.getInt(1);
			if(nTemp!=brforNum)//����
				return '1';
			else
				return '0';//����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		
		return '0';
	}
	//���ְ��±� �߰�
	public char SaveAddWay(String id, String wayName,String lat,String lon)
	{
		int nTemp=0;
		try {
			stmt.executeUpdate("insert into Way2 values('"+id+"','"+wayName+"','"+lat+"','"+lon+"');");//����
			result = stmt.executeQuery("select Count(id) from Way2 where id like '"+id+"' and wayName like '"+wayName+"' ;");//���� Ȯ���� ���� ��ȸ
			while(result.next())
				nTemp=result.getInt(1);
			if(nTemp!=0)//����
				return '1';
			else
				return '0';
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return '3';
	}
	 
	public char[] SetFriends(String hostId)
	{
		int i=0;
		char message[]=new char[600];
		Arrays.fill(message,'0');
		try {
			result = stmt.executeQuery("select friend from friend where id like '"+hostId+"' ;");
			String strIsLogin[]= new String[15];
			String strName[] = new String[15];
			String strisLogin="";
			int j=0;
			while(result.next())
			{
				strName[j]=result.getString(1);//ģ�� id�޾ƿ�
				j++;
			}
			
			for(int k=0;k<j;k++)
			{
				result= stmt.executeQuery("select isLogin from user4 where id like '"+strName[k]+"' ;");
				if(result.next())
					strIsLogin[k]=result.getString(1);//ģ�� ���ӿ��� �޾ƿ�
			}
			
			int k;
			int tempNum=0;
			for(i=0;i<j;i++,tempNum+=40)//0~ id, 20~ ���ӿ���       �迭�� 600�̴ϱ� 15����� ���� �� �ִ�.
			{
				for(k=tempNum;k<strName[i].length()+tempNum;k++)
					message[k]=strName[i].charAt(k-tempNum);
				message[k]='#';
				
				for(k=tempNum+20;k<strIsLogin[i].length()+tempNum+20;k++)
					message[k]=strIsLogin[i].charAt(k-tempNum-20);
				message[k]='#';
			}
			
			return message;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return message;
		}
	}
	
	//lat varchar(20) NOT NULL, " +//����
	//"lon 
	
	public char[] SetWay(String hostId)
	{
		int i=0;
		char message[]=new char[600];
		Arrays.fill(message,'0');
		try {
			result = stmt.executeQuery("select wayName from way2 where id like '"+hostId+"' ;");
			String lat[]= new String[15];
			String lon[]= new String[15];
			String wayName[] = new String[15];
			int j=0;
			while(result.next())
			{
				wayName[j]=result.getString(1);//�� �̸��� �޾ƿ´�
				j++;
			}
			int k=0;
				result= stmt.executeQuery("select lat from way2 where id like '"+hostId+"' ;");
				while(result.next()){
					lat[k]=result.getString(1);//������ ��ġ�� �޾ƿ´�
					k++;
				}
				k=0;
				result= stmt.executeQuery("select lon from way2 where id like '"+hostId+"' ;");
				while(result.next()){
					lon[k]=result.getString(1);//������ ��ġ�� �޾ƿ´�
					k++;
				}
			
			int tempNum=0;
			for(i=0;i<j;i++,tempNum+=40)//0~ id, 20~ ���ӿ���       �迭�� 600�̴ϱ� 15����� ���� �� �ִ�.
			{
				for(k=tempNum;k<wayName[i].length()+tempNum;k++)
					message[k]=wayName[i].charAt(k-tempNum);
				message[k]='#';
				
				for(k=tempNum+15;k<lat[i].length()+tempNum+15;k++)
					message[k]=lat[i].charAt(k-tempNum-15);
				message[k]='#';
				
				for(k=tempNum+27;k<lon[i].length()+tempNum+27;k++)
					message[k]=lon[i].charAt(k-tempNum-27);
				message[k]='#';
			}
			
			return message;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return message;
		}
	}
	
	public void SaveStairs(String lat,String lon,String type)
	{
		try {
			stmt.executeUpdate("insert into STAIRS values('"+lat+"','"+lon+"','"+type+"');");
			System.out.println("��ֹ� ���� : "+lat+" "+lon+" "+type);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public char Stairs(double lat,double lon)//ũ��� ũ�� �����ؾߴ�
	{
		String temp="";
		try {
			int num=3;
			result = stmt.executeQuery("select Count(type) from STAIRS where lat BETWEEN '"+(lat-0.002)+"' and '"+(lat+0.002)+"' and lon BETWEEN '"+(lon-0.002)+"' and '"+(lon+0.002)+"';");
			
			while(result.next())
				num=result.getInt(1);
			if(num==0)//��ֹ� ����
				return '0';
			else
			{
			
				result = stmt.executeQuery("select type from STAIRS where lat BETWEEN '"+(lat-0.002)+"' and '"+(lat+0.002)+"' and lon BETWEEN '"+(lon-0.002)+"' and '"+(lon+0.002)+"';");
				while(result.next())
				{
					temp=result.getString(1);//�� ������ �޾ƿ�
				}
				return temp.charAt(0);//���� ����. 1�̸� �ö󰡴°�, 2�� �������°�
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return '0';
	}
}