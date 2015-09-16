package com.cnwir.gongxin.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;


/**
 * 描述：用户信息的实体
 *@author cfp
 *
 */


//Unionid 第三方平台返回的标识id
//Channel 登录平台(weixin,weibo,qq)
//Nickname 昵称
//Sex 性别(1 男; 0 女)
//Headimgurl 头像地址
//token 与用户关联的操作需要传token

public class UserInfo {
	@Id
	@NoAutoIncrement
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String unionid;
	
	private String channel;
	
	private String nickName;
	
	private String headImgUrl;
	
	private String sex;
	
	private String token;
	
	private int collectCount;
	
	private int createCount;

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getCreateCount() {
		return createCount;
	}

	public void setCreateCount(int createCount) {
		this.createCount = createCount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	
	
	
	
	

	
	
}
