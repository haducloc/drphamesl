package com.drphamesl.beans;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.jpa.HintMapper;
import com.appslandia.common.jpa.JpaHints;
import com.drphamesl.utils.DBUtils;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Dependent
public class EntityManagerProducer {

	static {
		try {
			final HintMapper mapper = new HintMapper();
			mapper.addHint(JpaHints.HINT_QUERY_READONLY, "org.hibernate.readOnly");

			JpaHints.setHintMapper(mapper);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@PersistenceContext(unitName = DBUtils.PU_NAME)
	private EntityManager em;

	@Produces
	public EntityManagerImpl produce() {
		return new EntityManagerImpl(this.em);
	}
}
