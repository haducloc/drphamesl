package com.drphamesl.beans;

import com.appslandia.common.cdi.CDIFactory;
import com.appslandia.common.logging.AppLoggerManager;
import com.appslandia.common.logging.Log4JAppLoggerManager;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class AppLoggerManagerImpl implements CDIFactory<AppLoggerManager> {

	@Produces
	@Alternative
	@ApplicationScoped
	@Override
	public AppLoggerManager produce() {
		return new Log4JAppLoggerManager();
	}

	@Override
	public void dispose(@Disposes AppLoggerManager impl) {
		impl.close();
	}
}
