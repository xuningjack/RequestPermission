package com.jack.compat;

import android.content.Context;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 *
 * 完美世界
 * @Author Jack
 * @Date 2018/1/24 13:32
 * @Copyright:wanmei.com Inc. All rights reserved.
 */
public class PermissionChecker {

    /**
     * Checks whether your app has a given permission and whether the app op
     * that corresponds to this permission is allowed.
     *
     * @param context Context for accessing resources.
     * @param permission The permission to check.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
     *     or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkSelfPermission(Context context,
                                          String permission) {
        return checkPermission(context, permission, android.os.Process.myPid(),
                android.os.Process.myUid(), context.getPackageName());
    }

    /**
     * Checks whether a given package in a UID and PID has a given permission
     * and whether the app op that corresponds to this permission is allowed.
     *
     * @param context Context for accessing resources.
     * @param permission The permission to check.
     * @param pid The process id for which to check.
     * @param uid The uid for which to check.
     * @param packageName The package name for which to check. If null the
     *     the first package for the calling UID will be used.
     * @return The permission check result which is either {@link #PERMISSION_GRANTED}
     *     or {@link #PERMISSION_DENIED} or {@link #PERMISSION_DENIED_APP_OP}.
     */
    public static int checkPermission(Context context, String permission,
                                      int pid, int uid, String packageName) {
        if (context.checkPermission(permission, pid, uid) == PERMISSION_DENIED) {
            return PERMISSION_DENIED;
        }

        String op = AppOpsManagerCompat.permissionToOp(permission);
        if (op == null) {
            return PERMISSION_GRANTED;
        }

        if (packageName == null) {
            String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
            if (packageNames == null || packageNames.length <= 0) {
                return PERMISSION_DENIED;
            }
            packageName = packageNames[0];
        }

        if (AppOpsManagerCompat.noteProxyOp(context, op, packageName)
                != AppOpsManagerCompat.MODE_ALLOWED) {
            int PERMISSION_DENIED_APP_OP = -2;
            return PERMISSION_DENIED_APP_OP;
        }

        return PERMISSION_GRANTED;
    }


}
