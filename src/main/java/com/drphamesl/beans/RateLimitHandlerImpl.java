package com.drphamesl.beans;

import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

import com.appslandia.plum.base.BasicRateLimitHandler;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class RateLimitHandlerImpl extends BasicRateLimitHandler {

	@Resource
	protected ManagedScheduledExecutorService executor;

	@Override
	protected ScheduledExecutorService getScheduledExecutor() {
		return this.executor;
	}
}
