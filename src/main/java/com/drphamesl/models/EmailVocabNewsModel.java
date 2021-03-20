package com.drphamesl.models;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EmailVocabNewsModel {

	private String subject;
	private String vocabListUrl;

	private String unsubscribeUrl;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getVocabListUrl() {
		return vocabListUrl;
	}

	public void setVocabListUrl(String vocabListUrl) {
		this.vocabListUrl = vocabListUrl;
	}

	public String getUnsubscribeUrl() {
		return unsubscribeUrl;
	}

	public void setUnsubscribeUrl(String unsubscribeUrl) {
		this.unsubscribeUrl = unsubscribeUrl;
	}
}
