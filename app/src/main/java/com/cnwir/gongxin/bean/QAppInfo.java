package com.cnwir.gongxin.bean;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
@Table(name = "qappinfo")  // 建议加上注解， 混淆后表名不受影响
public class QAppInfo implements Serializable {

	// "title":"卡兜名片",
	// "image":"http://apps.huosu.com/upload/20140903-1307.png",
	// "url":"http://kado.im",
	// "collect":0,
	// "share":0,
	// "star":4,
	// "summary":"专注于为你提供全方位舒适的私人联系人管理的富名片应用。结合UC浏览器的二维码扫描功能，通过一体化的设",
	// "desc":""

	/**
	 * @author Cheng.F.P
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@NoAutoIncrement
	private int id;
	private String title;
	private String image;

	private String url;

	private String summary;
	private String desc;
	private int collect;
	private int share;

	private int type;

	private int star;
	
	private boolean isCollected;
	
	//使用这个注解不存去数据库
	@Transient
	private List<Jingxuan_model_item> list;
	
	
	
	public List<Jingxuan_model_item> getList() {
		return list;
	}

	public void setList(List<Jingxuan_model_item> list) {
		this.list = list;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	@Override
	public String toString() {
		return "QAppInfo [id=" + id + ", title=" + title + ", image=" + image + ", url=" + url + ", summary=" + summary
				+ ", desc=" + desc + ", collect=" + collect + ", share=" + share + ", type=" + type + ", star=" + star
				+ "]";
	}

	

}
