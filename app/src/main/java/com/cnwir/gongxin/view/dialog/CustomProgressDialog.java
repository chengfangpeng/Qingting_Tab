package com.cnwir.gongxin.view.dialog;



import com.cnwir.gongxin.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 自定义正在加载loading
 * @author Administrator
 *
 */
public class CustomProgressDialog extends Dialog {
	    @SuppressWarnings("unused")
		private Context context = null;
	    private static CustomProgressDialog customProgressDialog = null;
	     
	    public CustomProgressDialog(Context context){
	        super(context);
	        this.context = context;
	    }
	     
	    public CustomProgressDialog(Context context, int theme) {
	        super(context, theme);
	    }
	     
	    public static CustomProgressDialog createDialog(Context context){
	        customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
	        customProgressDialog.setContentView(R.layout.customprogressdialog);
	        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
	        
	        customProgressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
	        
	        return customProgressDialog;
	    }
	  
	    public void onWindowFocusChanged(boolean hasFocus){
	        if (customProgressDialog == null){
	            return;
	        }
//	        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
//	        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//	        animationDrawable.start();
	    }
	  
	    /**
	     *
	     * [Summary]
	     *       setTitile ����
	     * @param strTitle
	     * @return
	     *
	     */
	    public CustomProgressDialog setTitile(String strTitle){
	        return customProgressDialog;
	    }
	    /**
	     *
	     * [Summary]
	     *       setMessage ��ʾ����
	     * @param strMessage
	     * @return
	     *
	     */
	    public CustomProgressDialog setMessage(String strMessage){
	        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
	         
	        if (tvMsg != null){
	            tvMsg.setText(strMessage);
	        }
	         
	        return customProgressDialog;
	    }
	}

