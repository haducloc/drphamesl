package com.drphamesl.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.jsoup.Jsoup;

import com.appslandia.common.base.Bind;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.common.validators.BoolType;
import com.appslandia.common.validators.PathComponent;
import com.drphamesl.formatters.Formatters;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "BlogPost.query", query = "SELECT e FROM BlogPost e WHERE (:active=0 OR e.active=1) AND (:tag IS NULL OR e.tags LIKE :wtag) ORDER BY e.timeCreated DESC")
@NamedQuery(name = "BlogPost.queryCount", query = "SELECT COUNT(e) FROM BlogPost e WHERE (:active=0 OR e.active=1) AND (:tag IS NULL OR e.tags LIKE :wtag)")
@NamedQuery(name = "BlogPost.queryDbTags", query = "SELECT e.tags FROM BlogPost e")
public class BlogPost extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer blogPostId;

	@NotNull
	@Column(length = 255)
	private String titleText;

	@Bind(fmt = Formatters.RELAXED_HTML)
	@NotNull
	@Column(length = 8000)
	private String contentText;

	@Bind(fmt = Formatter.DB_TAGS)
	@NotNull
	@Column(length = 255)
	private String tags;

	@Bind(fmt = Formatter.KEYWORDS)
	@NotNull
	@Column(length = 255)
	private String keywords;

	@NotNull
	@Column(length = 160)
	private String descText;

	@Column(length = 255)
	private String imageUrl;

	@Column(length = 255)
	private String fbUrl;

	@NotNull
	private OffsetDateTime timeCreated;

	@BoolType
	private int active;

	@BoolType
	private int notified;

	@NotNull
	@PathComponent
	@Column(length = 255)
	private String title_path;

	@NotNull
	@Column(length = 512)
	private String stp_content;

	@Override
	public Serializable getPk() {
		return this.blogPostId;
	}

	public String[] getTagList() {
		return TagUtils.toTags(this.tags);
	}

	public String getDispTags() {
		return TagUtils.toDispTags(this.tags);
	}

	public Integer getBlogPostId() {
		return blogPostId;
	}

	public void setBlogPostId(Integer blogPostId) {
		this.blogPostId = blogPostId;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
		this.title_path = NormalizeUtils.unaccent(NormalizeUtils.normalizeLabel(titleText));
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;

		String ct = Jsoup.parse(contentText).text();
		this.stp_content = (ct.length() <= 512) ? ct : ct.substring(0, 512);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescText() {
		return descText;
	}

	public void setDescText(String descText) {
		this.descText = descText;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFbUrl() {
		return fbUrl;
	}

	public void setFbUrl(String fbUrl) {
		this.fbUrl = fbUrl;
	}

	public OffsetDateTime getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(OffsetDateTime timeCreated) {
		this.timeCreated = timeCreated;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getNotified() {
		return notified;
	}

	public void setNotified(int notified) {
		this.notified = notified;
	}

	public String getTitle_path() {
		return title_path;
	}

	public void setTitle_path(String title_path) {
		this.title_path = title_path;
	}

	public String getStp_content() {
		return stp_content;
	}

	public void setStp_content(String stp_content) {
		this.stp_content = stp_content;
	}
}
