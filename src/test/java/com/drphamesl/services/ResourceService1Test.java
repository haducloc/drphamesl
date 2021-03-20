package com.drphamesl.services;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddEnabledInterceptors;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.appslandia.common.junit.cdi_transaction.MandatoryTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.NeverTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.NotSupportedTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.RequiredTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.RequiresNewTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.SupportsTestTxInterceptor;
import com.appslandia.common.junit.cdi_transaction.TestEmfControl;
import com.appslandia.common.junit.cdi_transaction.TestEntityManagerProducer;
import com.drphamesl.base.NotSharedEmfTestEmExtension;
import com.drphamesl.entities.Resource;

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
	ResourceService resourceService;

	@Test
	@Order(1)
	public void test1() {
		Resource res = new Resource();
		res.setResourceId("123456");
		res.setResourceText("Resource1");

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

		// Because NotSharedDB -> Size must be 0
		Assertions.assertEquals(0, size);
	}
}
