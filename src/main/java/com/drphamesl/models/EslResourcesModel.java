package com.drphamesl.models;

import java.util.List;

import com.drphamesl.entities.EslRes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EslResourcesModel {

	private List<EslRes> eslRess;

	private String loginLink;
	private String signupLink;

	public List<EslRes> getEslRess() {
		return eslRess;
	}

	public void setEslRess(List<EslRes> eslRess) {
		this.eslRess = eslRess;
	}

	public String getLoginLink() {
		return loginLink;
	}

	public void setLoginLink(String loginLink) {
		this.loginLink = loginLink;
	}

	public String getSignupLink() {
		return signupLink;
	}

	public void setSignupLink(String signupLink) {
		this.signupLink = signupLink;
	}
}
