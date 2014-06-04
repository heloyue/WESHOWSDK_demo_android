/**
 * 
 */
package com.tencent.weshowsdk.android.model;

import java.io.Serializable;

/**
 * @author isaacxie
 * 
 */
public class RequestResultModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1734321600445401363L;

	private int ret;

	private int errcode;

	private String msg;

	private String data;

	/**
	 * @return the ret
	 */
	public int getRet() {
		return ret;
	}

	/**
	 * @param ret
	 *            the ret to set
	 */
	public void setRet(int ret) {
		this.ret = ret;
	}

	/**
	 * @return the errcode
	 */
	public int getErrcode() {
		return errcode;
	}

	/**
	 * @param errcode
	 *            the errcode to set
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

}
