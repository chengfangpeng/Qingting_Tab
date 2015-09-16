package com.cnwir.gongxin.ui.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;


import com.cnwir.gongxin.R;
import com.cnwir.gongxin.application.MyApplication;
import com.cnwir.gongxin.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by heaven on 2015/9/7.
 */
public class UpdateChecker {

    private static final String TAG = "UpdateChecker";
    private Context mContext;
    //检查版本信息的线程
    private Thread mThread;
    private AppVersion mAppVersion;
    //下载apk的对话框
    private ProgressDialog mProgressDialog;
    private static final int NOTIFICATION_ID = 1;

    private File apkFile;

    private Notification.Builder builder;
    private NotificationManager manager;

    private static boolean isRunBackground;

    public UpdateChecker(Context context) {
        mContext = context;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在下载");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadService.isStopService = true;

            }
        });
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        });


    }

    public void checkForUpdates(final String json) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    mAppVersion = (AppVersion) msg.obj;
                    try {
                        int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                        if (mAppVersion.getVersion() > versionCode) {
                            showUpdateDialog();
                        } else {
                            //Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    } catch (PackageManager.NameNotFoundException ignored) {
                        //
                    }
                }
            }
        };

        mThread = new Thread() {
            @Override
            public void run() {
                //if (isNetworkAvailable(mContext)) {
                Message msg = new Message();
                Log.i("jianghejie", "json = " + json);
                if (json != null) {
                    AppVersion appVersion = parseJson(json);
                    msg.what = 1;
                    msg.obj = appVersion;
                    handler.sendMessage(msg);
                } else {
                    Log.e(TAG, "can't get app update json");
                }
            }
        };
        mThread.start();
    }

    private AppVersion parseJson(String json) {
        AppVersion appVersion = new AppVersion();
        try {
            JSONObject obj = new JSONObject(json);
            String apkUrl = obj.getString(AppVersion.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(AppVersion.APK_VERSION_CODE);
            appVersion.setVersion(apkCode);
            appVersion.setUrl(apkUrl);
        } catch (JSONException e) {
            Log.e(TAG, "parse json error", e);
        }
        return appVersion;
    }

    public void showUpdateDialog() {

        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setTitle("发现新版本");
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.setMessage("");
        mMaterialDialog.setPositiveButton(" 立刻下载 ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                builder = new Notification.Builder(mContext);
                builder.setContentTitle("足迹").setContentText("正在下载...").setSmallIcon(R.drawable.ic_launcher);
                manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                downLoadApk();

            }
        });
        mMaterialDialog.setNegativeButton("  以后再说  ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });

        mMaterialDialog.show();



    }

    public void downLoadApk() {
        String apkUrl = mAppVersion.getUrl();
        String dir = mContext.getExternalFilesDir("apk").getAbsolutePath();
        File folder = Environment.getExternalStoragePublicDirectory(dir);
        if (folder.exists() && folder.isDirectory()) {
            //do nothing
        } else {
            folder.mkdirs();
        }
//        String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
        String filename = "zuji.apk";
        String destinationFilePath = dir + "/" + filename;
        apkFile = new File(destinationFilePath);
        mProgressDialog.show();
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra("url", apkUrl);
        intent.putExtra("dest", destinationFilePath);
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        mContext.startService(intent);
    }


    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                Log.d(TAG, "下载进度 = " + progress);
                builder.setProgress(100, progress, false);
                manager.notify(NOTIFICATION_ID, builder.build());

                if (progress == 100) {
                    mProgressDialog.dismiss();
                    builder.setContentTitle("足迹");
                    builder.setContentText("下载完成");
                    builder.setProgress(0, 0, false);
                    builder.setContentIntent(getPendingIntent());
                    manager.notify(NOTIFICATION_ID, builder.build());
                    //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
                    String[] command = {"chmod", "777", apkFile.toString()};
                    try {
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.start();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            }
            if (resultCode == DownloadService.DOWNLOADSTOP) {
                builder.setContentTitle("足迹");
                builder.setContentText("下载停止！");
                builder.setProgress(0, 0, false);
                manager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }


}
