package com.tencent.weshowsdk.demo;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.weshowsdk.android.api.AuthAPI;
import com.tencent.weshowsdk.android.api.WeshowAPI;
import com.tencent.weshowsdk.android.model.RequestResultModel;
import com.tencent.weshowsdk.android.network.HttpCallback;
import com.tencent.weshowsdk.android.utils.SharePersistent;
import com.tencent.weshowsdk.android.utils.Util;
import com.tencent.weshowsdk.demo.utils.WeshowSDKDemoConst;

public class MainActivity extends ActionBarActivity {

    private static Context context = null;

    private Button authButton = null;
    private Button getTagInfoButton = null;
    private Button getTagTimelineButton = null;
    private Button getTagDiscoveryButton = null;
    private Button getVideoURLButton = null;
    private Button getHotTagsButton = null;
    private Button getChannelTimelineButton = null;
    private Button getTimelineButton = null;
    private Button reportPlayVideoButton = null;
    private Button exitButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        context = this.getApplicationContext();

        /*
         * 顺序：
         * 
         * @order1.获取调用权限（auth/token）
         */
        authButton = (Button) findViewById(R.id.auth_button);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 跳转到授权
                 */
                // Toast.makeText(context, "授权",
                // WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                // Intent intent = new Intent(context, AuthActivity.class);
                // startActivity(intent);
                AuthAPI authAPI = new AuthAPI();

                final String appId = Util.getProperties("APP_KEY");
                final String appSecret = Util.getProperties("APP_KEY_SEC");

                authAPI.auth(context, AuthAPI.RESPONSE_TYPE_CODE, appId, appSecret, new HttpCallback() {

                    @Override
                    public void onResult(Object arg0) {
                        // System.out.println(arg0.toString());
                        RequestResultModel result = (RequestResultModel) arg0;
                        // {"expires_in":1398019616,"access_token":"MynwOe3A+hp0NldinfD1MTwYpJ\/Q5gnce\/i0Nd+ELB5Af0YaBUZsXFBPyTWnvgbYLimtsTsatcvITMyFIWnUgKp0CL8QycaL6AFSxwZp54hOfq0nF1214Mfd9kk0HuMj"}
                        int ret = result.getRet();
                        String dataStr = result.getData();
                        if (0 == ret) {
                            if (null != dataStr && !"".equals(dataStr)) {
                                try {
                                    JSONObject jsonObj = new JSONObject(dataStr);
                                    String accessToken = jsonObj.getString("access_token");
                                    long expiresIn = jsonObj.getLong("expires_in");
                                    long authorizeTime = System.currentTimeMillis() / 1000;

                                    SharePersistent.getInstance().put(context, "ACCESS_TOKEN", accessToken);
                                    SharePersistent.getInstance().put(context, "EXPIRES_IN", expiresIn);
                                    SharePersistent.getInstance().put(context, "AUTHORIZETIME", authorizeTime);

                                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                    i.putExtra("data", dataStr);
                                    startActivity(i);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //
                                int errCode = result.getErrcode();
                                String msg = result.getMsg();

                                Toast.makeText(MainActivity.this, "获取调用权限\n 错误码:" + errCode + "\n错误信息:" + msg,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int errCode = result.getErrcode();
                            String msg = result.getMsg();

                            Toast.makeText(MainActivity.this,
                                    "获取调用权限\n 返回码:" + ret + "错误码:" + errCode + "\n错误信息:" + msg, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }, null, 0);
            }
        });
        /*
         * 顺序：
         * 
         * @order2.获取热门频道名称（weishi/tags）
         */
        getTagInfoButton = (Button) findViewById(R.id.get_tags_button);
        getTagInfoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取热门频道名称", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();

                //
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());
                final String clientId = Util.getProperties("APP_KEY");
                if (null != weshowAPI) {
                    weshowAPI.getTags(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1, clientId,
                            new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel requestResultModel = (RequestResultModel) object;
                                    int ret = requestResultModel.getRet();
                                    if (0 == ret) {
                                        //
                                        String dataStr = requestResultModel.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = requestResultModel.getErrcode();
                                        String msg = requestResultModel.getMsg();

                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取标签失败\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }

                                }
                            }, null, 0);
                }

            }
        });
        /*
         * 顺序：
         * 
         * @order3.获取频道微视消息（weishi/channeltimeline）
         */
        getChannelTimelineButton = (Button) findViewById(R.id.get_channel_timeline_button);
        getChannelTimelineButton.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取频道微视消息", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    final String key = URLEncoder.encode("美女");
                    int start = 0;
                    int reqNum = 20;
                    int pageFlag = 0;
                    int lastId = 0;
                    int pageTime = 0;
                    int withOP = 1;
                    weshowAPI.getChannelTimeline(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1,
                            clientId, key, start, reqNum, pageFlag, lastId, pageTime, withOP, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", key + " : " + dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取频道微视消息\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);
                }
            }
        });

        /*
         * 顺序：
         * 
         * @order4.获取热门标签名称（weishi/tagdiscovery）
         */
        getTagDiscoveryButton = (Button) findViewById(R.id.get_tag_discovery_button);
        getTagDiscoveryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取热门标签名称", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();

                //
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());
                final String clientId = Util.getProperties("APP_KEY");
                if (null != weshowAPI) {
                    int start = 0;
                    int reqNum = 20;
                    weshowAPI.tagDiscovery(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1,
                            clientId, start, reqNum, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel requestResultModel = (RequestResultModel) object;
                                    int ret = requestResultModel.getRet();
                                    if (0 == ret) {
                                        //
                                        String dataStr = requestResultModel.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = requestResultModel.getErrcode();
                                        String msg = requestResultModel.getMsg();

                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取热门标签名称\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }

                                }
                            }, null, 0);
                }

            }
        });

        /*
         * 顺序：
         * 
         * @order5.获取推荐标签名称（weishi/hottag）
         */
        getHotTagsButton = (Button) findViewById(R.id.get_hot_tags_button);
        getHotTagsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取推荐标签名称", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    //
                    weshowAPI.getHotTag(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1, clientId,
                            new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取推荐标签名称\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);
                }
            }
        });

        /*
         * 顺序：
         * 
         * @order6.获取标签微视消息（weishi/timeline）
         */
        getTagTimelineButton = (Button) findViewById(R.id.get_tag_timeline_button);
        getTagTimelineButton.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取标签信息", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    //
                    // type 可选，请求类型，0标签全部timeline，1运营标签timeline.建议填1
                    int type = 1;
                    // tag 标签名称，由tag接口返回，最长32字节
                    String tag = URLEncoder.encode("美女");
                    // start 起始位置（当lastid或 pagetime为0时，使用start计算起始位置）
                    int start = 0;
                    // reqNum 请求个数，一次最多20条
                    int reqNum = 10;
                    // pageFlag 翻页选项：0-拉首页，1-向上翻页，2-向下翻页）
                    int pageFlag = 0;
                    /*
                     * lastId pageflag=0，使用start为起始位置，向下读取reqnum条消息
                     * pageflag=1表示向上翻页：lastid和pagetime是下一页的第一个帖子ID和时间；
                     * pageflag=2表示向下翻页：lastid和pagetime是上一页的最后一个帖子ID和时间
                     */
                    int lastId = 0;
                    // pageTime pageflag为0时该字段填0，否则参照lastid说明填写
                    int pageTime = 0;
                    weshowAPI.getTagTimeline(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1,
                            clientId, type, tag, start, reqNum, pageFlag, lastId, pageTime, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Log.d("data", dataStr);
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取时间线错误\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);

                }
            }
        });

        /*
         * 顺序：
         * 
         * @order7.获取视频播放地址（weishi/getvideourl）
         */
        getVideoURLButton = (Button) findViewById(R.id.get_video_url_button);
        getVideoURLButton.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取视频播放地址", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    /**
                     * @param id
                     *            微视id，可以通过timeline接口获取
                     * @param vid
                     *            微视视频id，可以通过timeline接口获取
                     * @param device
                     *            设备类型，只能填写为ipad-1，pc-2，android
                     *            phone-3，iphone-4，android pad-5，android
                     *            tv-6，win phone-7，win pad-8中的一种
                     * @param play
                     *            播放方式，只能填写为：HTML5-1，flash-2，app-3，client-4，WAP-
                     *            5，activex-6
                     * @param fmt
                     *            用户指定的播放格式名称只能指定为：mp4，flv:默认，flvsd:标清，hd:高清，shd
                     *            :超清，fhd:全高清，msd: 手机
                     */
                    final String tag = URLEncoder.encode("我是潮虫儿");
                    String id = "2004517055698919";
                    String vid = "1008_77acd40ca195411384c8388396391cc3";
                    String device = URLEncoder.encode("android phone-3");
                    String play = "flash-2";
                    String fmt = "flv";
                    weshowAPI.getVideoURL(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1,
                            clientId, id, vid, device, play, fmt, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", tag + " : " + dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取视频播放地址\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);
                }
            }
        });

        /*
         * 顺序：
         * 
         * @order8.获取某人原创微视消息（weishi/other）
         */
        getTimelineButton = (Button) findViewById(R.id.get_orig_timeline_button);
        getTimelineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "获取某人原创微视消息", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    //
                    int reqNum = 20;
                    int pageFlag = 0;
                    int lastId = 0;
                    int pageTime = 0;
                    String uid = "4342083";
                    final String name = "Kiki周琪";
                    weshowAPI.getOrigWeshows(context, WeshowAPI.PROTOCOL_TYPE_WBOX, WeshowAPI.PROTOCOL_VERSION_1,
                            clientId, reqNum, pageFlag, lastId, pageTime, uid, name, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", name + " : " + dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "获取某人原创微视消息\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);
                }
            }
        });

        /*
         * 顺序：
         * 
         * @order9.视频播放次数上报
         */
        reportPlayVideoButton = (Button) findViewById(R.id.report_play_video_button);
        reportPlayVideoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "视频播放次数上报", WeshowSDKDemoConst.TOAST_STAY_1_SECOND).show();
                WeshowAPI weshowAPI = WeshowAPI.getWeshowAPI(getApplicationContext());

                //
                if (null != weshowAPI) {
                    final String clientId = Util.getProperties("APP_KEY");
                    final String accessToken = SharePersistent.getInstance().get(context, "ACCESS_TOKEN");
                    String idinfos = "2007022131987130:1:100:tag:576O5aWz,2006022128793916:1:100:channel:576O5aWz,2005522128659902:1:100:wsid:MzM1MzE1Mg==";
                    String imei = "VVVVVVVVVVVVVVVVVV";
                    String version = "1.0";
                    String appversion = "2.2a";
                    
                    weshowAPI.playReport(context, WeshowAPI.PROTOCOL_TYPE_WBOX, version, clientId, accessToken,
                            idinfos, 2, imei, appversion, clientId, 1, new HttpCallback() {

                                @Override
                                public void onResult(Object object) {
                                    RequestResultModel result = (RequestResultModel) object;
                                    int ret = result.getRet();
                                    if (0 == ret) {
                                        String dataStr = result.getData();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", dataStr);
                                        startActivity(i);
                                    } else {
                                        int errCode = result.getErrcode();
                                        String msg = result.getMsg();
                                        Intent i = new Intent(MainActivity.this, GeneralDataShowActivity.class);
                                        i.putExtra("data", "播放上报失败\n错误码:" + errCode + "\n错误信息:" + msg);
                                        startActivity(i);
                                    }
                                }
                            }, null, 0);
                }
            }
        });

        // 退出
        exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
