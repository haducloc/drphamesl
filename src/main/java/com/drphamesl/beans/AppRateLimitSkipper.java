package com.drphamesl.beans;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;

import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RateLimitSkipper;
import com.appslandia.plum.base.RequestContext;

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
