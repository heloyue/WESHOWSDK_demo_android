/**
 * 
 */
package com.tencent.weshowsdk.android.model;

import java.io.Serializable;

/**
 * @author isaacxie
 * 
 */
public class ImageInfoBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6705683128711709049L;

    /**
     * url : 图片链接,
     */
    private String url;

    /**
     * pic_width : 图片长,
     */
    private int pic_width;

    /**
     * pic_height : 图片宽,
     */
    private int pic_height;

    /**
     * pic_type : 图片类型
     */
    private String pic_type;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the pic_width
     */
    public int getPic_width() {
        return pic_width;
    }

    /**
     * @param pic_width
     *            the pic_width to set
     */
    public void setPic_width(int pic_width) {
        this.pic_width = pic_width;
    }

    /**
     * @return the pic_height
     */
    public int getPic_height() {
        return pic_height;
    }

    /**
     * @param pic_height
     *            the pic_height to set
     */
    public void setPic_height(int pic_height) {
        this.pic_height = pic_height;
    }

    /**
     * @return the pic_type
     */
    public String getPic_type() {
        return pic_type;
    }

    /**
     * @param pic_type
     *            the pic_type to set
     */
    public void setPic_type(String pic_type) {
        this.pic_type = pic_type;
    }
}
