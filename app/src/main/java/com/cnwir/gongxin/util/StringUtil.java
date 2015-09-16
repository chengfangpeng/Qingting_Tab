package com.cnwir.gongxin.util;

public class StringUtil {

	/**
	 * 是否空串
	 * @author wangwm
	 * 2015年4月14日 上午9:55:37
	 * @param str
	 * @return true 是 
	 */
	public static boolean isNull(String str){
		if(str!=null & !"".equals(str) )
			return false;
		return true;
	}
	
}
