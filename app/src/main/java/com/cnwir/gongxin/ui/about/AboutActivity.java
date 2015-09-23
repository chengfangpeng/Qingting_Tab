package com.cnwir.gongxin.ui.about;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andexert.library.RippleView;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.ui.BaseActivity;

import me.drakeet.materialdialog.MaterialDialog;

public class AboutActivity extends BaseActivity implements RippleView.OnRippleCompleteListener {

    private RippleView userProtocolRV, aboutUsRV;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_about);

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void findViewById() {

        userProtocolRV = (RippleView) findViewById(R.id.user_protocol_rl);

        aboutUsRV = (RippleView) findViewById(R.id.about_us_rl);
        userProtocolRV.setOnRippleCompleteListener(this);
        aboutUsRV.setOnRippleCompleteListener(this);


    }


    @Override
    public void onComplete(RippleView rippleView) {

        switch (rippleView.getId()){

            case R.id.user_protocol_rl:

                final MaterialDialog mMaterialDialog = new MaterialDialog(AboutActivity.this);
                mMaterialDialog.setTitle("用户服务协议");
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.setMessage("不得利用本网危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本网制作、复制和传播下列信息：\n" +
                        "　　1．煽动抗拒、破坏宪法和法律、行政法规实施的；\n" +
                        "　　2．煽动颠覆国家政权，推翻社会主义制度的；\n" +
                        "　　3．煽动分裂国家、破坏国家统一的；\n" +
                        "　　4．煽动民族仇恨、民族歧视，破坏民族团结的；");
                mMaterialDialog.setNegativeButton("  确定  ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });

                mMaterialDialog.show();
                break;
            case R.id.about_us_rl:

                final MaterialDialog mMaterialDialog1 = new MaterialDialog(AboutActivity.this);
                mMaterialDialog1.setTitle("关于我们");
                mMaterialDialog1.setCanceledOnTouchOutside(true);
                mMaterialDialog1.setMessage("中科时代（北京）信息技术研究院 经国家行政部门批准成立，专注于移动互联网、手机APP客户端、轻应用及全网整合营销云平台进行研发与管理的 研究机构单位；我们是中国最大的移动互联网全案营销服务商，全国范围已经发展了超过170家代理合作伙伴，目前已经完成弗纳斯投资基金提供的A轮融资,融资额 度800万美元，“无线中国移动应用联盟”将进一步加大研发和技术的投入，进行更多新商业模式的探索。");
                mMaterialDialog1.setNegativeButton("  确定  ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog1.dismiss();
                    }
                });

                mMaterialDialog1.show();
                break;
            default:
                break;




        }

    }
}
