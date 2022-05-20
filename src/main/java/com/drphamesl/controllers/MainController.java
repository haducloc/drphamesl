package com.drphamesl.controllers;

import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.Home;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.drphamesl.beans.MailService;
import com.drphamesl.services.ServiceCatService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

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
