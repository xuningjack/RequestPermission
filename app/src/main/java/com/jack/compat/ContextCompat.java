package com.jack.compat;

import android.content.Context;
import android.os.Process;


/**
 * +
 * 完美世界
 * @Author Jack
 * @Date 2018/1/24 13:49
 * @Copyright:wanmei.com Inc. All rights reserved.
 */
public class ContextCompat {

    public static int checkSelfPermission(Context context, String permission) {
        if(permission == null) {
            throw new IllegalArgumentException("permission is null");
        } else {
            return context.checkPermission(permission, Process.myPid(), Process.myUid());
        }
    }

}
