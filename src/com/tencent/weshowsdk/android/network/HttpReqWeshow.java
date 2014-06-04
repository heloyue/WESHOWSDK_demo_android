package com.tencent.weshowsdk.android.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.tencent.weshowsdk.android.model.BasicVO;
import com.tencent.weshowsdk.android.model.RequestResultModel;

/**
 * 
 * @author ISAACXIE
 * 
 */
public class HttpReqWeshow extends HttpReq {
    private Class<? extends BasicVO> mTargetClass;// 对象类型
    private Integer mResultType = 0;// 结果类型
    private Context mContext;// 上下文

    /**
     * 构造函数
     * 
     * @param context
     *            上下文
     * @param url
     *            请求url
     * @param function
     *            回调对象
     * @param targetClass
     *            返回数据解析后存储对象，可为空
     * @param requestMethod
     *            请求方式
     * @param resultType
     *            返回数据类型BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1
     *            BaseVO.TYPE_OBJECT=2 BaseVO.TYPE_BEAN_LIST=3
     *            BaseVO.TYPE_JSON=4
     * 
     * */
    public HttpReqWeshow(Context context,ReqParam param,String url, HttpCallback function, Class<? extends BasicVO> targetClass,
            String requestMethod, Integer resultType) {
        mContext = context;
        mHost = HttpConfig.CRM_SERVER_NAME;
        mPort = HttpConfig.CRM_SERVER_PORT;
        mUrl = url;
        mParam = param;
        mCallBack = function;
        mTargetClass = targetClass;
        mResultType = resultType;
        mMethod = requestMethod;
    }

    /**
     * @return the mTargetClass
     */
    public Class<? extends BasicVO> getmTargetClass() {
        return mTargetClass;
    }

    /**
     * @param mTargetClass
     *            the mTargetClass to set
     */
    public void setmTargetClass(Class<? extends BasicVO> mTargetClass) {
        this.mTargetClass = mTargetClass;
    }

    /**
     * @return the mResultType
     */
    public Integer getmResultType() {
        return mResultType;
    }

    /**
     * @param mResultType
     *            the mResultType to set
     */
    public void setmResultType(Integer mResultType) {
        this.mResultType = mResultType;
    }

    /**
     * @return the mContext
     */
    public Context getmContext() {
        return mContext;
    }

    /**
     * @param mContext
     *            the mContext to set
     */
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public BasicVO resolveData(Class<? extends BasicVO> targetClass, String dataStr) {
        // TODO
        targetClass.getGenericInterfaces();

        return null;
    }

    @Override
    protected Object processResponse(InputStream response) throws Exception {
        RequestResultModel resultModel = new RequestResultModel();

        if (response != null) {

            InputStream is = response;
            InputStreamReader ireader = new InputStreamReader(is);
            BufferedReader breader = new BufferedReader(ireader);
            StringBuffer sb = new StringBuffer();
            String code = null;
            while ((code = breader.readLine()) != null) {
                sb.append(code);
            }
            breader.close();
            ireader.close();
            Log.d("relst", sb.toString());
            // =============
            if (null != sb && !("".equals(sb.toString()))&& sb.toString().startsWith("{") && sb.toString().endsWith("}")) {
                JSONObject jsonObject = new JSONObject(sb.toString());
                int ret = jsonObject.getInt("ret");
                int errcode = jsonObject.getInt("errcode");
                String msg = jsonObject.getString("msg");
                String dataStr = jsonObject.getString("data");

                System.out.println("====ret : " + ret + ",errcode : " + errcode + ",msg : " + msg + ",data : "
                        + dataStr);

                resultModel.setRet(ret);
                resultModel.setErrcode(errcode);
                resultModel.setMsg(msg);

                // TODO
                // resultModel.setDataVO(resolveData(getmTargetClass(),
                // dataStr));
                resultModel.setData(dataStr);
            } else {
                resultModel.setRet(-1);
                resultModel.setErrcode(0);
                resultModel.setMsg("response has no content.");
            }
            // =============

        } else {
            resultModel.setRet(-1);
            resultModel.setErrcode(0);
            resultModel.setMsg("response no reach.");
        }
        return resultModel;
    }

    @Override
    protected void setReq(HttpMethod method) throws Exception {

        if ("POST".equals(mMethod)) {
            PostMethod post = (PostMethod) method;
            post.addParameter("Connection", "Keep-Alive");
            post.addParameter("Charset", "UTF-8");
            post.setRequestEntity(new ByteArrayRequestEntity(mParam.toString().getBytes("utf-8")));
        }

    }

    /**
     * 向服务器发送请求
     * 
     * @param url
     *            请求url
     * @throws Exception
     * 
     */
    public void setReq(String url) throws Exception {

        if ("POST".equals(mMethod)) {
            PostMethod post = new UTF8PostMethod(mUrl);
            post.setRequestEntity(new ByteArrayRequestEntity(mParam.toString().getBytes("utf-8")));
        }

    }

}
