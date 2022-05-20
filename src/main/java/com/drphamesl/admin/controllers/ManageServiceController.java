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
import com.drphamesl.entities.Service;
import com.drphamesl.services.ServiceCatService;
import com.drphamesl.services.ServiceService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-service", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageServiceController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected ServiceService serviceService;

	@Inject
	protected ServiceCatService serviceCatService;

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		request.store("services", serviceService.queryAll());
		return JspResult.DEFAULT;
	}

	void prepareEdit(RequestAccessor request) {
		request.store("serviceCats", serviceCatService.queryAll());
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, Integer serviceId) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			Service model = null;

			if (serviceId == null) {
				model = new Service();
				model.setActive(BitBool.TRUE);
			} else {
				model = this.serviceService.findByPk(serviceId);
				request.assertNotNull(model);
			}

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		// POST
		Service model = new Service();
		modelBinder.bindModel(request, model);

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		try {
			// Save
			this.serviceService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("service")));

			if (model.getServiceId() != null) {
				return new RedirectResult("index");
			} else {
				return new RedirectResult("edit");
			}

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}
	}
}
