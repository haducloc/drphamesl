package com.drphamesl.beans;

import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RateLimitSkipper;
import com.appslandia.plum.base.RequestContext;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class AppRateLimitSkipper implements RateLimitSkipper {

	@Override
	public boolean skipRequest(HttpServletRequest request, RequestContext requestContext) {

		return Modules.ADMIN.equalsIgnoreCase(requestContext.getModule());
	}
}
