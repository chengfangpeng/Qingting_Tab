package com.cnwir.gongxin.ui;

import java.util.ArrayList;
import java.util.List;

import org.cnwir.asyncImg.ImageDownloader;
import org.cnwir.asyncImg.OnImageDownload;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.adapter.AbFragmentPagerStateAdapter;
import com.ab.view.slidingmenu.SlidingFragmentActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.bean.UserInfo;
import com.cnwir.gongxin.ui.about.AboutActivity;
import com.cnwir.gongxin.ui.clouddesktop.CloudScanningActivity;
import com.cnwir.gongxin.ui.feedback.FeedBackActivity;
import com.cnwir.gongxin.ui.leftmenu.LeftMenuFragment;
import com.cnwir.gongxin.ui.loading.GuideActivity;
import com.cnwir.gongxin.ui.login.LoginActivity;
import com.cnwir.gongxin.ui.search.SearchActivity;
import com.cnwir.gongxin.ui.setting.SettingActivity;
import com.cnwir.gongxin.util.Constant;
import com.cnwir.gongxin.util.SharedPrefUtils;
import com.cnwir.gongxin.util.StringUtil;
import com.cnwir.gongxin.util.VersionManager;
import com.cnwir.gongxin.view.CircleImageView;
import com.cnwir.gongxin.view.viewpager.CustomViewPager;
import com.lidroid.xutils.BitmapUtils;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends SlidingFragmentActivity implements
        LeftMenuFragment.Callbacks, OnClickListener {

    public static String KEY = "ParamsKey";
    private FragmentTransaction fragmentTransaction;
    private RadioButton rb1, rb2, rb3, rb4, rbCloudDesktop;
    private CircleImageView home_menu;
    private Button login;

    private SlidingMenu slidingMenu;
    private Fragment mFragment = null;

    private Fragment f1, f2, f3, f4, f5;

    private TextView homeTitle;

    private Bundle savedInstanceState;

    private FragmentManager fm;

    private String fragmentTag;

    private CustomViewPager mViewPager;

    private ArrayList<Fragment> listFragments;

    private ImageView rightIconImg;

    private AbFragmentPagerStateAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        CheckToGuide();
        setContentView(R.layout.activity_main);
        // sliding menu
        // customize the SlidingMenu
        slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeEnabled(false);
        slidingMenu.setBehindScrollScale(0.25f);
        slidingMenu.setFadeDegree(0.25f);

        slidingMenu.setBackgroundImage(R.drawable.bg);
        setBehindContentView(R.layout.leftmenu_framelayout);
        slidingMenu
                .setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                    @Override
                    public void transformCanvas(Canvas canvas, float percentOpen) {
                        float scale = (float) (percentOpen * 0.25 + 0.75);
                        canvas.scale(scale, scale, -canvas.getWidth() / 2,
                                canvas.getHeight() / 2);
                    }
                });

        slidingMenu
                .setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                    @Override
                    public void transformCanvas(Canvas canvas, float percentOpen) {
                        float scale = (float) (1 - percentOpen * 0.25);
                        canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
                    }
                });

        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        Fragment leftMenuFragment = new LeftMenuFragment();
        t.replace(R.id.leftmenu_FrameLayout, leftMenuFragment);
        t.commit();

        fm = this.getFragmentManager();

        fragmentTransaction = fm.beginTransaction();
        listFragments = new ArrayList<Fragment>();
        f1 = new MainOneFragment();
        f2 = new MainTwoFragment();
        f3 = new MainThreeFragment();
        f4 = new CategoryFragment();
        Bundle bundle = new Bundle();
        Object[] obj = new Object[]{"jokedz"};
        bundle.putSerializable(MainActivity.KEY, obj);
        f4.setArguments(bundle);
        f5 = new CloudDesktopFragment();
        listFragments.add(f1);
        listFragments.add(f2);
        listFragments.add(f3);
        listFragments.add(f4);
        listFragments.add(f5);
        findViewById();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeIcon();

    }

    private void CheckToGuide() {

        int oldVersionCode = SharedPrefUtils.getVersionCode(this,
                "version_code");
        int currentVersionCode = VersionManager.getCurrentVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            Intent guideIntent = new Intent(this, GuideActivity.class);

            startActivity(guideIntent);

            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            SharedPrefUtils.putVersionCode(currentVersionCode, "version_code",
                    this);

        }

    }

    private void updateHomeIcon() {
        if (MyApplication.getInstance().isLogined()) {

            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.imageDownload(MyApplication.getInstance()
                            .getUserInfo().getHeadImgUrl(), home_menu,
                    Constant.CACHE_IMG, MainActivity.this,
                    new OnImageDownload() {

                        @Override
                        public void onDownloadSucc(Bitmap bitmap, String c_url,
                                                   ImageView imageView) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

        } else {

            home_menu.setImageResource(R.drawable.menu_icon);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    protected void findViewById() {

        homeTitle = (TextView) findViewById(R.id.home_title);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        mViewPager.setNoScroll(true);
        mViewPager.setOffscreenPageLimit(4);
        mAdapter = new AbFragmentPagerStateAdapter(
                getFragmentManager(), listFragments);
        mViewPager.setAdapter(mAdapter);

        home_menu = (CircleImageView) findViewById(R.id.home_iv_menu);
        home_menu.setOnClickListener(this);
        rightIconImg = (ImageView) findViewById(R.id.home_iv_search);

        rb1 = (RadioButton) findViewById(R.id.home_rb_1);
        rb2 = (RadioButton) findViewById(R.id.home_rb_2);
        rb3 = (RadioButton) findViewById(R.id.home_rb_3);
        rb4 = (RadioButton) findViewById(R.id.home_rb_4);
        rbCloudDesktop = (RadioButton) findViewById(R.id.home_rb_clouddesktop);
        rb1.setOnCheckedChangeListener(rbListener);
        rb2.setOnCheckedChangeListener(rbListener);
        rb3.setOnCheckedChangeListener(rbListener);
        rb4.setOnCheckedChangeListener(rbListener);
        rbCloudDesktop.setOnCheckedChangeListener(rbListener);

        rb1.setChecked(true);

    }

    /**
     * 底部change listener
     */
    OnCheckedChangeListener rbListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (!isChecked)
                return;
            Fragment fragment = null;
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            rightIconImg.setImageResource(R.drawable.search_icon);
            switch (buttonView.getId()) {
                case R.id.home_rb_1:
                    mViewPager.setCurrentItem(0);
                    rightIconImg.setOnClickListener(MainActivity.this);
                    break;
                case R.id.home_rb_2:
                    mViewPager.setCurrentItem(1);
                    rightIconImg.setOnClickListener(MainActivity.this);
                    break;
                case R.id.home_rb_3:
                    mViewPager.setCurrentItem(2);
                    rightIconImg.setOnClickListener(MainActivity.this);
                    break;
                case R.id.home_rb_4:
                    mViewPager.setCurrentItem(3);
                    rightIconImg.setOnClickListener(MainActivity.this);
                    break;
                case R.id.home_rb_clouddesktop:
                    mViewPager.setCurrentItem(4);


                    rightIconImg.setImageResource(R.drawable.btn_cloud);
                    rightIconImg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,
                                    CloudScanningActivity.class);

                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        }
                    });
                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_iv_menu:

                slidingMenu.showMenu();
                break;
            case R.id.home_iv_search:// 搜索
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.anim.slide_left_in,
                        R.anim.slide_left_out);
                break;

            default:
                break;

        }
    }

    private long exitTime = 0;

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            Toast.makeText(this,
                    "再按一次退出" + getResources().getString(R.string.app_name),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLeftMenuClick(View v) {

        switch (v.getId()) {
            case R.id.ll_feedback:// 意见反馈

                if (MyApplication.getInstance().isLogined()) {

                    Intent intent = new Intent(MainActivity.this,
                            FeedBackActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in,
                            R.anim.slide_left_out);
                } else {
                    Toast.makeText(MainActivity.this, R.string.login_first, Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_bottom_in,
                            R.anim.slide_top_out);
                }

                break;

            case R.id.ll_setting:// 设置

                Intent itSetting = new Intent(MainActivity.this,
                        SettingActivity.class);
                startActivity(itSetting);
                overridePendingTransition(R.anim.slide_left_in,
                        R.anim.slide_left_out);

                break;
            case R.id.about_us://关于我们
                Intent itAboutus = new Intent(MainActivity.this,
                        AboutActivity.class);
                startActivity(itAboutus);
                overridePendingTransition(R.anim.slide_left_in,
                        R.anim.slide_left_out);
                break;
            case R.id.login:// 登录

                if (!MyApplication.getInstance().isLogined()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_bottom_in,
                            R.anim.slide_top_out);
                } else {
                    slidingMenu.showContent();
                    final MaterialDialog mMaterialDialog = new MaterialDialog(this);
                    mMaterialDialog.setTitle("退出");
                    mMaterialDialog.setMessage("你确定要退出登陆吗？");
                    mMaterialDialog.setPositiveButton("  确定  ", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            MyApplication.getInstance().deleteUserInfo();
                            MyApplication.getInstance().deleteCollectAppList();
                            MyApplication.getInstance().deleteCreateApps();
                            Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivity(intentToLogin);
                            overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);


                        }
                    });
                    mMaterialDialog.setNegativeButton("  取消  ", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();
                }

            default:
                break;
        }
    }


}
