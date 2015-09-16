package com.cnwir.gongxin.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
@Table(name = "collect_app")
public class CollectApp extends QAppInfo{

	/**
	 * 描述：用户收藏的应用实体
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(column = "collect_count")
	private int collect_count;


	public int getCollect_count() {
		return collect_count;
	}


	public void setCollect_count(int collect_count) {
		this.collect_count = collect_count;
	}
	
	

}
