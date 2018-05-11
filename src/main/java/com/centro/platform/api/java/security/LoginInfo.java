package com.centro.platform.api.java.security;

import javax.servlet.http.HttpServletRequest;

public class LoginInfo {

	private String username;
	private String password;
	private Object certificate;
	private transient HttpServletRequest request = null;
	
	public LoginInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Object getCertificate() {
		return certificate;
	}
	public void setCertificate(Object certificate) {
		this.certificate = certificate;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}	
}
