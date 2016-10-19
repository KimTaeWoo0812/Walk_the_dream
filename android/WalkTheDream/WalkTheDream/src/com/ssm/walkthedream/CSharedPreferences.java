package com.ssm.walkthedream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CSharedPreferences{
	Context context;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	public CSharedPreferences(Context _context) {
		context = _context;
		pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
		editor = pref.edit();
	}
	//불러오기
	public String getPreferences(String  key){
		
		String id=pref.getString(key, "");
		
		return id;
    }
     
    // 값 저장하기
    public void savePreferences(String  key,String data){
    	editor.putString(key, data);
        editor.commit();
    }
     
    // 값(Key Data) 삭제하기
    public void removePreferences(String  key){
        editor.remove(key);
        editor.commit();
    }
    public int IsLogin(){
    	String temp="";
    	temp=pref.getString("isLogin", "");
    	if(temp.equals("") || temp==null)
    		return 0;//로그인 안됨
    	else
    		return 1;//이미 로그인 됨
    }
    public int IsFirst(){
    	String temp="";
    	temp=pref.getString("isFirst", "");
    	if(temp.equals(""))
    		return 0;//로그인 안됨
    	else
    		return 1;//이미 로그인 됨
    }
}
