package com.drphamesl.entities;

import java.io.Serializable;
import java.util.List;

import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.models.SelectItem;
import com.appslandia.common.validators.BoolType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "ServiceCat.query", query = "SELECT e FROM ServiceCat e WHERE (:active=0 OR e.active=1) ORDER BY e.dispPos")
@NamedEntityGraph(name = "ServiceCatGraph.services", attributeNodes = @NamedAttributeNode("services"))

public class ServiceCat extends EntityBase implements SelectItem {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer serviceCatId;

	@NotNull
	@Column(length = 255)
	private String titleText;

	@NotNull
	private int dispPos;

	@BoolType
	private int active;

	@OneToMany(mappedBy = "serviceCat")
	@OrderBy("dispPos ASC")
	private List<Service> services;

	@Override
	public Serializable getPk() {
		return this.serviceCatId;
	}

	@Override
	public Object getValue() {
		return this.serviceCatId;
	}

	@Override
	public String getDisplayName() {
		return this.titleText;
	}

	public Integer getServiceCatId() {
		return serviceCatId;
	}

	public void setServiceCatId(Integer serviceCatId) {
		this.serviceCatId = serviceCatId;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
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

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
