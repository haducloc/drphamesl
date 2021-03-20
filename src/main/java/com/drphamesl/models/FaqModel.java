package com.drphamesl.models;

import java.util.List;

import com.drphamesl.entities.Faq;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FaqModel {

	private List<Faq> faqs;

	private String contactLink;

	public List<Faq> getFaqs() {
		return faqs;
	}

	public void setFaqs(List<Faq> faqs) {
		this.faqs = faqs;
	}

	public String getContactLink() {
		return contactLink;
	}

	public void setContactLink(String contactLink) {
		this.contactLink = contactLink;
	}
}
