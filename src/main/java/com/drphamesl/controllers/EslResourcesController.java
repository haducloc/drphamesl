package com.drphamesl.controllers;

import com.appslandia.common.base.Params;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.utils.ServletUtils;
import com.drphamesl.models.EslResourcesModel;
import com.drphamesl.services.EslResService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller("esl-resources")
public class EslResourcesController {

	@Inject
	protected EslResService eslResService;

	@Inject
	protected ActionParser actionParser;

	@HttpGet
	@EnableEtag
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {

		EslResourcesModel model = new EslResourcesModel();
		model.setEslRess(eslResService.getEslResources());

		// Links
		Params returnUrl = Params.of(ServletUtils.PARAM_RETURN_URL, ServletUtils.getUriQuery(request));

		model.setLoginLink(actionParser.toActionLink(request, "account", "login", returnUrl, Params.of("class", "lower"), request.res("label.login")));
		model.setSignupLink(actionParser.toActionLink(request, "account", "signup", returnUrl, Params.of("class", "lower"), request.res("label.signup")));

		request.storeModel(model);
		return JspResult.DEFAULT;
	}
}
