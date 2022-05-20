package com.drphamesl.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.NotBind;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.common.validators.MaxLength;
import com.drphamesl.formatters.Formatters;
import com.drphamesl.utils.DefGroup;
import com.drphamesl.utils.VocabUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Vocab.queryCount", query = "SELECT COUNT(e) FROM Vocab e WHERE (:query IS NULL) OR (:query LIKE '#%' AND e.tags LIKE :wtag) OR (e.s_words LIKE :s_words)")
@NamedQuery(name = "Vocab.query", query = "SELECT e FROM Vocab e WHERE (:query IS NULL) OR (:query LIKE '#%' AND e.tags LIKE :wtag) OR (e.s_words LIKE :s_words) ORDER BY e.timeCreated DESC")

@NamedQuery(name = "Vocab.findByWords", query = "SELECT e FROM Vocab e WHERE e.s_words=:s_words")
@NamedQuery(name = "Vocab.checkTag", query = "SELECT 1 FROM Vocab e WHERE e.tags LIKE :wtag")
@NamedQuery(name = "Vocab.queryByTag", query = "SELECT e FROM Vocab e WHERE (e.tags LIKE :wtag) AND (e.tags NOT LIKE '%|#tbd|%') ORDER BY s_words")
@NamedQuery(name = "Vocab.queryDbTags", query = "SELECT e.tags FROM Vocab e")
public class Vocab extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vocabId;

	@Bind(fmt = Formatters.WORDS)
	@NotNull
	@MaxLength(75)
	@Column(length = 75)
	private String words;

	@NotBind
	@NotNull
	@MaxLength(75)
	@Column(length = 75, unique = true)
	private String s_words;

	@Bind(fmt = Formatter.DB_TAGS)
	@NotNull
	@MaxLength(155)
	@Column(length = 155)
	private String tags;

	@Bind(fmt = Formatters.DEFINITIONS)
	@NotNull
	@MaxLength(2500)
	@Column(length = 2500)
	private String definitions;

	@NotNull
	@Column(updatable = false)
	private OffsetDateTime timeCreated;

	@Transient
	private transient List<DefGroup> _defGroups;

	public Vocab() {
	}

	public Vocab(int vocabId) {
		this.vocabId = vocabId;
	}

	@Override
	public Serializable getPk() {
		return this.vocabId;
	}

	public String getImgQuery() {
		return VocabUtils.encodeWords(this.words);
	}

	public String[] getTagList() {
		return TagUtils.toTags(this.tags);
	}

	public List<String> getMarkedDefs() {
		return VocabUtils.getMarkedDefs(getDefGroups());
	}

	public String getDefs1Line() {
		return VocabUtils.toDefs1Line(getDefGroups());
	}

	public List<DefGroup> getDefGroups() {
		if (this._defGroups == null) {
			this._defGroups = VocabUtils.getDefGroups(this.definitions);
		}
		return this._defGroups;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || ((obj instanceof Vocab) == false)) {
			return false;
		}
		Vocab another = (Vocab) obj;
		return Objects.equals(this.vocabId, another.vocabId);
	}

	@Override
	public int hashCode() {
		int hash = 1, p = 31;
		hash = p * hash + Objects.hashCode(this.vocabId);
		return hash;
	}

	public Integer getVocabId() {
		return vocabId;
	}

	public void setVocabId(Integer vocabId) {
		this.vocabId = vocabId;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
		this.s_words = VocabUtils.nullOrLower(words);
	}

	public String getS_words() {
		return s_words;
	}

	public void setS_words(String s_words) {
		this.s_words = s_words;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	public OffsetDateTime getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(OffsetDateTime timeCreated) {
		this.timeCreated = timeCreated;
	}
}
