package com.cnwir.gongxin.wxapi;

import android.content.Context;
import android.content.SharedPreferences;

public class WXTokenInfo
{
  private String access_token;
  private int expires_in;
  private String openid;
  private String refresh_token;
  private String scope;

  public String getAccess_token()
  {
    return this.access_token;
  }

  public int getExpires_in()
  {
    return this.expires_in;
  }

  public String getOpenid()
  {
    return this.openid;
  }

  public String getRefresh_token()
  {
    return this.refresh_token;
  }

  public String getScope()
  {
    return this.scope;
  }

  public void saveInSp(Context paramContext)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("token_info", 0).edit();
    localEditor.putString("access_token", this.access_token);
    localEditor.putString("refresh_token", this.refresh_token);
    localEditor.putString("openid", this.openid);
    localEditor.putInt("expires_in", this.expires_in);
    localEditor.putLong("timestamp", System.currentTimeMillis());
    localEditor.commit();
  }

  public void setAccess_token(String paramString)
  {
    this.access_token = paramString;
  }

  public void setExpires_in(int paramInt)
  {
    this.expires_in = paramInt;
  }

  public void setOpenid(String paramString)
  {
    this.openid = paramString;
  }

  public void setRefresh_token(String paramString)
  {
    this.refresh_token = paramString;
  }

  public void setScope(String paramString)
  {
    this.scope = paramString;
  }
}