package com.drphamesl.admin.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Out;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.AuthContext;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.AuthParameters;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.FormLogin;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.models.LoginModel;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.ServletUtils;
import com.drphamesl.admin.auth.AdminCredential;
import com.drphamesl.entities.AdUser;
import com.drphamesl.services.AdUserService;
import com.drphamesl.utils.AccountUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(module = Modules.ADMIN)
public class AuthController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected AuthContext authContext;

	@Inject
	protected AdUserService userService;

	@HttpGetPost
	@FormLogin
	public ActionResult login(RequestAccessor request, HttpServletResponse response) throws Exception {
		LoginModel model = new LoginModel();

		// GET
		if (request.isGetOrHead()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		modelBinder.bindModel(request, model);

		// Module Authenticated?
		if (request.isModuleAuthenticated()) {
			model.setUserName(request.getRemoteUser());
			model.setRememberMe(request.getUserPrincipal().isRememberMe());

			request.getModelState().remove("userName", "rememberMe");
		}

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// AuthParameters
			AuthParameters authParameters = new AuthParameters().credential(new AdminCredential(model.getUserName(), model.getPassword()))
					.rememberMe(model.isRememberMe()).reauthentication(request.isModuleAuthenticated());

			// Logout
			if (request.getUserPrincipal() != null) {
				AdUser user = userService.findByName(model.getUserName());
				AssertUtils.assertNotNull(user);

				// Verify password
				if (!AccountUtils.verifyPassword(model.getPassword(), user.getPassword())) {
					request.getModelState().addError(request.res(getMsgKey(AuthFailureResult.PASSWORD_INVALID.getFailureCode())));

					request.storeModel(model);
					return JspResult.DEFAULT;
				}

				// LOGOUT
				request.logout();
			}

			// Authenticate
			Out<String> failureCode = new Out<>();
			if (!this.authContext.authenticate(request, response, authParameters, failureCode)) {
				request.getModelState().addError(request.res(getMsgKey(failureCode.val())));

				request.storeModel(model);
				return JspResult.DEFAULT;
			}

		} catch (Exception ex) {
			logger.error(ex);
			request.getModelState().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// returnUrl
		String returnUrl = request.getParamOrNull(ServletUtils.PARAM_RETURN_URL);
		if (returnUrl != null) {
			return new RedirectResult().location(returnUrl);
		} else {
			return new RedirectResult("index", "manage-post");
		}
	}

	@HttpGet
	public ActionResult logout(RequestAccessor request, HttpServletResponse response) throws Exception {
		if (request.getUserPrincipal() != null) {
			request.logout();
		}
		return new RedirectResult("index", "manage-post");
	}

	static String getMsgKey(String failureCode) {
		return "auth." + failureCode;
	}
}
