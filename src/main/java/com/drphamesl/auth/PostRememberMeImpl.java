package com.drphamesl.auth;

import com.appslandia.plum.base.PostRememberMe;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class PostRememberMeImpl implements PostRememberMe {

	@Override
	public void apply(HttpServletRequest request, HttpServletResponse response, String tokenIdentity) throws Exception {
	}
}
