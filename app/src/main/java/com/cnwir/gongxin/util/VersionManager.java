package com.cnwir.gongxin.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by heaven on 2015/6/26.
 */
public class VersionManager {


    public static int getCurrentVersionCode(Context context){

            int versionCode = -1;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }finally {
            return versionCode;
        }


    }
}
