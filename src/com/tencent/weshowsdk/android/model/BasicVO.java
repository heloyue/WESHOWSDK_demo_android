/**
 * 
 */
package com.tencent.weshowsdk.android.model;

import java.io.Serializable;

/**
 * @author isaacxie
 *
 */
public abstract class BasicVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354024629811018923L;
	
	public static final int TYPE_JSON = 1;
	public static final int TYPE_XML = 2;
	
	public abstract String toString();

}
