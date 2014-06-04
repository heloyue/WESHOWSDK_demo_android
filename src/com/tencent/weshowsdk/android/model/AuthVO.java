/**
 * 
 */
package com.tencent.weshowsdk.android.model;


/**
 * @author isaacxie
 * 
 */
public class AuthVO extends BasicVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3393701011804748726L;

	/*
	 * "access_token":
	 * "TJ5ZM2Eh07uhk6Pri1g2nrGDQ467yzS8Y3wOwTrV6XR7u93OVio+XEAtF3FAXfHEiwcbdfAcIHPnYxSaY0hhTqZXGEOXaKs9KxfXoNZCMFOEbu9wJXk960Yzm34C3VVL"
	 * , "expires_in":1396955176
	 */
	private String accessToken;
	
	private long expiresIn;

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken
	 *            the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the expiresIn
	 */
	public long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn
	 *            the expiresIn to set
	 */
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	/* (non-Javadoc)
	 * @see com.tencent.weshowsdk.android.model.BasicVO#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
