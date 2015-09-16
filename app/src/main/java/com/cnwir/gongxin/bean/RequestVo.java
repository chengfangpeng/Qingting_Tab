package com.cnwir.gongxin.bean;

import java.util.HashMap;
import android.content.Context;
import com.cnwir.gongxin.parser.BaseParser;


public class RequestVo {
	public String requestUrl;
	public Context context;
	public HashMap<String,String> requestDataMap;
	public BaseParser<?> jsonParser;
}
