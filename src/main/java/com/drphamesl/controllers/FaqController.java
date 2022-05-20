package com.drphamesl.controllers;

import com.appslandia.common.base.Params;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.drphamesl.models.FaqModel;
import com.drphamesl.services.FaqService;

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
public class FaqController {

	@Inject
	protected FaqService faqService;

	@Inject
	protected ActionParser actionParser;

	@HttpGet
	@EnableEtag
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		FaqModel model = new FaqModel();
		model.setFaqs(faqService.getFaqs());

		model.setContactLink(actionParser.toActionLink(request, "dr-pham", "contact", null, Params.of("class", "lower"), request.res("label.contact")));

		request.storeModel(model);
		return JspResult.DEFAULT;
	}
}
