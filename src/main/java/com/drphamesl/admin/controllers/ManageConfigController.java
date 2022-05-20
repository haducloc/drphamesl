package com.drphamesl.admin.controllers;

import com.appslandia.common.logging.AppLogger;
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
import com.drphamesl.entities.Config;
import com.drphamesl.services.ConfigService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-config", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageConfigController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected ConfigService configService;

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response) throws Exception {
		request.store("configs", configService.queryAll());
		return JspResult.DEFAULT;
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, String configId) throws Exception {
		request.assertNotNull(configId);

		// GET
		if (request.isGetOrHead()) {
			Config model = this.configService.findByPk(configId);
			if (model == null) {
				model = new Config();
				model.setConfigId(configId);
			}

			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		Config model = new Config();
		modelBinder.bindModel(request, model);

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// Save
			this.configService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("config")));

			return new RedirectResult("edit").query("configId", model.getPk());

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}
}
