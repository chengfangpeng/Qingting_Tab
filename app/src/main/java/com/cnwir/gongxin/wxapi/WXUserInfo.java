package com.cnwir.gongxin.wxapi;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;

public class WXUserInfo
{
  private String city;
  private String country;
  private String headimgurl;
  private String language;
  private String nickname;
  private String openid;
  private List<Object> privilege;
  private String province;
  private int sex;
  private String unionid;

  public String getCity()
  {
    return this.city;
  }

  public String getCountry()
  {
    return this.country;
  }

  public String getHeadimgurl()
  {
    return this.headimgurl;
  }

  public String getLanguage()
  {
    return this.language;
  }

  public String getNickname()
  {
    return this.nickname;
  }

  public String getOpenid()
  {
    return this.openid;
  }

  public List<Object> getPrivilege()
  {
    return this.privilege;
  }

  public String getProvince()
  {
    return this.province;
  }

  public int getSex()
  {
    return this.sex;
  }

  public String getUnionid()
  {
    return this.unionid;
  }

  public void saveInSp(Context paramContext)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("user_info", 0).edit();
    localEditor.putString("openid", this.openid);
    localEditor.putInt("sex", this.sex);
    localEditor.putString("nickname", this.nickname);
    localEditor.putString("language", this.language);
    localEditor.putString("city", this.city);
    localEditor.putString("province", this.province);
    localEditor.putString("country", this.country);
    localEditor.putString("headimgurl", this.headimgurl);
    localEditor.putString("unionid", this.unionid);
    localEditor.commit();
  }

  public void setCity(String paramString)
  {
    this.city = paramString;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }

  public void setHeadimgurl(String paramString)
  {
    this.headimgurl = paramString;
  }

  public void setLanguage(String paramString)
  {
    this.language = paramString;
  }

  public void setNickname(String paramString)
  {
    this.nickname = paramString;
  }

  public void setOpenid(String paramString)
  {
    this.openid = paramString;
  }

  public void setPrivilege(List<Object> paramList)
  {
    this.privilege = paramList;
  }

  public void setProvince(String paramString)
  {
    this.province = paramString;
  }

  public void setSex(int paramInt)
  {
    this.sex = paramInt;
  }

  public void setUnionid(String paramString)
  {
    this.unionid = paramString;
  }
}
