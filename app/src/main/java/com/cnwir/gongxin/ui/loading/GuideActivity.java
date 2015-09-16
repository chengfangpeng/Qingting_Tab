package com.cnwir.gongxin.ui.loading;

import com.cnwir.gongxin.R;
import com.cnwir.gongxin.R.anim;
import com.cnwir.gongxin.R.id;
import com.cnwir.gongxin.R.layout;
import com.cnwir.gongxin.ui.BaseActivity;
import com.cnwir.gongxin.ui.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends BaseActivity {

	private static final int NUM_PAGES = 4;

	private ViewPager guidePager;

	private PagerAdapter guidePagerAdpater;

	private Button skip, done;

	private ImageView next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Window window = getWindow();
		// window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_guide);

	}

	@Override
	protected void processLogic() {

	}

	@Override
	protected void findViewById() {

		guidePager = (ViewPager) findViewById(R.id.guide_pager);
		guidePagerAdpater = new GuidePagerAdapter(getSupportFragmentManager());
		guidePager.setAdapter(guidePagerAdpater);
		guidePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				
				if (position == NUM_PAGES - 2) {

                    next.setVisibility(View.GONE);
                    skip.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);

                }else if (position < NUM_PAGES - 2){

                    next.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                }else if (position == NUM_PAGES - 1) {
					endGuide();

				}

				

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		skip = (Button) findViewById(R.id.guide_skip);
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endGuide();
			}
		});
		next = (ImageView) findViewById(R.id.guide_next);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				guidePager.setCurrentItem(guidePager.getCurrentItem() + 1, true);
			}
		});

		done = (Button) findViewById(R.id.guide_done);
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endGuide();
			}
		});

	}

	/**
	 * 描述：结束引导页
	 */
	private void endGuide() {
		
		finish();
		overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);

	}

	public class GuidePagerAdapter extends FragmentStatePagerAdapter {

		public GuidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			GuideFragment fg = null;

			switch (position) {
			case 0:
				fg = GuideFragment.newInstance(R.layout.guide_fragment_1);
				break;
			case 1:
				fg = GuideFragment.newInstance(R.layout.guide_fragment_2);
				break;
			case 2:

				fg = GuideFragment.newInstance(R.layout.guide_fragment_3);
				break;
			case 3:
				fg = GuideFragment.newInstance(R.layout.guide_fragment_4);
				break;

			}
			return fg;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	public void onBackPressed() {

		if (guidePager.getCurrentItem() == 0) {

			super.onBackPressed();
		} else {

			guidePager.setCurrentItem(guidePager.getCurrentItem() - 1);
		}

	}

}
