package com.drphamesl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import com.appslandia.common.jpa.EntityBase;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Resource.queryAll", query = "SELECT e FROM Resource e")
public class Resource extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	private String resourceId;

	@Column(length = 8000)
	private String resourceText;

	@Override
	public Serializable getPk() {
		return this.resourceId;
	}

	public String getTruncatedText() {
		if (this.resourceText == null)
			return null;
		return (this.resourceText.length() <= 255) ? this.resourceText : this.resourceText.substring(0, 255);
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceText() {
		return resourceText;
	}

	public void setResourceText(String resourceText) {
		this.resourceText = resourceText;
	}
}
