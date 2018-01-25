package com.jack.compat;



import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;


/**
 * AppOpsManager implementations for API 23.
 * @Author Jack
 * @Date 2018/1/24 13:37
 * @Copyright:wanmei.com Inc. All rights reserved.
 */
@TargetApi(23)
class AppOpsManagerCompat23 {
    public static String permissionToOp(String permission) {
        return AppOpsManager.permissionToOp(permission);
    }

    public static int noteOp(Context context, String op, int uid, String packageName) {
        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteOp(op, uid, packageName);
    }

    public static int noteProxyOp(Context context, String op, String proxiedPackageName) {
        AppOpsManager appOpsManager = context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteProxyOp(op, proxiedPackageName);
    }
}
