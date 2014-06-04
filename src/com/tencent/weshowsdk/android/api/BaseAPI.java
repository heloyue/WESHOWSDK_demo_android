/**
 * 
 */
package com.tencent.weshowsdk.android.api;

import java.util.Map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;

import com.tencent.weshowsdk.android.model.AuthVO;
import com.tencent.weshowsdk.android.model.BasicVO;
import com.tencent.weshowsdk.android.network.HttpCallback;
import com.tencent.weshowsdk.android.network.HttpReqWeshow;
import com.tencent.weshowsdk.android.network.HttpService;
import com.tencent.weshowsdk.android.network.ReqParam;
import com.tencent.weshowsdk.android.utils.SharePersistent;
import com.tencent.weshowsdk.android.utils.Util;

/**
 * @author isaacxie
 * 
 */
public abstract class BaseAPI {

    /**
	 * 
	 */
    private AuthVO mAuthInfo;

    /**
	 * 
	 */
    private HttpReqWeshow weshow;

    /**
	 * 
	 */
    private Context mContext;

    /**
	 * 
	 */
    private String mRequestUrl;

    /**
	 * 
	 */
    private ReqParam mParams;

    /**
	 * 
	 */
    private Class<? extends BasicVO> mmTargetClass;

    /**
	 * 
	 */
    private HttpCallback mmCallBack;

    /**
	 * 
	 */
    private String mRequestMethod;

    /**
	 * 
	 */
    private int mResultType;

    /**
     * API服务接口调用地址
     */
    public static final String API_SERVER = "https://open.t.qq.com/api";

    public static final String REPORT_DATA_URL = "http://btrace.qq.com/collect";

    /**
     * post请求方式
     */
    public static final String HTTP_METHOD_POST = "POST";

    /**
     * get请求方式
     */
    public static final String HTTP_METHOD_GET = "GET";

    public BaseAPI() {
    }

    /**
     * 
     * @param account
     */
    public BaseAPI(AuthVO account) {
        mAuthInfo = account;
    }

    /**
     * @param context
     *            上下文
     * 
     * @param requestUrl
     *            请求url
     * 
     * @param params
     *            请求参数
     * 
     * @param mCallBack
     *            回调对象
     * 
     * @param mTargetClass
     *            返回对象类型
     * 
     * @param requestMethod
     *            请求方式 get 或者 post
     * 
     * @param resultType
     *            返回结果类型 BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1
     *            BaseVO.TYPE_OBJECT=2 BaseVO.TYPE_BEAN_LIST=3
     *            BaseVO.TYPE_JSON=4
     */
    protected void startRequest(Context context, final String requestUrl, final ReqParam params,
            HttpCallback mCallBack, Class<? extends BasicVO> mTargetClass, String requestMethod, int resultType) {
        mContext = context;
        mRequestUrl = requestUrl;
        mParams = params;
        mmCallBack = mCallBack;
        mmTargetClass = mTargetClass;
        mRequestMethod = requestMethod;
        mResultType = resultType;
        weshow = new HttpReqWeshow(mContext, mParams, mRequestUrl, mmCallBack, mmTargetClass, mRequestMethod,
                mResultType);

        HttpService.getInstance().addImmediateReq(weshow);

        // 数据上报
        ReqParam param = getReportDatas(mContext);
        Map<String, String> paramMap = param.getmParams();
        StringBuffer paramStrBuf = new StringBuffer();
        /*
         * sIp=127.0.0.1 &iQQ=10001 &sBiz=zhibo &sOp=click &iSta=0 &iTy=2136
         * &iFlow= &fCtime= &sUni= &sIMEI= &sIMSI= &sDevice= &sOs= &sOsver=
         * &sCver= &sScreen= &iOper= &iNet= &fLat= &fLng= &iCountry=
         * &iChannelId= &appkey=
         */
        paramStrBuf.append("sIp=").append(paramMap.get("sIp")).append("&");
        paramStrBuf.append("iQQ=").append(paramMap.get("iQQ")).append("&");
        paramStrBuf.append("sBiz=").append(paramMap.get("sBiz")).append("&");
        paramStrBuf.append("sOp=").append(paramMap.get("sOp")).append("&");
        paramStrBuf.append("iSta=").append(paramMap.get("iSta")).append("&");
        paramStrBuf.append("iTy=").append(paramMap.get("iTy")).append("&");
        paramStrBuf.append("iFlow=").append(paramMap.get("iFlow")).append("&");
        paramStrBuf.append("fCtime=").append(paramMap.get("fCtime")).append("&");
        paramStrBuf.append("sUni=").append(paramMap.get("sUni")).append("&");
        paramStrBuf.append("sIMEI=").append(paramMap.get("sIMEI")).append("&");
        paramStrBuf.append("sIMSI=").append(paramMap.get("sIMSI")).append("&");
        paramStrBuf.append("sDevice=").append(paramMap.get("sDevice")).append("&");
        paramStrBuf.append("sOs=").append(paramMap.get("sOs")).append("&");
        paramStrBuf.append("sOsver=").append(paramMap.get("sOsver")).append("&");
        paramStrBuf.append("sCver=").append(paramMap.get("sCver")).append("&");
        paramStrBuf.append("sScreen=").append(paramMap.get("sScreen")).append("&");
        paramStrBuf.append("iOper=").append(paramMap.get("iOper")).append("&");
        paramStrBuf.append("iNet=").append(paramMap.get("iNet")).append("&");
        paramStrBuf.append("fLat=").append(paramMap.get("fLat")).append("&");
        paramStrBuf.append("fLng=").append(paramMap.get("fLng")).append("&");
        paramStrBuf.append("iCountry=").append(paramMap.get("iCountry")).append("&");
        paramStrBuf.append("iChannelId=").append(paramMap.get("iChannelId")).append("&");
        paramStrBuf.append("appkey=").append(paramMap.get("appkey"));

        String reportURL = REPORT_DATA_URL + "?" + paramStrBuf.toString();
        HttpReqWeshow reportRequest = new HttpReqWeshow(mContext, null, reportURL, new HttpCallback() {

            @Override
            public void onResult(Object object) {
                // TODO Auto-generated method stub
            }
        }, null, HTTP_METHOD_GET, 0);
        HttpService.getInstance().addNormalReq(reportRequest);
    }

    /**
     * @return the mAuthInfo
     */
    public AuthVO getmAuthInfo() {
        return mAuthInfo;
    }

    /**
     * @param mAuthInfo
     *            the mAuthInfo to set
     */
    public void setmAuthInfo(AuthVO mAuthInfo) {
        this.mAuthInfo = mAuthInfo;
    }

    /**
     * @return the weshow
     */
    public HttpReqWeshow getWeshow() {
        return weshow;
    }

    /**
     * @param weshow
     *            the weshow to set
     */
    public void setWeshow(HttpReqWeshow weshow) {
        this.weshow = weshow;
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

    /**
     * @return the mRequestUrl
     */
    public String getmRequestUrl() {
        return mRequestUrl;
    }

    /**
     * @param mRequestUrl
     *            the mRequestUrl to set
     */
    public void setmRequestUrl(String mRequestUrl) {
        this.mRequestUrl = mRequestUrl;
    }

    /**
     * @return the mParams
     */
    public ReqParam getmParams() {
        return mParams;
    }

    /**
     * @param mParams
     *            the mParams to set
     */
    public void setmParams(ReqParam mParams) {
        this.mParams = mParams;
    }

    /**
     * @return the mmTargetClass
     */
    public Class<? extends BasicVO> getMmTargetClass() {
        return mmTargetClass;
    }

    /**
     * @param mmTargetClass
     *            the mmTargetClass to set
     */
    public void setMmTargetClass(Class<? extends BasicVO> mmTargetClass) {
        this.mmTargetClass = mmTargetClass;
    }

    /**
     * @return the mmCallBack
     */
    public HttpCallback getMmCallBack() {
        return mmCallBack;
    }

    /**
     * @param mmCallBack
     *            the mmCallBack to set
     */
    public void setMmCallBack(HttpCallback mmCallBack) {
        this.mmCallBack = mmCallBack;
    }

    /**
     * @return the mRequestMethod
     */
    public String getmRequestMethod() {
        return mRequestMethod;
    }

    /**
     * @param mRequestMethod
     *            the mRequestMethod to set
     */
    public void setmRequestMethod(String mRequestMethod) {
        this.mRequestMethod = mRequestMethod;
    }

    /**
     * @return the mResultType
     */
    public int getmResultType() {
        return mResultType;
    }

    /**
     * @param mResultType
     *            the mResultType to set
     */
    public void setmResultType(int mResultType) {
        this.mResultType = mResultType;
    }

    private ReqParam getReportDatas(Context context) {
        ReqParam param = new ReqParam();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        /*
         * http://tool.boss.webdev.com/items/bosslog/idmgr/view_bossid_info.htm?
         * bossid=2136
         * http://btrace.qq.com/collect?sIp=127.0.0.1&iQQ=10001&sBiz=
         * zhibo&sOp=click
         * &iSta=0&iTy=2136&iFlow=&fCtime=&sUni=&sIMEI=&sIMSI=&sDevice
         * =&sOs=&sOsver
         * =&sCver=&sScreen=&iOper=&iNet=&fLat=&fLng=&iCountry=&iChannelId
         * =&appkey=
         * 
         * appkey sCver sOsver sOs sDevice sIMEI sBOSSID sUni sBiz
         */

        /*
         * sIp=127.0.0.1
         */
        String sIP = SharePersistent.getInstance().get(context, "SIP");
        if (null == sIP || "".equals(sIP)) {
            sIP = Util.getLocalIPAddress(context);
            SharePersistent.getInstance().put(context, "SIP", sIP);
        }
        param.addParam("sIp", sIP);
        /*
         * iQQ=10001
         */
        long iQQ = SharePersistent.getInstance().getLong(context, "IQQ");
        if (0L == iQQ) {
            iQQ = 0L;
            SharePersistent.getInstance().put(context, "IQQ", iQQ);
        }
        param.addParam("iQQ", iQQ);
        /*
         * sBiz=zhibo
         */
        String sBiz = SharePersistent.getInstance().get(context, "SBIZ");
        if (null == sBiz || "".equals(sBiz)) {
            sBiz = "weishi";
            SharePersistent.getInstance().put(context, "SBIZ", sBiz);
        }
        param.addParam("sBiz", sBiz);
        /*
         * sOp=click
         */
        String sOp = SharePersistent.getInstance().get(context, "SOP");
        if (null == sOp || "".equals(sOp)) {
            sOp = "click";
            SharePersistent.getInstance().put(context, "SOP", sOp);
        }
        param.addParam("sOp", sOp);
        /*
         * iSta=0
         */
        long iSta = SharePersistent.getInstance().getLong(context, "ISTA");
        if (0 == iSta) {
            iSta = 1;
            SharePersistent.getInstance().put(context, "ISTA", iSta);
        }
        param.addParam("iSta", iSta);

        /*
         * iTy=2136
         */
        long iTy = SharePersistent.getInstance().getLong(context, "ITY");
        if (0 == iTy) {
            iTy = 2136;
            SharePersistent.getInstance().put(context, "ITY", iTy);
        }
        param.addParam("iTy", iTy);
        /*
         * iFlow=
         */
        long iFlow = SharePersistent.getInstance().getLong(context, "IFLOW");
        if (0L == iFlow) {
            iFlow = 0L;
            SharePersistent.getInstance().put(context, "IFLOW", iFlow);
        }
        param.addParam("iFlow", iFlow);
        /*
         * fCtime=
         */
        long fCtime = SharePersistent.getInstance().getLong(context, "FCTIME");
        if (0L == fCtime) {
            fCtime = 0L;
            SharePersistent.getInstance().put(context, "FCTIME", fCtime);
        }
        param.addParam("fCtime", fCtime);
        /*
         * 设备标识 sUni=
         */
        String sUni = SharePersistent.getInstance().get(context, "SUNI");
        if (null == sUni || "".equals(sUni)) {
            sUni = telephonyManager.getDeviceId();
            SharePersistent.getInstance().put(context, "SUNI", sUni);
        }
        param.addParam("sUni", sUni);
        /*
         * sIMEI=
         */
        String sIMEI = SharePersistent.getInstance().get(context, "SIMEI");
        if (null == sIMEI || "".equals(sIMEI)) {
            sIMEI = "";
            SharePersistent.getInstance().put(context, "SIMEI", sIMEI);
        }
        param.addParam("sIMEI", sIMEI);
        /*
         * sIMSI=
         */
        String sIMSI = SharePersistent.getInstance().get(context, "SIMSI");
        if (null == sIMSI || "".equals(sIMSI)) {
            sIMSI = telephonyManager.getSubscriberId();
            SharePersistent.getInstance().put(context, "SIMSI", sIMSI);
        }
        param.addParam("sIMSI", sIMSI);
        /*
         * 设备型号 sDevice=
         */
        String sDevice = SharePersistent.getInstance().get(context, "SDEVICE");
        if (null == sDevice || "".equals(sDevice)) {
            sDevice = android.os.Build.BRAND + "_" + android.os.Build.MODEL + "_" + android.os.Build.PRODUCT;
            System.out.println("sDevice === :  " + sDevice);
            SharePersistent.getInstance().put(context, "SDEVICE", sDevice);
        }
        param.addParam("sDevice", sDevice);
        /*
         * sOs=
         */
        String sOs = SharePersistent.getInstance().get(context, "SOS");
        if (null == sOs || "".equals(sOs)) {
            sOs = "Android";
            System.out.println("sOs === :  " + sOs);
            SharePersistent.getInstance().put(context, "SOS", sOs);
        }
        param.addParam("sOs", sOs);
        /*
         * sOsver=
         */
        String sOsver = SharePersistent.getInstance().get(context, "SOSVER");
        if (null == sOsver || "".equals(sOsver)) {
            sOsver = android.os.Build.VERSION.RELEASE;
            System.out.println("sOsver === :  " + sOsver);
            SharePersistent.getInstance().put(context, "SOSVER", sOsver);
        }
        param.addParam("sOsver", sOsver);
        /*
         * sCver=
         */
        String sCver = SharePersistent.getInstance().get(context, "SCVER");
        if (null == sCver || "".equals(sCver)) {
            sCver = "1.0";
            SharePersistent.getInstance().put(context, "SCVER", sCver);
        }
        param.addParam("sCver", sCver);
        /*
         * sScreen=
         */
        String sScreen = SharePersistent.getInstance().get(context, "SSCREEN");
        if (null == sScreen || "".equals(sScreen)) {
            sScreen = "nul";
            SharePersistent.getInstance().put(context, "SSCREEN", sScreen);
        }
        param.addParam("sScreen", sScreen);
        /*
         * iOper=
         */
        String iOper = SharePersistent.getInstance().get(context, "IOPER");
        if (null == iOper || "".equals(iOper)) {
            iOper = telephonyManager.getSimCountryIso();
            SharePersistent.getInstance().put(context, "IOPER", iOper);
        }
        param.addParam("iOper", iOper);
        /*
         * iNet=
         */
        String iNet = SharePersistent.getInstance().get(context, "INET");
        if (null == iNet || "".equals(iNet)) {
            iNet = "nul";
            SharePersistent.getInstance().put(context, "INET", iNet);
        }
        param.addParam("iNet", iNet);
        
        // -------------
        // 获取系统LocationManager服务
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 从GPS获取最近的定位信息
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // 从NetWork获取最近的定位信息
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            location = null;
        }
        String fLat = "unknow";
        String fLng = "unknow";
        if (null != location) {
            fLat = location.getLatitude() + "";
            fLng = location.getLongitude() + "";
        }
        param.addParam("fLat", fLat);
        param.addParam("fLng", fLng);

        // --------------------
        /*
         * fLat=
         */
        // String fLat = SharePersistent.getInstance().get(context, "FLAT");
        // if (null == fLat || "".equals(fLat)) {
        // fLat = "null";
        // //SharePersistent.getInstance().put(context, "FLAT", fLat);
        // }
        // param.addParam("fLat", fLat);
        /*
         * fLng=
         */
        // String fLng = SharePersistent.getInstance().get(context, "FLNG");
        // if (null == fLng || "".equals(fLng)) {
        // fLng = "nul";
        // SharePersistent.getInstance().put(context, "FLNG", fLng);
        // }

        /*
         * iCountry=
         */
        String iCountry = SharePersistent.getInstance().get(context, "ICOUNTRY");
        if (null == iCountry || "".equals(iCountry)) {
            iCountry = telephonyManager.getNetworkCountryIso();
            SharePersistent.getInstance().put(context, "ICOUNTRY", iCountry);
        }
        param.addParam("iCountry", iCountry);
        /*
         * iChannelId=
         */
        String iChannelId = SharePersistent.getInstance().get(context, "ICHANNELID");
        if (null == iChannelId || "".equals(iChannelId)) {
            iChannelId = "nul";
            SharePersistent.getInstance().put(context, "ICHANNELID", iChannelId);
        }
        param.addParam("iChannelId", iChannelId);
        /*
         * appkey=
         */
        String appkey = SharePersistent.getInstance().get(context, "APPKEY");
        if (null == appkey || "".equals(appkey)) {
            appkey = Util.getProperties("APP_KEY");
            SharePersistent.getInstance().put(context, "APPKEY", appkey);
        }
        param.addParam("appkey", appkey);

        return param;
    }
}
