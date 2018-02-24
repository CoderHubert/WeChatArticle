package com.hubert.wechatarticle.service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.hubert.wechatarticle.StaticString;
import com.hubert.wechatarticle.mode.Action;
import com.hubert.wechatarticle.utils.EventScheduling;

import java.util.List;

/**
 * Project: WeChatArticle.
 * Package: com.hubert.wechatarticle.service.
 * Created by alanwalker on 2018/2/21.
 */

public class ArticleService extends AccessibilityService {
    public static String TAG = ArticleService.class.getSimpleName();
    private static ArticleService service;
    private SharedPreferences sharedPreferences;
    private String key = "";
    private EventScheduling scheduling;

    public static AccessibilityService getService() {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scheduling = new EventScheduling();
        sharedPreferences = getApplicationContext().getSharedPreferences(StaticString.URL_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        Log.d(TAG, "onAccessibilityEvent eventType = " + eventType);
        Log.d(TAG, "onAccessibilityEvent className = " + className);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED: {
                Log.i(TAG, "通知状态变化");
                onNotificationStateChanged(className);
                break;
            }
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                Log.i(TAG, "窗口状态改变");
                onWindowStateChanged(className, rootNode);
                break;
            }
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: {
                Log.i(TAG, "窗口内容变化");
                onWindowContentChanged(className, rootNode);
                break;
            }
        }

    }

    public synchronized void onNotificationStateChanged(String className) {
        Log.i(TAG, "onNotificationStateChanged");
        if (className.equals(StaticString.WC_TOAST)) {
            if (EventScheduling.STATUS == EventScheduling.CLICKED_COPY_STATUS) {
                String url = getTextFromClip();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, url);
                editor.apply();
                Log.i(TAG, url);
                EventScheduling.STATUS = EventScheduling.SUC_COPY_STATUS;
            }
        }
    }

    public synchronized void onWindowStateChanged(String className, final AccessibilityNodeInfo rootNode) {
        Log.i(TAG, "onWindowStateChanged");
        if (rootNode == null) {
            Log.i(TAG, "rootNode == null");
            return;
        }
        if (className.equals(StaticString.WC_LAUNCHER)) {
            if (EventScheduling.STATUS == EventScheduling.DEFAULT_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList;
                nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/adn");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null && nodeInfo.getText() != null) {
                        String title = nodeInfo.getText().toString();
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_LINK_ACTION);
                        action.setKey(title);
                        action.setUrl(sharedPreferences.getString(title, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取需要点击的链接");
                            key = title;
                        }
                    }
                }
            }
        } else if (className.equals(StaticString.WC_WEBVIEW)) {
            if (EventScheduling.STATUS == EventScheduling.CLICKED_LINK_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/he");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null && nodeInfo.getContentDescription().equals("更多")) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_MENU_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取菜单按钮");
                        }
                    }
                }
            } else if (EventScheduling.STATUS == EventScheduling.SUC_COPY_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hy");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_CLOSE_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取关闭按钮");
                        }
                    }
                }
            }
        } else if (className.equals(StaticString.WC_LINEARLAYOUT)) {
            if (EventScheduling.STATUS == EventScheduling.SUC_COPY_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hy");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_CLOSE_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取关闭按钮");
                        }
                    }
                }
            }
        } else if (className.equals(StaticString.WC_MENU)) {
            if (EventScheduling.STATUS == EventScheduling.CLICKED_MENU_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByText("复制链接");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
                    if (nodeInfo != null) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_COPY_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取复制链接按钮");
                        }
                    }
                }
            }
        }
        rootNode.recycle();
    }

    public synchronized void onWindowContentChanged(String className, AccessibilityNodeInfo rootNode) {
        Log.i(TAG, "onWindowContentChanged");
        if (rootNode != null) {
            if (EventScheduling.STATUS == EventScheduling.SUC_COPY_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hy");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_CLOSE_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取关闭按钮");
                        }
                    }
                }
            } else if (EventScheduling.STATUS == EventScheduling.CLICKED_LINK_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/he");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null && nodeInfo.getContentDescription().equals("更多")) {
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_MENU_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取菜单按钮");
                        }
                    }
                }
            } else if (EventScheduling.STATUS == EventScheduling.CLICKED_MENU_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByText("复制链接");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
                    if (nodeInfo != null) {
                        Log.i(TAG,nodeInfo.toString());
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_COPY_ACTION);
                        action.setKey(key);
                        action.setUrl(sharedPreferences.getString(key, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取复制链接按钮");
                        }
                    }
                }
            } else if (EventScheduling.STATUS == EventScheduling.DEFAULT_STATUS) {
                List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/adn");
                if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                    AccessibilityNodeInfo nodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
                    if (nodeInfo != null && nodeInfo.getText() != null) {
                        String title = nodeInfo.getText().toString();
                        Action action = new Action();
                        action.setActionNum(Action.CLICK_LINK_ACTION);
                        action.setKey(title);
                        action.setUrl(sharedPreferences.getString(title, ""));
                        action.setClassName(className);
                        action.setNodeInfo(nodeInfo);
                        if (scheduling.setAction(action)) {
                            Log.i(TAG, "获取需要点击的链接");
                            key = title;
                        }
                    }
                }
            }
            rootNode.recycle();
        }
    }

    public String getTextFromClip() {
        ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null || !clipboardManager.hasPrimaryClip())
            return "";
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null) {
            return clipData.getItemAt(0).getText().toString();
        }
        return "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
        Toast.makeText(this, "WeChat Article服务被中断", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "onServiceConnected");
        Toast.makeText(this, "WeChat Article服务已连接", Toast.LENGTH_LONG).show();
        service = this;
        super.onServiceConnected();
    }

    @Override
    public void onDestroy() {
        service = null;
        super.onDestroy();
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(1000);
            if (serviceList.size() < 0) {
                return false;
            }
            for (int i=0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(className)) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }
}
