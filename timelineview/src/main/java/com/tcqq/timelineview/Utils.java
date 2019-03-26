package com.tcqq.timelineview;

import android.content.res.Resources;

final class Utils {

    static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
