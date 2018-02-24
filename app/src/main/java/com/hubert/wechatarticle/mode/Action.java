package com.hubert.wechatarticle.mode;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Project: WeChatArticle.
 * Package: com.hubert.wechatarticle.mode.
 * Created by alanwalker on 2018/2/22.
 */

public class Action {

    public static final int DEFAULT_ACTION = 0;
    public static final int CLICK_LINK_ACTION = 1;
    public static final int CLICK_MENU_ACTION = 2;
    public static final int CLICK_COPY_ACTION = 3;
    public static final int CLICK_CLOSE_ACTION = 4;

    private int actionNum;
    private String key;
    private String url;
    private String className;
    private AccessibilityNodeInfo nodeInfo;
    private int delayTime;

    @Override
    public String toString() {
        return "[actionNum:" + actionNum + " key:" + key + " url:" + url + " className:" + className +"]";
    }

    public int getActionNum() {
        return actionNum;
    }

    public void setActionNum(int actionNum) {
        this.actionNum = actionNum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public AccessibilityNodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(AccessibilityNodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

}
