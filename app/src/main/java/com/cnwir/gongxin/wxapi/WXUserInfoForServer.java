package com.cnwir.gongxin.wxapi;

import com.cnwir.gongxin.util.ScreenUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class WXUserInfoForServer
{
  public String App = "chuye";
  public String Device = "";
  public String Resolution = "";
  public String System = "";
  public String UserAgent = "Android";
  public String Version = "";
  private String city;
  private String country;
  private String headimgurl;
  private String language;
  private String nickname;
  private String openid;
  private String province;
  private int sex;
  private String unionid;

  public static WXUserInfoForServer getInSp(Context paramContext)
  {
    WXUserInfoForServer localWXUserInfoForServer = new WXUserInfoForServer();
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("user_info", 0);
    localWXUserInfoForServer.openid = localSharedPreferences.getString("openid", "");
    localWXUserInfoForServer.sex = localSharedPreferences.getInt("sex", 1);
    localWXUserInfoForServer.nickname = localSharedPreferences.getString("nickname", "");
    localWXUserInfoForServer.language = localSharedPreferences.getString("language", "");
    localWXUserInfoForServer.city = localSharedPreferences.getString("city", "");
    localWXUserInfoForServer.province = localSharedPreferences.getString("province", "");
    localWXUserInfoForServer.country = localSharedPreferences.getString("country", "");
    localWXUserInfoForServer.headimgurl = localSharedPreferences.getString("headimgurl", "");
    localWXUserInfoForServer.unionid = localSharedPreferences.getString("unionid", "");
    localWXUserInfoForServer.Device = Build.MODEL;
    localWXUserInfoForServer.System = "Android " + Build.VERSION.RELEASE;
    localWXUserInfoForServer.Version = Constants.Config.APP_VERNAME;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = String.valueOf(ScreenUtils.getScreenHeight(paramContext));
    arrayOfObject[1] = String.valueOf(ScreenUtils.getScreenWidth(paramContext));
    localWXUserInfoForServer.Resolution = String.format("%s*%s", arrayOfObject);
    return localWXUserInfoForServer;
  }

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
