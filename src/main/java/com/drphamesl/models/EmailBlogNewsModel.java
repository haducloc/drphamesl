package com.drphamesl.models;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EmailBlogNewsModel {

	private String subject;
	private String blogPostUrl;

	private String unsubscribeUrl;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBlogPostUrl() {
		return blogPostUrl;
	}

	public void setBlogPostUrl(String blogPostUrl) {
		this.blogPostUrl = blogPostUrl;
	}

	public String getUnsubscribeUrl() {
		return unsubscribeUrl;
	}

	public void setUnsubscribeUrl(String unsubscribeUrl) {
		this.unsubscribeUrl = unsubscribeUrl;
	}
}
