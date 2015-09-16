package com.cnwir.gongxin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	
	public static long stringToDateToLong(String date){
		long time = 0;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date temDate = sdf.parse(date);
			time = temDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
		
	}

}
