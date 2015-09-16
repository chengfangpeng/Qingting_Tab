package com.cnwir.gongxin.parser;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseParser<T> {
	
	 public abstract T parseJSON(String paramString) throws JSONException;
	 
	 /**
	  * 
	  * @author wangwm
	  * 2015年3月30日 下午4:10:34
	  * @param paramString
	  * @return
	  * @throws JSONException
	  */
	 public String checkResponse(String paramString) throws JSONException{
		if(paramString==null){ 
			return null;
		}else{
			JSONObject jsonObject = new JSONObject(paramString); 
			String result = jsonObject.getString("response");
			if(result!=null && !result.equals("error")){
				return result;
			}else{
				return null;
			}
			
		}
	 }
}
