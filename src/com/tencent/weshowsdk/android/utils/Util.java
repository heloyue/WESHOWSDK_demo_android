package com.tencent.weshowsdk.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Util {

    /**
     * 清除全部缓存数据
     * 
     * @param context
     *            上下文 *
     * 
     */
    public static void clearSharePersistent(Context context) {
        SharePersistent mSharePersistent = SharePersistent.getInstance();
        mSharePersistent.clear(context, "ACCESS_TOKEN");
        mSharePersistent.clear(context, "EXPIRES_IN");
        mSharePersistent.clear(context, "AUTHORIZETIME");
    }

    /**
     * 获取手机ip
     * 
     * @param context
     *            上下文
     * @return 可用的ip
     * 
     */
    public static String getLocalIPAddress(Context context) {
        // try {
        // for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
        // .getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
        // NetworkInterface intf = mEnumeration.nextElement();
        // for (Enumeration<InetAddress> enumIPAddr = intf
        // .getInetAddresses(); enumIPAddr.hasMoreElements();) {
        // InetAddress inetAddress = enumIPAddr.nextElement();
        // // 如果不是回环地址
        // if (!inetAddress.isLoopbackAddress()) {
        // // 直接返回本地IP地址
        // int i = Integer.parseInt(inetAddress.getHostAddress());
        // String ipStr=(i & 0xFF)+"."+((i>>8) & 0xFF)+"."+
        // ((i>>16) & 0xFF)+"."+(i>>24 & 0xFF);
        // return ipStr;
        // }
        // }
        // }
        // } catch (SocketException ex) {
        // Log.e("Error", ex.toString());
        // }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        int i = info.getIpAddress();
        String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        return ipStr;

        // return intToIp(ip);
        // return null;
    }

    /**
     * 判断当前网络是否可用
     * 
     * @param activity
     *            当前Acitivity对象
     * @return 可用返回true 否则返回false
     * 
     * */
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo info[] = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取手机经纬度
     * 
     * @param context
     *            上下文
     * @return 可用的location 可能为空
     * 
     */
    public static Location getLocation(Context context) {
        // LocationManager lm=(LocationManager)
        // context.getSystemService(Context.LOCATION_SERVICE);
        // Criteria criteria = new Criteria();
        // criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        // criteria.setAltitudeRequired(false);//不要求海拔
        // criteria.setBearingRequired(false);//不要求方位
        // criteria.setCostAllowed(true);//允许有花费
        // criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        // //从可用的位置提供器中，匹配以上标准的最佳提供器
        // String provider = lm.getBestProvider(criteria, true);
        // if (provider!=null) {
        // Location location=lm.getLastKnownLocation(provider);
        // return location;
        // }else {
        // return null;
        // }
        Location currentLocation = null;
        try {
            // 获取到LocationManager对象
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // 创建一个Criteria对象
            Criteria criteria = new Criteria();
            // 设置粗略精确度
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // 设置是否需要返回海拔信息
            criteria.setAltitudeRequired(false);
            // 设置是否需要返回方位信息
            criteria.setBearingRequired(false);
            // 设置是否允许付费服务
            criteria.setCostAllowed(true);
            // 设置电量消耗等级
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            // 设置是否需要返回速度信息
            criteria.setSpeedRequired(false);

            // 根据设置的Criteria对象，获取最符合此标准的provider对象
            String currentProvider = locationManager.getBestProvider(criteria, true);
            Log.d("Location", "currentProvider: " + currentProvider);
            // 根据当前provider对象获取最后一次位置信息
            currentLocation = locationManager.getLastKnownLocation(currentProvider);
        } catch (Exception e) {
            currentLocation = null;
        }
        return currentLocation;
    }

    @SuppressWarnings("deprecation")
    public void getTelephonyInfo(Context context) {
        String androidSDKVersion = android.os.Build.VERSION.SDK;
        int androidSDKVersionNumber = android.os.Build.VERSION.SDK_INT;

        System.out.println(androidSDKVersion + ", " + androidSDKVersionNumber);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        /*
         * 电话状态： 1.tm.CALL_STATE_IDLE=0 无活动 2.tm.CALL_STATE_RINGING=1 响铃
         * 3.tm.CALL_STATE_OFFHOOK=2 摘机
         */
        telephonyManager.getCallState();// int

        /*
         * 电话方位：
         */
        telephonyManager.getCellLocation();// CellLocation

        /*
         * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
         * available.
         */
        telephonyManager.getDeviceId();// String

        /*
         * 设备的软件版本号： 例如：the IMEI/SV(software version) for GSM phones. Return
         * null if the software version is not available.
         */
        telephonyManager.getDeviceSoftwareVersion();// String

        /*
         * 手机号： GSM手机的 MSISDN. Return null if it is unavailable.
         */
        telephonyManager.getLine1Number();// String

        /*
         * 附近的电话的信息: 类型：List<NeighboringCellInfo>
         * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
         */
        telephonyManager.getNeighboringCellInfo();// List<NeighboringCellInfo>

        /*
         * 获取ISO标准的国家码，即国际长途区号。 注意：仅当用户已在网络注册后有效。 在CDMA网络中结果也许不可靠。
         */
        telephonyManager.getNetworkCountryIso();// String

        /*
         * MCC+MNC(mobile country code + mobile network code) 注意：仅当用户已在网络注册时有效。
         * 在CDMA网络中结果也许不可靠。
         */
        telephonyManager.getNetworkOperator();// String

        /*
         * 按照字母次序的current registered operator(当前已注册的用户)的名字 注意：仅当用户已在网络注册时有效。
         * 在CDMA网络中结果也许不可靠。
         */
        telephonyManager.getNetworkOperatorName();// String

        /*
         * 当前使用的网络类型： 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0 NETWORK_TYPE_GPRS GPRS网络
         * 1 NETWORK_TYPE_EDGE EDGE网络 2 NETWORK_TYPE_UMTS UMTS网络 3
         * NETWORK_TYPE_HSDPA HSDPA网络 8 NETWORK_TYPE_HSUPA HSUPA网络 9
         * NETWORK_TYPE_HSPA HSPA网络 10 NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4
         * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5 NETWORK_TYPE_EVDO_A EVDO网络,
         * revision A. 6 NETWORK_TYPE_1xRTT 1xRTT网络 7
         */
        telephonyManager.getNetworkType();// int

        /*
         * 手机类型： 例如： PHONE_TYPE_NONE 无信号 PHONE_TYPE_GSM GSM信号 PHONE_TYPE_CDMA
         * CDMA信号
         */
        telephonyManager.getPhoneType();// int

        /*
         * Returns the ISO country code equivalent for the SIM provider's
         * country code. 获取ISO国家码，相当于提供SIM卡的国家码。
         */
        telephonyManager.getSimCountryIso();// String

        /*
         * Returns the MCC+MNC (mobile country code + mobile network code) of
         * the provider of the SIM. 5 or 6 decimal digits.
         * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字. SIM卡的状态必须是
         * SIM_STATE_READY(使用getSimState()判断).
         */
        telephonyManager.getSimOperator();// String

        /*
         * 服务商名称： 例如：中国移动、联通 SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
         */
        telephonyManager.getSimOperatorName();// String

        /*
         * SIM卡的序列号： 需要权限：READ_PHONE_STATE
         */
        telephonyManager.getSimSerialNumber();// String

        /*
         * SIM的状态信息： SIM_STATE_UNKNOWN 未知状态 0 SIM_STATE_ABSENT 没插卡 1
         * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2 SIM_STATE_PUK_REQUIRED
         * 锁定状态，需要用户的PUK码解锁 3 SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
         * SIM_STATE_READY 就绪状态 5
         */
        telephonyManager.getSimState();// int

        /*
         * 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone. 需要权限：READ_PHONE_STATE
         */
        telephonyManager.getSubscriberId();// String

        /*
         * 取得和语音邮件相关的标签，即为识别符 需要权限：READ_PHONE_STATE
         */
        telephonyManager.getVoiceMailAlphaTag();// String

        /*
         * 获取语音邮件号码： 需要权限：READ_PHONE_STATE
         */
        telephonyManager.getVoiceMailNumber();// String

        /*
         * ICC卡是否存在
         */
        telephonyManager.hasIccCard();// boolean

        /*
         * 是否漫游: (在GSM用途下)
         */
        telephonyManager.isNetworkRoaming();//
    }

    public static String intToIp(int i) {
        String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);

        return ipStr;
    }

    /**
     * @description 通过网络地址获得图片
     * @param imageUrl
     *            图片地址
     * @return drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable loadImageFromUrl(String imageUrl) {
        try {
            URL u = new URL(imageUrl);
            URLConnection conn = u.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inJustDecodeBounds = true;
            // int sampleSize = computeSampleSize(options, -1, 189*127);
            // options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;// 宽度和高度设置为原来的1/2
            // options.inJustDecodeBounds = false;
            // options.inSampleSize = 1;// 缩小图片，否则内存溢出
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            return new BitmapDrawable(bitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    /**
     * @des 得到config.properties配置文件中的所有配置
     * @return Properties对象
     */
    private static Properties getConfig() {
        Properties props = new Properties();
        InputStream in = Util.class.getResourceAsStream("/config/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("工具包异常", "获取配置文件异常");
            e.printStackTrace();
        }
        return props;
    }

    /**
     * 
     * @param propertyKey
     * @return
     */
    public static String getProperties(String propertyKey) {
        return getProperties(propertyKey, "");
    }

    /**
     * 
     * @param propertyKey
     * @param defaultValue
     * @return
     */
    public static String getProperties(String propertyKey, String defaultValue) {
        return getConfig().getProperty(propertyKey, defaultValue);
    }

    /**
     * 
     * @param propertyKey
     * @return
     */
    public static int getPropertiesInt(String propertyKey) {
        return getPropertiesInt(propertyKey, 0);
    }

    /**
     * 
     * @param propertyKey
     * @param defaultValue
     * @return
     */
    public static int getPropertiesInt(String propertyKey, int defaultValue) {
        String value = getConfig().getProperty(propertyKey,"").trim();
        if (null == value || "".equals(value)) {
            return defaultValue;
        } else {
            return Integer.getInteger(value, defaultValue).intValue();
        }
    }

    /**
     * 
     * @param propertyKey
     * @return
     */
    public static long getPropertiesLong(String propertyKey) {
        return getPropertiesLong(propertyKey, 0L);
    }

    /**
     * 
     * @param propertyKey
     * @param defaultValue
     * @return
     */
    public static long getPropertiesLong(String propertyKey, long defaultValue) {
        String value = getConfig().getProperty(propertyKey).trim();
        if (null == value || "".equals(value)) {
            return defaultValue;
        } else {
            return Long.getLong(value, defaultValue).longValue();
        }
    }

    /**
     * 判断授权是否过期
     * 
     * @param context
     * @return 授权过期则返回true，否则 返回false
     */
    public static boolean isAuthorizeExpired(Context context) {
        boolean expired = false;
        long authorizeTime = SharePersistent.getInstance().getLong(context, "AUTHORIZETIME");
        long expiresTime = SharePersistent.getInstance().getLong(context, "EXPIRES_IN");
        long currentTime = System.currentTimeMillis() / 1000;
        if ((Long.valueOf(authorizeTime) + Long.valueOf(expiresTime)) < currentTime) {
            expired = true;
        }
        return expired;
    }

}
