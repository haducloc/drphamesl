package com.drphamesl.models;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EmailResetPwdModel {

	private String subject;
	private String resetpwdUrl;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getResetpwdUrl() {
		return resetpwdUrl;
	}

	public void setResetpwdUrl(String resetpwdUrl) {
		this.resetpwdUrl = resetpwdUrl;
	}
}
