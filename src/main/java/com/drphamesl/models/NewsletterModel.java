package com.drphamesl.models;

import javax.validation.constraints.NotNull;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.NotBind;
import com.appslandia.common.formatters.Formatter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class NewsletterModel {

	@NotNull
	private String subject;

	@NotNull
	@Bind(fmt = Formatter.TEXT_ML)
	private String message;

	@NotBind
	private String unsubscribeUrl;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUnsubscribeUrl() {
		return unsubscribeUrl;
	}

	public void setUnsubscribeUrl(String unsubscribeUrl) {
		this.unsubscribeUrl = unsubscribeUrl;
	}
}
