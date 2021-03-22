package com.drphamesl.controllers;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Out;
import com.appslandia.common.base.Params;
import com.appslandia.common.base.UUIDGenerator;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.AuthContext;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.AuthParameters;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.FormLogin;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.base.SeriesToken;
import com.appslandia.plum.base.VerifyService;
import com.appslandia.plum.models.EmailLoginModel;
import com.appslandia.plum.models.EmailModel;
import com.appslandia.plum.models.EmailSeriesToken;
import com.appslandia.plum.models.NewPasswordModel;
import com.appslandia.plum.models.ResetPasswordModel;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.JspTemplateUtils;
import com.appslandia.plum.utils.ServletUtils;
import com.drphamesl.auth.AccountCredential;
import com.drphamesl.entities.Account;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.models.EmailActivateAccountModel;
import com.drphamesl.models.EmailResetPwdModel;
import com.drphamesl.models.RegistrationModel;
import com.drphamesl.services.AccountService;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.utils.AccountUtils;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.Services;
import com.drphamesl.utils.TimeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller
public class AccountController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ActionParser actionParser;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected AuthContext authContext;

	@Inject
	protected MailMsgService mailMsgService;

	@Inject
	protected VerifyService verifyService;

	@Inject
	protected AccountService accountService;

	@EnableEtag
	@HttpGetPost
	public ActionResult signup(RequestAccessor request, HttpServletResponse response) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			RegistrationModel model = new RegistrationModel();

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
		// POST
		RegistrationModel model = new RegistrationModel();
		modelBinder.bindModel(request, model);

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// Check email
		if (accountService.findByEmail(model.getEmail()) != null) {
			request.getModelState().addError("email", request.res("account_signup.email_registered_already"));
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// New Account
			Account account = new Account();

			ModelUtils.copyProps(account, model, "firstName", "lastName", "email");
			account.setPassword(AccountUtils.hashPassword(model.getPassword()));

			account.setIdProvider(AccountUtils.ID_PROVIDER_APP);
			account.setStatus(AccountUtils.ACCOUNT_PENDING);
			account.setSid(UUIDGenerator.INSTANCE.generate());
			account.setTimeCreated(TimeUtils.nowAtGMT7());

			accountService.save(account);

			// EMAIL
			EmailActivateAccountModel activateModel = new EmailActivateAccountModel();
			activateModel.setSubject(request.res("account_signup.signup_email_subject"));

			// activateUrl
			final SeriesToken seriesToken = verifyService.saveToken(model.getEmail(), null, TimeUnit.MILLISECONDS.convert(360, TimeUnit.DAYS));
			Params params = new Params().set("series", seriesToken.getSeries()).set("token", seriesToken.getToken()).set("email", model.getEmail());
			String activateUrl = actionParser.toActionUrl(request, "account", "activate", params, true);

			activateModel.setActivateUrl(activateUrl);
			String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/signup_account_email.jsp", activateModel);

			// MailMsg
			MailMsg mailMsg = new MailMsg();
			mailMsg.setReplyToEmail(AppUtils.DRPHAM_ESL_EMAIL);
			mailMsg.setToEmail(model.getEmail());

			mailMsg.setSubject(activateModel.getSubject());
			mailMsg.setContent(htmlContent);
			mailMsg.setIsHtml(BitBool.TRUE);

			mailMsg.setMailerId(MailMsgs.MAILER_1);
			mailMsg.setPriority(MailMsgs.PRIORITY_1);
			mailMsg.setServiceId(Services.TYPE_SIGNUP_ACCOUNT);

			mailMsg.setClientId(request.getRequestContext().getClientId());
			mailMsg.setTimeCreated(System.currentTimeMillis());

			mailMsgService.add(mailMsg);

			request.getMessages().addNotice(request.res("account_signup.account_created_successfully"));

			// returnUrl
			String returnUrl = request.getParamOrNull(ServletUtils.PARAM_RETURN_URL);
			if (returnUrl != null) {
				return new RedirectResult().location(returnUrl);
			} else {
				return new RedirectResult("profile");
			}

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGet
	public ActionResult activate(RequestAccessor request, HttpServletResponse response, @Model EmailSeriesToken model) throws Exception {
		request.assertValidModel();

		try {
			// Verify Token
			Out<String> failureCode = new Out<String>();
			if (verifyService.verifyToken(model.getSeries(), model.getToken(), model.getEmail(), null, 0, failureCode)) {

				accountService.activate(model.getEmail());
				verifyService.removeToken(model.getSeries());

				request.getMessages().addNotice(request.res("account_activate.account_activated_successfully"));
				return new RedirectResult("profile");
			}

			// Invalid?
			request.getMessages().addError(request.res("account_activate.activate_invalid"));
			return new RedirectResult("activation");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			return new RedirectResult("profile");
		}
	}

	@HttpGetPost
	@Authorize
	public ActionResult profile(RequestAccessor request, HttpServletResponse response) throws Exception {
		Account currentAccount = accountService.findByPk(request.getUserId());

		// GET
		if (request.isGetOrHead()) {
			request.storeModel(currentAccount);
			return JspResult.DEFAULT;
		}

		// POST
		Account model = new Account();
		modelBinder.bindModel(request, model);
		request.getModelState().remove("accountId", "email", "password", "idProvider", "sid", "status", "timeCreated");

		if (!request.getModelState().isValid()) {
			model.setEmail(currentAccount.getEmail());
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			ModelUtils.copyProps(currentAccount, model, "firstName", "lastName");
			accountService.save(currentAccount);

			request.getMessages().addNotice(request.res("account_profile.profile_saved_successfully"));
			return new RedirectResult("profile");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGetPost
	@Authorize
	public ActionResult changepwd(RequestAccessor request, HttpServletResponse response, @Model NewPasswordModel model) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			request.getModelState().clearErrors();

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
		// POST
		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}
		try {
			accountService.changePassword(request.getUserId(), AccountUtils.hashPassword(model.getNewPassword()));

			request.getMessages().addNotice(request.res("account_changepwd.password_changed_successfully"));
			return new RedirectResult("changepwd");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@EnableEtag
	@HttpGetPost
	public ActionResult forgotpwd(RequestAccessor request, HttpServletResponse response, @Model EmailModel model) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			request.getModelState().clearErrors();

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
		// POST
		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		Account account = accountService.findByEmail(model.getEmail());
		if (account == null) {
			request.getModelState().addError("email", request.res("account_forgotpwd.email_not_registered"));
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// EMAIL
			EmailResetPwdModel resetPwdModel = new EmailResetPwdModel();
			resetPwdModel.setSubject(request.res("account_forgotpwd.resetpwd_email_subject"));

			// createPwdUrl
			final SeriesToken seriesToken = verifyService.saveToken(model.getEmail(), null, TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS));
			Params params = new Params().set("series", seriesToken.getSeries()).set("token", seriesToken.getToken()).set("email", model.getEmail());

			String resetpwdUrl = actionParser.toActionUrl(request, "account", "resetpwd", params, true);
			resetPwdModel.setResetpwdUrl(resetpwdUrl);

			String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/reset_password_email.jsp", resetPwdModel);

			// MailMsg
			MailMsg mailMsg = new MailMsg();
			mailMsg.setReplyToEmail(AppUtils.DRPHAM_ESL_EMAIL);
			mailMsg.setToEmail(model.getEmail());

			mailMsg.setSubject(resetPwdModel.getSubject());
			mailMsg.setContent(htmlContent);
			mailMsg.setIsHtml(BitBool.TRUE);

			mailMsg.setMailerId(MailMsgs.MAILER_1);
			mailMsg.setPriority(MailMsgs.PRIORITY_1);
			mailMsg.setServiceId(Services.TYPE_RESET_PWD);

			mailMsg.setClientId(request.getRequestContext().getClientId());
			mailMsg.setTimeCreated(System.currentTimeMillis());

			mailMsgService.add(mailMsg);

			request.getMessages().addNotice(request.res("account_forgotpwd.resetpwd_sent_successfully"));
			return new RedirectResult("forgotpwd");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGetPost
	public ActionResult activation(RequestAccessor request, HttpServletResponse response, @Model EmailModel model) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			request.getModelState().clearErrors();

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
		// POST
		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		Account account = accountService.findByEmail(model.getEmail());
		if (account == null) {
			request.getModelState().addError("email", request.res("account_activation.email_not_registered"));
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// EMAIL
			EmailActivateAccountModel activateModel = new EmailActivateAccountModel();
			activateModel.setSubject(request.res("account_signup.signup_email_subject"));

			// activateUrl
			final SeriesToken seriesToken = verifyService.saveToken(model.getEmail(), null, TimeUnit.MILLISECONDS.convert(360, TimeUnit.DAYS));
			Params params = new Params().set("series", seriesToken.getSeries()).set("token", seriesToken.getToken()).set("email", model.getEmail());
			String activateUrl = actionParser.toActionUrl(request, "account", "activate", params, true);

			activateModel.setActivateUrl(activateUrl);
			String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/account_activation_email.jsp", activateModel);

			// MailMsg
			MailMsg mailMsg = new MailMsg();
			mailMsg.setReplyToEmail(AppUtils.DRPHAM_ESL_EMAIL);
			mailMsg.setToEmail(model.getEmail());

			mailMsg.setSubject(activateModel.getSubject());
			mailMsg.setContent(htmlContent);
			mailMsg.setIsHtml(BitBool.TRUE);

			mailMsg.setMailerId(MailMsgs.MAILER_1);
			mailMsg.setPriority(MailMsgs.PRIORITY_1);
			mailMsg.setServiceId(Services.TYPE_ACCOUNT_ACT);

			mailMsg.setClientId(request.getRequestContext().getClientId());
			mailMsg.setTimeCreated(System.currentTimeMillis());

			mailMsgService.add(mailMsg);

			request.getMessages().addNotice(request.res("account_activation.activation_sent_successfully"));
			return new RedirectResult("activation");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGetPost
	public ActionResult resetpwd(RequestAccessor request, HttpServletResponse response, @Model ResetPasswordModel model) throws Exception {
		request.assertValidFields("series", "token", "email");

		// Verify Token
		Out<String> failureCode = new Out<String>();
		if (!verifyService.verifyToken(model.getSeries(), model.getToken(), model.getEmail(), null, 0, failureCode)) {

			request.getMessages().addError(request.res("account_resetpwd.resetpwd_invalid"));
			return new RedirectResult("forgotpwd");
		}

		// GET
		if (request.isGetOrHead()) {
			request.getModelState().clearErrors();
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		request.getModelState().remove("confirmPassword");

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// Reset Password
			accountService.resetPassword(model.getEmail(), AccountUtils.hashPassword(model.getNewPassword()));
			verifyService.removeToken(model.getSeries());

			request.getMessages().addNotice(request.res("account_resetpwd.password_reset_successfully"));
			return new RedirectResult("profile");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGetPost
	@FormLogin
	public ActionResult login(RequestAccessor request, HttpServletResponse response) throws Exception {
		EmailLoginModel model = new EmailLoginModel();

		// GET
		if (request.isGetOrHead()) {
			model.setRememberMe(true);

			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		modelBinder.bindModel(request, model);

		// Module Authenticated?
		if (request.isModuleAuthenticated()) {
			model.setEmail(request.getRemoteUser());
			model.setRememberMe(request.getUserPrincipal().isRememberMe());

			request.getModelState().remove("email", "rememberMe");
		}

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// AuthParameters
			AuthParameters authParameters = new AuthParameters().credential(new AccountCredential(model.getEmail(), model.getPassword()))
					.rememberMe(model.isRememberMe()).reauthentication(request.isModuleAuthenticated());

			// Logout
			if (request.getUserPrincipal() != null) {
				Account account = accountService.findByEmail(model.getEmail());
				AssertUtils.assertNotNull(account);

				// Verify password
				if (!AccountUtils.verifyPassword(model.getPassword(), account.getPassword())) {
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

				// Not activated?
				if (AuthFailureResult.CREDENTIAL_NOT_ACTIVATED.getFailureCode().equals(failureCode.val())) {

					String activateLink = actionParser.toActionLink(request, "account", "activation", Params.of("email", model.getEmail()),
							Params.of("class", "lower"), request.res("label.activate"));
					String escMsg = request.getRequestContext().fmtEscCt(getMsgKey(AuthFailureResult.CREDENTIAL_NOT_ACTIVATED.getFailureCode()), activateLink);

					request.getModelState().addError(escMsg, false);
				} else {
					request.getModelState().addError(request.res(getMsgKey(failureCode.val())));
				}
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
			return new RedirectResult("index", "vocab");
		}
	}

	@HttpGet
	public ActionResult logout(RequestAccessor request, HttpServletResponse response) throws Exception {
		if (request.getUserPrincipal() != null) {
			request.logout();
		}
		return new RedirectResult("index", "vocab");
	}

	static String getMsgKey(String failureCode) {
		return "account." + failureCode;
	}
}
