/**
 * 
 */
package com.tencent.weshowsdk.android.api;

import android.content.Context;
import android.util.Log;

import com.tencent.weshowsdk.android.model.BasicVO;
import com.tencent.weshowsdk.android.network.HttpCallback;
import com.tencent.weshowsdk.android.network.ReqParam;
import com.tencent.weshowsdk.android.utils.Util;

/**
 * @author ISAACXIE
 * 
 */
public class AuthAPI extends BaseAPI {

    /**
     * 获取调用权限URL
     */
    public static final String AUTH_URL = "/auth/token";

    public static final String RESPONSE_TYPE_CODE = "code";

    /**
     * @param account
     */
    public AuthAPI() {
        super();
    }

    /**
     * 鉴权
     * 
     * @param context
     * @param responseType 现在只能填写成code
     * @param clientId 第三方创建应用获取的App Key
     * @param clientSecret 第三方创建应用获取的App Secret
     * @param mCallBack 回调
     * @param mTargetClass 扩展用，暂时填null
     * @param resultType 扩展用，暂时用0
     */
    public void auth(Context context, String responseType, String clientId, String clientSecret,
            HttpCallback mCallBack, Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("response_type", RESPONSE_TYPE_CODE);
        Log.d("response_type", mParam.getmParams().get("response_type").toString());
        mParam.addParam("client_id", Util.getProperties("APP_KEY"));
        mParam.addParam("client_secret", Util.getProperties("APP_KEY_SEC"));

        mParam.addParam("oauth_version", "2.a");

        startRequest(context, API_SERVER + AUTH_URL, mParam, mCallBack, mTargetClass, BaseAPI.HTTP_METHOD_GET,
                resultType);
    }

}
