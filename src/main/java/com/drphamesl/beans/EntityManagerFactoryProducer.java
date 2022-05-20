package com.drphamesl.beans;

import com.appslandia.common.jpa.EntityManagerFactoryImpl;
import com.drphamesl.utils.DBUtils;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Dependent
public class EntityManagerFactoryProducer {

	@PersistenceUnit(unitName = DBUtils.PU_NAME)
	private EntityManagerFactory emf;

	@Produces
	public EntityManagerFactoryImpl produce() {
		return new EntityManagerFactoryImpl(this.emf);
	}
}
