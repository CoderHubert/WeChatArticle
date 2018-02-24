package com.hubert.wechatarticle.utils;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Project: WeChatArticle.
 * Package: com.hubert.wechatarticle.utils.
 * Created by alanwalker on 2018/2/21.
 */

public final class AccessibilityHelper {

    private final static String TAG = AccessibilityHelper.class.getSimpleName();

    public static boolean performClick(AccessibilityNodeInfo nodeInfo) {
        Log.i(TAG, "performClick");
        boolean flag;
        if(nodeInfo == null) {
            flag = false;
        }else {
            if(nodeInfo.isClickable()) {
                flag = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if(flag) Log.i(TAG,"performClick success");
            } else {
                flag = performClick(nodeInfo.getParent());
            }
        }
        return flag;
    }

    public static boolean hasClickableNodeInfo(AccessibilityNodeInfo nodeInfo) {
        boolean flag;
        if (nodeInfo == null) {
            flag = false;
        } else {
            if (nodeInfo.isClickable()) {
                flag = true;
            } else {
                flag = hasClickableNodeInfo(nodeInfo.getParent());
            }
        }
        return flag;
    }

    public static void performBack(AccessibilityService service) {
        if(service == null) {
            return;
        }
        Log.i(TAG, "performBack");
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }
}
