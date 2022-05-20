package com.drphamesl.beans;

import java.util.concurrent.ScheduledExecutorService;

import com.appslandia.plum.base.BasicRateLimitHandler;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;

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
