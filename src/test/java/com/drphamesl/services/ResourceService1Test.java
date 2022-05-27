package com.drphamesl.services;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddEnabledInterceptors;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.appslandia.common.junit.jpa.MandatoryTestTxInterceptor;
import com.appslandia.common.junit.jpa.NeverTestTxInterceptor;
import com.appslandia.common.junit.jpa.NotSupportedTestTxInterceptor;
import com.appslandia.common.junit.jpa.RequiredTestTxInterceptor;
import com.appslandia.common.junit.jpa.RequiresNewTestTxInterceptor;
import com.appslandia.common.junit.jpa.SupportsTestTxInterceptor;
import com.appslandia.common.junit.jpa.TestEmfControl;
import com.appslandia.common.junit.jpa.TestEntityManagerProducer;
import com.drphamesl.base.NotSharedEmfTestEmExtension;
import com.drphamesl.entities.Resource;

import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ExtendWith(NotSharedEmfTestEmExtension.class)
@EnableAutoWeld
@AddBeanClasses({ TestEntityManagerProducer.class, TestEmfControl.NotSharedEmf.class, ResourceService.class })
@AddEnabledInterceptors({ RequiredTestTxInterceptor.class, RequiresNewTestTxInterceptor.class, MandatoryTestTxInterceptor.class,
		SupportsTestTxInterceptor.class, NotSupportedTestTxInterceptor.class, NeverTestTxInterceptor.class })
public class ResourceService1Test {

	@Inject
	private ResourceService resourceService;

	@Test
	@Order(1)
	public void test1() {
		Resource res = new Resource();

		res.setResourceId("page_about.content");
		res.setResourceText("Some content");

		try {
			resourceService.save(res);

		} catch (Exception ex) {
			Assertions.fail(ex);
		}
	}

	@Test
	@Order(10)
	public void test2() {
		int size = resourceService.queryAll().size();

		// Because NotSharedEmfTestEmExtension -> Size must be 0
		Assertions.assertEquals(0, size);
	}
}
