package com.hubert.wechatarticle.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.hubert.wechatarticle.mode.Action;

import java.util.ArrayList;

/**
 * Project: WeChatArticle.
 * Package: com.hubert.wechatarticle.utils.
 * Created by alanwalker on 2018/2/22.
 */

public final class EventScheduling {
    private final static String TAG = EventScheduling.class.getSimpleName();

    public static int DEFAULT_STATUS = 0;
    public static int CLICKED_LINK_STATUS = 1;
    public static int CLICKED_MENU_STATUS = 2;
    public static int CLICKED_COPY_STATUS = 3;
    public static int SUC_COPY_STATUS = 4;

    public static int STATUS = DEFAULT_STATUS;

    private Action currentAction;
    private ArrayList<Action> actionList;

    public EventScheduling() {
        currentAction = null;
    }

    public boolean setAction(Action action){
        if(this.currentAction != null){
            Log.i(TAG,"currentAction != null");
            return false;
        }
        if(action.getActionNum() == Action.CLICK_LINK_ACTION && !action.getUrl().isEmpty()){
            Log.i(TAG,"url is not empty");
            return false;
        }
        if(action.getNodeInfo() != null && !AccessibilityHelper.hasClickableNodeInfo(action.getNodeInfo())){
            Log.i(TAG,"has not clickable nodeInfo");
            return false;
        }

        this.currentAction = action;
        handleAction(action.getActionNum());
        return true;
    }

    private void handleAction(int actionNum){
        Log.i(TAG, "handle actionNum : " + actionNum);
        switch (actionNum) {
            case Action.CLICK_LINK_ACTION:
                if(getLastStep() == Action.DEFAULT_ACTION && currentAction != null && currentAction.getNodeInfo() != null){
                    AccessibilityNodeInfo nodeInfo = currentAction.getNodeInfo();
                    addCurrentAction();
                    if(AccessibilityHelper.performClick(nodeInfo)){
                        EventScheduling.STATUS = CLICKED_LINK_STATUS;
                    }
                }
                break;
            case Action.CLICK_MENU_ACTION:
                if(getLastStep() == Action.CLICK_LINK_ACTION && currentAction != null && currentAction.getNodeInfo() != null){
                    AccessibilityNodeInfo nodeInfo = currentAction.getNodeInfo();
                    addCurrentAction();
                    if(AccessibilityHelper.performClick(nodeInfo)){
                        EventScheduling.STATUS = CLICKED_MENU_STATUS;
                    }
                }
                break;
            case Action.CLICK_COPY_ACTION:
                if(getLastStep() == Action.CLICK_MENU_ACTION && currentAction != null && currentAction.getNodeInfo() != null){
                    AccessibilityNodeInfo nodeInfo = currentAction.getNodeInfo();
                    addCurrentAction();
                    if(AccessibilityHelper.performClick(nodeInfo)){
                        EventScheduling.STATUS = CLICKED_COPY_STATUS;
                    }
                }
                break;
            case Action.CLICK_CLOSE_ACTION:
                if(getLastStep() == Action.CLICK_COPY_ACTION && currentAction != null && currentAction.getNodeInfo() != null){
                    AccessibilityNodeInfo nodeInfo = currentAction.getNodeInfo();
                    addCurrentAction();
                    if(AccessibilityHelper.performClick(nodeInfo)){
                        EventScheduling.STATUS = EventScheduling.DEFAULT_STATUS;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void addCurrentAction(){
        if(this.actionList == null){
            this.actionList = new ArrayList<>();
        }
        if(currentAction.getActionNum() == Action.CLICK_CLOSE_ACTION){
            this.actionList.clear();
        }else {
            this.actionList.add(currentAction);
        }
        currentAction = null;
    }


    public int getLastStep() {
        if (this.actionList != null && actionList.size() > 0) {
            return this.actionList.get(actionList.size() - 1).getActionNum();
        }
        return 0;
    }
}
