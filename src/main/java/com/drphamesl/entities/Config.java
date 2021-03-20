package com.drphamesl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.appslandia.common.jpa.EntityBase;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@Table(name = "DbConfig")
@NamedQuery(name = "Config.queryAll", query = "SELECT e FROM Config e ORDER BY e.configId")
public class Config extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	private String configId;

	@Column(length = 255)
	private String configValue;

	@Override
	public Serializable getPk() {
		return this.configId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
}
