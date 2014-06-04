package com.tencent.weshowsdk.android.network;

/**
 * @author ISAACXIE
 * 回调接口
 */
public interface HttpCallback {
    /**
     * 回调方法
     * 
     * @param object
     *            返回的数据对象
     */
    public void onResult(Object object);
}