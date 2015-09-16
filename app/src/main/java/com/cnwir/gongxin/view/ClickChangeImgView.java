package com.cnwir.gongxin.view;

import com.cnwir.gongxin.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ClickChangeImgView extends ImageView{
	
	
	
	private boolean isClick;


	  
	
	public ClickChangeImgView(Context context){
		
		super(context);
	}

	public ClickChangeImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
  
  
        setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                /* 
                 * 判断控件是否被点击过 
                 */  
                if (isClick) {  
                    // 如果已经被点击了则点击时设置颜色过滤为空还原本色  
                	setImageResource(R.drawable.item_fav);
                    isClick = false;  
                } else {  
                    // 如果未被点击则点击时设置颜色过滤后为黄色  
                	setImageResource(R.drawable.item_fav_d);
                    isClick = true;  
                }  
  
                // 记得重绘  
                invalidate();  
            }  
        });  
	}

	
	
	
	

}
