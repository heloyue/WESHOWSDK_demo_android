/**
 * 
 */
package com.tencent.weshowsdk.android.api;

import android.content.Context;
import android.widget.Toast;

import com.tencent.weshowsdk.android.model.AuthVO;
import com.tencent.weshowsdk.android.model.BasicVO;
import com.tencent.weshowsdk.android.network.HttpCallback;
import com.tencent.weshowsdk.android.network.ReqParam;
import com.tencent.weshowsdk.android.utils.SharePersistent;
import com.tencent.weshowsdk.android.utils.Util;

/**
 * @author ISAACXIE
 * 
 */
public class WeshowAPI extends BaseAPI {

    /**
     * 2. 获取标签信息URL
     */
    public static final String WESHOW_URL_TAGS = "/weishi/tags";

    /**
     * 3. 获取某个标签下的微视信息URL
     */
    public static final String WESHOW_URL_TIMELINE = "/weishi/timeline";

    /**
     * 4. 获取微视视频下载地址URL
     */
    public static final String WESHOW_URL_GETVIDEOURL = "/weishi/getvideourl";

    /**
     * 5. 获取热门标签列表URL
     */
    public static final String WESHOW_URL_HOTTAG = "/weishi/hotTag";

    /**
     * 6. 获取频道timeline URL
     */
    public static final String WESHOW_URL_CHANNEL_TIMELINE = "/weishi/channelTimeline";

    /**
     * 7. 获取某人原创广播列表URL
     */
    public static final String WESHOW_URL_OTHER = "/weishi/other";

    /**
     * 8. 获取热门标签列表URL
     */
    public static final String WESHOW_URL_TAG_DISCOVERY = "/weishi/tagDiscovery";

    /**
     * 9.视频播放次数上报接口（必须使用）
     */
    public static final String WESHOW_URL_PLAY_REPORT = "/weishi/playReport";

    /**
     * protocol_type wbox
     */
    public static final String PROTOCOL_TYPE_WBOX = "wbox";

    /**
     * version 1.0
     */
    public static final String PROTOCOL_VERSION_1 = "1.0";

    private static WeshowAPI weshowAPI = null;

    private WeshowAPI() {
        super();
    }

    /**
     * @param account
     */
    private WeshowAPI(AuthVO account) {
        super(account);
    }

    public static WeshowAPI getWeshowAPI(Context context) {

        String accessToken = SharePersistent.getInstance().get(context, "ACCESS_TOKEN");

        if (accessToken == null || "".equals(accessToken)) {
            Toast.makeText(context, "尚未授权，请先授权", Toast.LENGTH_SHORT).show();
            return weshowAPI;
        } else if (Util.isAuthorizeExpired(context)) {
            Toast.makeText(context, "授权过去，请重新授权", Toast.LENGTH_SHORT).show();
            return weshowAPI;
        } else {
            AuthVO account = new AuthVO();
            long expiresIn = SharePersistent.getInstance().getLong(context, "EXPIRES_IN");

            account.setAccessToken(accessToken);
            account.setExpiresIn(expiresIn);

            if (weshowAPI == null) {
                weshowAPI = new WeshowAPI(account);
            } else {
                weshowAPI.setmAuthInfo(account);
            }
            return weshowAPI;
        }
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param client_id
     *            第三方appid
     * @param access_token
     *            第三方调用的票据
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getTags(Context context, String protocolType, String version, String clientId, HttpCallback mCallBack,
            Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        // mParam.addParam("protocol_type", PROTOCOL_TYPE_WBOX);
        // mParam.addParam("version", "1.0");
        // mParam.addParam("client_id",
        // Util.getConfig().getProperty("APP_KEY"));
        // mParam.addParam("access_token", Util.getSharePersistent(context,
        // "ACCESS_TOKEN"));

        startRequest(context, API_SERVER + WESHOW_URL_TAGS, mParam, mCallBack, mTargetClass, BaseAPI.HTTP_METHOD_GET,
                resultType);
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param type
     *            可选，请求类型，0标签全部timeline，1运营标签timeline.建议填1
     * @param tag
     *            标签名称，由tag接口返回，最长32字节
     * @param start
     *            起始位置（当lastid或 pagetime为0时，使用start计算起始位置）
     * @param reqNum
     *            请求个数，一次最多20条
     * @param pageFlag
     *            翻页选项：0-拉首页，1-向上翻页，2-向下翻页）
     * @param lastId
     *            pageflag=0，使用start为起始位置，向下读取reqnum条消息
     *            pageflag=1表示向上翻页：lastid和pagetime是下一页的第一个帖子ID和时间；
     *            pageflag=2表示向下翻页：lastid和pagetime是上一页的最后一个帖子ID和时间
     * @param pageTime
     *            pageflag为0时该字段填0，否则参照lastid说明填写
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getTagTimeline(Context context, String protocolType, String version, String clientId, int type,
            String tag, int start, int reqNum, int pageFlag, int lastId, int pageTime, HttpCallback mCallBack,
            Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        mParam.addParam("type", type);
        mParam.addParam("tag", tag);
        mParam.addParam("start", start);
        mParam.addParam("reqnum", reqNum);
        mParam.addParam("pageflag", pageFlag);
        mParam.addParam("lastid", lastId);
        mParam.addParam("pagetime", pageTime);

        startRequest(context, API_SERVER + WESHOW_URL_TIMELINE, mParam, mCallBack, mTargetClass,
                BaseAPI.HTTP_METHOD_GET, resultType);
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param id
     *            微视id，可以通过timeline接口获取
     * @param vid
     *            微视视频id，可以通过timeline接口获取
     * @param device
     *            设备类型，只能填写为ipad-1，pc-2，android phone-3，iphone-4，android
     *            pad-5，android tv-6，win phone-7，win pad-8中的一种
     * @param play
     *            播放方式，只能填写为：HTML5-1，flash-2，app-3，client-4，WAP-5，activex-6
     * @param fmt
     *            用户指定的播放格式名称只能指定为：mp4，flv:默认，flvsd:标清，hd:高清，shd:超清，fhd:全高清，msd:
     *            手机
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getVideoURL(Context context, String protocolType, String version, String clientId, String id,
            String vid, String device, String play, String fmt, HttpCallback mCallBack,
            Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());
        mParam.addParam("id", id);
        mParam.addParam("vid", vid);
        mParam.addParam("device", device);
        mParam.addParam("play", play);
        mParam.addParam("fmt", fmt);

        startRequest(context, API_SERVER + WESHOW_URL_GETVIDEOURL, mParam, mCallBack, mTargetClass,
                BaseAPI.HTTP_METHOD_GET, resultType);
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getHotTag(Context context, String protocolType, String version, String clientId,
            HttpCallback mCallBack, Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        startRequest(context, API_SERVER + WESHOW_URL_HOTTAG, mParam, mCallBack, mTargetClass, BaseAPI.HTTP_METHOD_GET,
                resultType);
    }

    /*
     * key 是 start 否 reqnum 否 pageflag 否 lastid 否 pagetime 否 withop 否
     */
    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param key
     *            频道名
     * @param start
     *            起始位置（当lastid或 pagetime为0时，使用start计算起始位置）
     * @param reqNum
     *            请求个数，一次最多20条
     * @param pageFlag
     *            翻页选项：0-拉首页，1-向上翻页，2-向下翻页）
     * @param lastId
     *            pageflag=0，使用start为起始位置，向下读取reqnum条消息
     *            pageflag=1表示向上翻页：lastid和pagetime是下一页的第一个帖子ID和时间；
     *            pageflag=2表示向下翻页：lastid和pagetime是上一页的最后一个帖子ID和时间
     * @param pageTime
     *            pageflag为0时该字段填0，否则参照lastid说明填写
     * @param withOP
     *            最新频道timeline是否需要带运营消息，1带，0不带
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getChannelTimeline(Context context, String protocolType, String version, String clientId, String key,
            int start, int reqNum, int pageFlag, int lastId, int pageTime, int withOP, HttpCallback mCallBack,
            Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        mParam.addParam("key", key);
        mParam.addParam("start", start);
        mParam.addParam("reqnum", reqNum);
        mParam.addParam("pageflag", pageFlag);

        mParam.addParam("lastid", lastId);
        mParam.addParam("pagetime", pageTime);
        mParam.addParam("withop", withOP);

        startRequest(context, API_SERVER + WESHOW_URL_CHANNEL_TIMELINE, mParam, mCallBack, mTargetClass,
                BaseAPI.HTTP_METHOD_GET, resultType);
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param reqNum
     *            请求个数，一次最多20条
     * @param pageFlag
     *            翻页选项：0-拉首页，1-向上翻页，2-向下翻页）
     * @param lastId
     *            pageflag=0，使用start为起始位置，向下读取reqnum条消息
     *            pageflag=1表示向上翻页：lastid和pagetime是下一页的第一个帖子ID和时间；
     *            pageflag=2表示向下翻页：lastid和pagetime是上一页的最后一个帖子ID和时间
     * @param pageTime
     *            pageflag为0时该字段填0，否则参照lastid说明填写
     * @param uid
     *            微视id，新请求用uid替换name参数，name参数暂时保留以兼容旧版本
     * @param name
     *            微视账号名
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void getOrigWeshows(Context context, String protocolType, String version, String clientId, int reqNum,
            int pageFlag, int lastId, int pageTime, String uid, String name, HttpCallback mCallBack,
            Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        mParam.addParam("reqnum", reqNum);
        mParam.addParam("pageflag", pageFlag);
        mParam.addParam("lastid", lastId);
        mParam.addParam("pagetime", pageTime);

        mParam.addParam("uid", uid);
        mParam.addParam("name", name);

        startRequest(context, API_SERVER + WESHOW_URL_OTHER, mParam, mCallBack, mTargetClass, BaseAPI.HTTP_METHOD_GET,
                resultType);
    }

    /**
     * 
     * @param context
     * @param protocolType
     *            鉴权的协议类型，填wbox
     * @param version
     *            协议版本，填1.0
     * @param clientId
     *            第三方appid
     * @param accessToken
     *            第三方调用的票据
     * @param start
     *            请求开始偏移
     * @param reqNum
     *            请求条数，默认20条
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void tagDiscovery(Context context, String protocolType, String version, String clientId, int start,
            int reqNum, HttpCallback mCallBack, Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", getmAuthInfo().getAccessToken());

        mParam.addParam("start", start);
        mParam.addParam("reqnum", reqNum);

        startRequest(context, API_SERVER + WESHOW_URL_TAG_DISCOVERY, mParam, mCallBack, mTargetClass,
                BaseAPI.HTTP_METHOD_GET, resultType);
    }

    /**
     * 播放次数上报
     * 
     * @param context
     * @param protocolType
     * @param version
     * @param clientId
     * @param accessToken
     * @param idinfos
     * @param platform
     * @param imei
     * @param appversion
     * @param appid
     * @param test
     * @param mCallBack
     * @param mTargetClass
     * @param resultType
     */
    public void playReport(Context context, String protocolType, String version, String clientId, String accessToken,
            String idinfos, int platform, String imei, String appversion, String appid, int test,
            HttpCallback mCallBack, Class<? extends BasicVO> mTargetClass, int resultType) {
        ReqParam mParam = new ReqParam();

        mParam.addParam("protocol_type", protocolType);
        mParam.addParam("version", version);
        mParam.addParam("client_id", clientId);
        mParam.addParam("access_token", accessToken);

        mParam.addParam("idinfos", idinfos);
        mParam.addParam("platform", platform);
        mParam.addParam("imei", imei);
        mParam.addParam("appversion", appversion);
        mParam.addParam("appid", appid);
        mParam.addParam("test", test);

        startRequest(context, API_SERVER + WESHOW_URL_PLAY_REPORT, mParam, mCallBack, mTargetClass,
                BaseAPI.HTTP_METHOD_POST, resultType);
    }

}
