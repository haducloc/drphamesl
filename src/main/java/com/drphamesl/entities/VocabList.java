package com.drphamesl.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import com.appslandia.common.base.Bind;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.common.validators.BoolType;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.common.validators.PathComponent;
import com.appslandia.common.validators.ValidInts;
import com.drphamesl.utils.ShareTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "VocabList.query", query = "SELECT e FROM VocabList e WHERE (:shareType IS NULL OR e.shareType=:shareType) AND (:tag IS NULL OR e.tag=:tag) ORDER BY e.timeCreated DESC")
@NamedQuery(name = "VocabList.queryCount", query = "SELECT COUNT(e) FROM VocabList e WHERE (:shareType IS NULL OR e.shareType=:shareType) AND (:tag IS NULL OR e.tag=:tag)")
@NamedQuery(name = "VocabList.queryList", query = "SELECT e FROM VocabList e WHERE e.shareType > 0")
public class VocabList extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vocabListId;

	@Bind(fmt = Formatter.TAG)
	@NotNull
	@MaxLength(45)
	@Column(length = 45, unique = true, updatable = false)
	private String tag;

	@NotNull
	@Column(length = 50)
	private String tag_likeval;

	@NotNull
	@MaxLength(255)
	@Column(length = 255)
	private String titleText;

	@NotNull
	@PathComponent
	@Column(length = 255)
	private String title_path;

	@NotNull
	@MaxLength(160)
	@Column(length = 160)
	private String descText;

	@Bind(fmt = Formatter.KEYWORDS)
	@NotNull
	@MaxLength(255)
	@Column(length = 255)
	private String keywords;

	@Column(length = 255)
	private String imageUrl;

	private int dispPos;

	@ValidInts({ ShareTypes.OFF, ShareTypes.ON_PRIVATE, ShareTypes.ON_PUBLIC })
	private int shareType;

	@BoolType
	private int notified;

	@NotNull
	@Column(length = 16, unique = true, updatable = false)
	private String spk;

	@Column(updatable = false, nullable = false)
	private OffsetDateTime timeCreated;

	@Override
	public Serializable getPk() {
		return this.vocabListId;
	}

	public boolean isNew() {
		LocalDate ld = DateUtils.atUTC(this.timeCreated).toLocalDate();
		return ChronoUnit.DAYS.between(ld, DateUtils.nowAtUTC().toLocalDate()) <= 30;
	}

	public long getTimeCreatedMs() {
		return this.timeCreated.toInstant().toEpochMilli();
	}

	public String getIdOrSpk() {
		return (this.shareType == ShareTypes.ON_PUBLIC) ? AssertUtils.assertNotNull(vocabListId).toString() : spk;
	}

	public Integer getVocabListId() {
		return vocabListId;
	}

	public void setVocabListId(Integer vocabListId) {
		this.vocabListId = vocabListId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
		if (tag != null) {
			this.tag_likeval = TagUtils.toTagLikeVal(tag);
		}
	}

	public String getTag_likeval() {
		return tag_likeval;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
		this.title_path = NormalizeUtils.unaccent(NormalizeUtils.normalizeLabel(titleText));
	}

	public String getTitle_path() {
		return title_path;
	}

	public void setTitle_path(String title_path) {
		this.title_path = title_path;
	}

	public String getDescText() {
		return descText;
	}

	public void setDescText(String descText) {
		this.descText = descText;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getDispPos() {
		return dispPos;
	}

	public void setDispPos(int dispPos) {
		this.dispPos = dispPos;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public int getNotified() {
		return notified;
	}

	public void setNotified(int notified) {
		this.notified = notified;
	}

	public String getSpk() {
		return spk;
	}

	public void setSpk(String spk) {
		this.spk = spk;
	}

	public OffsetDateTime getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(OffsetDateTime timeCreated) {
		this.timeCreated = timeCreated;
	}
}
