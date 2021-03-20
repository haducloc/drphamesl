package com.drphamesl.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.Home;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.drphamesl.beans.MailService;
import com.drphamesl.services.ServiceCatService;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller
@Home
public class MainController {

	@Inject
	protected MailService mailService;

	@Inject
	protected ServiceCatService serviceCatService;

	@PostConstruct
	protected void initialized() {
		mailService.toString();
	}

	@HttpGet
	@EnableEtag
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		request.store("serviceCats", serviceCatService.getServiceCats());
		return JspResult.DEFAULT;
	}
}
