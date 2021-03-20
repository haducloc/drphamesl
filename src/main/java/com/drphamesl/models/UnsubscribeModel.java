package com.drphamesl.models;

import javax.validation.constraints.NotNull;

import com.appslandia.common.base.Bind;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.validators.Email;
import com.appslandia.common.validators.ValidInts;
import com.drphamesl.utils.NewsTypes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class UnsubscribeModel {

	@Bind(fmt = Formatter.STRING_ELC)
	@NotNull
	@Email
	private String email;

	@NotNull
	private String sid;

	@ValidInts({ NewsTypes.NEWS_LETTER, NewsTypes.NEWS_BLOG, NewsTypes.NEWS_VOCAB })
	private int type;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
