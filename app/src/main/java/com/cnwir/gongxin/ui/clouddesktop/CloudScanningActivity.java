package com.cnwir.gongxin.ui.clouddesktop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.parentclass.ParentFragment;

public class CloudScanningActivity extends FragmentActivity implements OnClickListener{
	
	
	private ImageView img_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cloud_desktop_scanning_activity);
		
		img_return = (ImageView) findViewById(R.id.return_back);
		img_return.setOnClickListener(this);
		  replaceFragment(R.id.content_frame, new CloudDesktopHomeFragment());
		
	}
	
	 protected void replaceFragment(int viewId, Fragment fragment) {
	        FragmentManager fragmentManager = getSupportFragmentManager();
	        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
	    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.return_back:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;

		default:
			break;
		}
		
	}


}
