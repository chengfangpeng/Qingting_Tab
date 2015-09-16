package com.cnwir.gongxin.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import android.graphics.drawable.Drawable;

/**
 * 精选中--卡片信息
 * 
 * @author wangwm 2015年4月15日 上午11:03:34
 */
public class CardInfo_1 {
	@Id
	private int id;
	
	private int u_id;

	private boolean isShow = true;

	/** 对应接口的key字段值 */
	private String moduleKey;

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	/** 对应接口的key字段值 */
	public String getModuleKey() {
		return moduleKey;
	}

	/** 对应接口的key字段值 */
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

}
