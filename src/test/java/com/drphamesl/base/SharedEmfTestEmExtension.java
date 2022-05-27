package com.drphamesl.base;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.appslandia.common.junit.jpa.SharedEmfTestEntityManagerExtension;

import jakarta.persistence.EntityManager;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SharedEmfTestEmExtension extends SharedEmfTestEntityManagerExtension {

	@Override
	protected String getPUName() {
		return "drphamesl";
	}

	@Override
	protected void initEach(ExtensionContext context, EntityManager txEm) {
	}
}
