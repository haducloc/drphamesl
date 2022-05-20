package com.drphamesl.admin.controllers;

import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.BitBool;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.drphamesl.entities.ServiceCat;
import com.drphamesl.services.ServiceCatService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-servicecat", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageServiceCatController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected ServiceCatService serviceCatService;

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		request.store("serviceCats", serviceCatService.queryAll());
		return JspResult.DEFAULT;
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, Integer serviceCatId) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			ServiceCat model = null;

			if (serviceCatId == null) {
				model = new ServiceCat();
				model.setActive(BitBool.TRUE);
			} else {
				model = this.serviceCatService.findByPk(serviceCatId);
				request.assertNotNull(model);
			}

			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		ServiceCat model = new ServiceCat();
		modelBinder.bindModel(request, model);

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// Save
			this.serviceCatService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("serviceCat")));

			if (model.getServiceCatId() != null) {
				return new RedirectResult("index");
			} else {
				return new RedirectResult("edit");
			}

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}
}
