package com.drphamesl.services;

import java.util.List;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.ModelUtils;
import com.drphamesl.beans.ResourceChange;
import com.drphamesl.entities.Resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class ResourceService {

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected Event<ResourceChange> event;

	public Resource findByPk(String pk) {
		return em.find(Resource.class, pk);
	}

	public List<Resource> queryAll() {
		return em.createNamedQuery("Resource.queryAll", Resource.class).asReadonly().getResultList();
	}

	@Transactional
	public void save(Resource obj) throws Exception {
		Resource managed = em.find(Resource.class, obj.getPk());

		if (managed == null) {
			em.insert(obj);
		} else {
			ModelUtils.copy(managed, obj, "resourceText");
		}

		// Notify change
		event.fireAsync(new ResourceChange());
	}
}
