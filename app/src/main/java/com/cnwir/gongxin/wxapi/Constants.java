package com.cnwir.gongxin.wxapi;

public class Constants {
	
    public static final String APP_ID = "wxdd4986b22e386959";
    public static final String APP_SECRET = "7e0f7a6435e8e6ec801b679b51b153a9";
    
    public static class ShowMsgActivity {
		public static final String STitle = "showmsg_title";
		public static final String SMessage = "showmsg_message";
		public static final String BAThumbData = "showmsg_thumb_data";
	}
    
    public static class Config
    {
      public static final String APP_MODEL = " Bate";
      public static String APP_VERNAME = "";
      public static boolean AllowLoginByCloud7User = false;
      public static boolean ForceLoginByCloud7User = false;
      public static boolean PraiseEnable = false;
      public static final String SentLogTime = "SentLogTime";
      public static boolean WeiboShare = false;

      static
      {
        PraiseEnable = false;
        ForceLoginByCloud7User = false;
      }
    }
}
