package com.drphamesl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import com.appslandia.common.base.Bind;
import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.validators.BoolType;
import com.drphamesl.formatters.Formatters;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Faq.queryAll", query = "SELECT e FROM Faq e WHERE (:active=0 OR e.active=1) ORDER BY e.dispPos")
public class Faq extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer faqId;

	@NotNull
	@Column(length = 255)
	private String questText;

	@Bind(fmt = Formatters.RELAXED_HTML)
	@NotNull
	@Column(length = 4000)
	private String ansText;

	@NotNull
	private int dispPos;

	@BoolType
	private int active;

	@Override
	public Serializable getPk() {
		return this.faqId;
	}

	public Integer getFaqId() {
		return faqId;
	}

	public void setFaqId(Integer faqId) {
		this.faqId = faqId;
	}

	public String getQuestText() {
		return questText;
	}

	public void setQuestText(String questText) {
		this.questText = questText;
	}

	public String getAnsText() {
		return ansText;
	}

	public void setAnsText(String ansText) {
		this.ansText = ansText;
	}

	public int getDispPos() {
		return dispPos;
	}

	public void setDispPos(int dispPos) {
		this.dispPos = dispPos;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}
