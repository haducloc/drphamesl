package com.drphamesl.admin.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.ToStringBuilder;
import com.appslandia.common.utils.MimeTypes;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.BasicRateLimitHandler;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.results.TextResult;
import com.appslandia.plum.utils.DebugUtils;
import com.drphamesl.utils.AccountUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class AdminController {

	@Inject
	protected BasicRateLimitHandler rateLimitHandler;

	@HttpGet
	public ActionResult index(RequestAccessor request) throws Exception {
		return JspResult.DEFAULT;
	}

	@HttpGet
	public ActionResult clearBannedClients(RequestAccessor request) throws Exception {
		this.rateLimitHandler.clearBannedClients();

		request.getMessages().addNotice("Done clearBanned.");
		return new RedirectResult("index");
	}

	@HttpGet
	public ActionResult bannedClients() throws Exception {
		String text = new ToStringBuilder(3).toString(this.rateLimitHandler.getBannedClients());
		return new TextResult(text, MimeTypes.TEXT_PLAIN);
	}

	@HttpGet
	public ActionResult clientTrackers() throws Exception {
		String text = new ToStringBuilder(3).toString(this.rateLimitHandler.getClientTrackers());
		return new TextResult(text, MimeTypes.TEXT_PLAIN);
	}

	@HttpGet
	public String hashpwd(RequestAccessor request, String password) throws Exception {
		if (password != null) {
			return AccountUtils.hashPassword(password);
		} else {
			return "No password provided.";
		}
	}

	@Inject
	protected BeanManager beanManager;

	@HttpGet
	public void tcpInfo(RequestAccessor request, HttpServletResponse response) throws Exception {
		DebugUtils.writeTcpInfo(request, response);
	}

	@HttpGet
	public void beanInfo(RequestAccessor request, HttpServletResponse response) throws Exception {
		DebugUtils.writeBeanInfo(this.beanManager, response);
	}

	@HttpGet
	public void servletInfo(RequestAccessor request, HttpServletResponse response) throws Exception {
		DebugUtils.writeServletInfo(request, response);
	}

	@HttpGet
	public void envsProps(RequestAccessor request, HttpServletResponse response) throws Exception {
		DebugUtils.writeEnvsProps(request, response);
	}

	@HttpGet
	public void zoneInfo(RequestAccessor request, HttpServletResponse response) throws Exception {
		DebugUtils.writeZoneInfo(request, response);
	}
}
