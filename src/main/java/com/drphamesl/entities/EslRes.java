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
@NamedQuery(name = "EslRes.queryAll", query = "SELECT e FROM EslRes e WHERE (:active=0 OR e.active=1) ORDER BY e.dispPos")
public class EslRes extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eslResId;

	@NotNull
	@Column(length = 255)
	private String titleText;

	@Bind(fmt = Formatters.RELAXED_HTML)
	@NotNull
	@Column(length = 4000)
	private String descText;

	@NotNull
	private int dispPos;

	@BoolType
	private int active;

	@Override
	public Serializable getPk() {
		return eslResId;
	}

	public Integer getEslResId() {
		return eslResId;
	}

	public void setEslResId(Integer eslResId) {
		this.eslResId = eslResId;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getDescText() {
		return descText;
	}

	public void setDescText(String descText) {
		this.descText = descText;
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
