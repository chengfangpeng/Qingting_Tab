package com.cnwir.gongxin.ui.setting;

import org.cnwir.mycache.ConfigCacheUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.ui.BaseActivity;
import com.cnwir.gongxin.ui.update.UpdateChecker;

public class SettingActivity extends BaseActivity implements OnClickListener, RippleView.OnRippleCompleteListener {


    private static final String TAG = SettingActivity.class.getSimpleName();

    private static final String SHAREDP_SETTING = "setting";

    private static final String ISPUSH = "isPush";

    private TextView tv_title_text;

    private ImageView return_back;

    private RadioButton push_rb;

    private RippleView cacheClear;

    private RippleView checkUpdate;

    private RippleView changeTheme;

    private boolean isPush;

    SharedPreferences preferences;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void processLogic() {
        tv_title_text.setText("设置");
        preferences = getSharedPreferences(SHAREDP_SETTING, Activity.MODE_PRIVATE);
        isPush = preferences.getBoolean(ISPUSH, true);
        push_rb.setChecked(isPush);


    }

    @Override
    protected void findViewById() {

        tv_title_text = (TextView) findViewById(R.id.tv_title_text);

        return_back = (ImageView) findViewById(R.id.return_back);
        return_back.setOnClickListener(this);
        push_rb = (RadioButton) findViewById(R.id.push_switch_rb);
        cacheClear = (RippleView) findViewById(R.id.cache_clear_rl);
        checkUpdate = (RippleView) findViewById(R.id.check_update);
        changeTheme = (RippleView) findViewById(R.id.change_theme);

        push_rb.setOnClickListener(this);
        cacheClear.setOnRippleCompleteListener(this);
        checkUpdate.setOnRippleCompleteListener(this);
        changeTheme.setOnRippleCompleteListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.return_back:
                mfinish();

                break;
            case R.id.push_switch_rb:
                isPush = !isPush;
                push_rb.setChecked(isPush);

                break;
            default:
                break;
        }

    }

    /**
     * 到服务检测是否有新版本
     */
    private void checkUpdate() {
        Log.d(TAG, "检测版本...");
        pd = new ProgressDialog(this);
        pd.setMessage("正在检测...");
        pd.show();
        JsonObjectRequest request = new JsonObjectRequest("http://192.168.1.170:8080/GongXinApp/app/checkupdate", null, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                pd.dismiss();
                try {
                    if (response == null) {
                        return;
                    }
                    int error = (int) response.get("error");
                    if (error != 0) {

                        return;
                    }
                    JSONObject json = (JSONObject) response.get("data");


                    UpdateChecker checker = new UpdateChecker(SettingActivity.this);
                    checker.checkForUpdates(json.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                pd.dismiss();
            }
        });
        ClientTask.getInstance(this).addToRequestQueue(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preferences == null) {
            preferences = getSharedPreferences(SHAREDP_SETTING, Activity.MODE_PRIVATE);
        }
        if (pd != null) {

            pd.cancel();
        }

        Editor editor = preferences.edit();
        editor.putBoolean(ISPUSH, isPush);
        editor.apply();
    }


    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {

            case R.id.cache_clear_rl:

                ConfigCacheUtil.clearCache(null);
                Toast.makeText(SettingActivity.this, R.string.cache_clear_success, Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_update:

                checkUpdate();
                break;
            case R.id.change_theme:


                break;

            default:
                break;
        }
    }
}
