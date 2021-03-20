package com.drphamesl.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.appslandia.common.jpa.EntityManagerAccessor;
import com.drphamesl.entities.Config;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class ConfigService {

	@Inject
	protected EntityManagerAccessor em;

	public Config findByPk(String pk) {
		return em.find(Config.class, pk);
	}

	public List<Config> queryAll() {
		return em.createNamedQuery("Config.queryAll", Config.class).asReadonly().getResultList();
	}

	@Transactional
	public void save(Config obj) throws Exception {

		Config managed = em.find(Config.class, obj.getPk());
		if (managed == null) {
			em.insert(obj);
		} else {
			managed.setConfigValue(obj.getConfigValue());
		}
	}
}
