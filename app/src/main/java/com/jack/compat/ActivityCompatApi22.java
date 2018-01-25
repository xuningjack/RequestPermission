package com.jack.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;


@TargetApi(22)
class ActivityCompatApi22 {
    public static Uri getReferrer(Activity activity) {
        return activity.getReferrer();
    }
}
