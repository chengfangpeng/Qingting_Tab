package com.cnwir.gongxin.bean;

import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 功能描述：用户收创建的轻应用实体
 * 
 * @author Cheng.F.P
 * */



@Table(name = "create_app")
public class CreateApp extends QAppInfo{


	@Column(column = "create_count")
	private int createCount;

	public int getCollectCount() {
		return createCount;
	}

	public void setCollectCount(int collectCount) {
		createCount = createCount;
	}
	
	
	


	

}
