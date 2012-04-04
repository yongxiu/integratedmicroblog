package cn.edu.nju.software.bean;

import java.io.Serializable;

import cn.edu.nju.software.utils.MicroBlogType;
import android.graphics.drawable.Drawable;

public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1792222905630107850L;
	
	public static final String ID = "_id";
	public static final String MBTYPE = "mbType";
	public static final String USERID = "userId";
	public static final String TOKEN = "token";
	public static final String TOKENSECRET = "tokenSecret";
	public static final String USERNAME = "userName";
	public static final String USERICON = "userIcon";
	
	private MicroBlogType mbType;
	private String id;
	private String userId;// 用户id
	private String token;
	private String tokenSecret;
	private String userName;
	private Drawable userIcon;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Drawable getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(Drawable userIcon) {
		this.userIcon = userIcon;
	}

	public void setMbType(MicroBlogType mbType) {
		this.mbType = mbType;
	}

	public MicroBlogType getMbType() {
		return mbType;
	}
}