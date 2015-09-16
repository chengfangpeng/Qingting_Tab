package com.cnwir.gongxin.bean;

import android.graphics.drawable.Drawable;

/**
 * 精选中--卡片信息
 * 
 * @author wangwm 2015年4月15日 上午11:03:34
 */
public class CardInfo {

	private int id;
	private boolean isShow= true;
	private int sort;
//	imgId, title;
	/** 中文汉字 */
	private String moduleText,desc;
	/** 对应接口的key字段值 */
	private String moduleKey;
	private Drawable img;
	

	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
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



	public int getSort() {
		return sort;
	}



	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/** 中文汉字 */
	public String getModuleText() {
		return moduleText;
	}

	/** 中文汉字 */
	public void setModuleText(String moduleText) {
		this.moduleText = moduleText;
	}

	/** 对应接口的key字段值 */
	public String getModuleKey() {
		return moduleKey;
	}

	/** 对应接口的key字段值 */
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}



//	/**
//	 * 
//	 * @param moduleText 中文汉字
//	 * @param moduleKey 对应接口的key字段值
//	 * @param sort  排序index
//	 * @param imgId  图片本地id
//	 * @param title  String id
//	 * @param desc   String id
//	 * @param isShow  是否显示
//	 */
//	public CardInfo(String moduleText,String moduleKey, int sort, int imgId, int title,
//			int desc, boolean isShow) {
//		this.isShow = isShow;
//		this.sort = sort;
//		this.imgId = imgId;
//		this.title = title;
//		this.desc = desc;
//		this.moduleText = moduleText;
//	}

}
