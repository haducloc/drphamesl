package com.drphamesl.entities;

import java.io.Serializable;

import com.appslandia.common.base.Bind;
import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.validators.BoolType;
import com.drphamesl.formatters.Formatters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Service.queryAll", query = "SELECT e FROM Service e ORDER BY e.serviceCat.dispPos, e.serviceCatId, e.dispPos")
public class Service extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer serviceId;

	@NotNull
	@Column(length = 255)
	private String titleText;

	@Bind(fmt = Formatters.RELAXED_HTML)
	@NotNull
	@Column(length = 8000)
	private String descText;

	@Bind(fmt = Formatters.RELAXED_HTML)
	@NotNull
	@Column(length = 2000)
	private String tuitionText;

	@NotNull
	private Integer serviceCatId;

	@NotNull
	private int dispPos;

	@BoolType
	private int active;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "serviceCatId", insertable = false, updatable = false)
	private ServiceCat serviceCat;

	@Override
	public Serializable getPk() {
		return this.serviceId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public String getTuitionText() {
		return tuitionText;
	}

	public void setTuitionText(String tuitionText) {
		this.tuitionText = tuitionText;
	}

	public Integer getServiceCatId() {
		return serviceCatId;
	}

	public void setServiceCatId(Integer serviceCatId) {
		this.serviceCatId = serviceCatId;
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

	public ServiceCat getServiceCat() {
		return serviceCat;
	}

	public void setServiceCat(ServiceCat serviceCat) {
		this.serviceCat = serviceCat;
	}
}
