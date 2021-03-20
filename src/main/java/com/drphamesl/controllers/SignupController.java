package com.drphamesl.controllers;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Out;
import com.appslandia.common.base.Params;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.BitBool;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.BadRequestException;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpPost;
import com.appslandia.plum.base.Message;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.base.Result;
import com.appslandia.plum.base.SeriesToken;
import com.appslandia.plum.base.VerifyService;
import com.appslandia.plum.models.EmailModel;
import com.appslandia.plum.models.EmailSeriesToken;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.JspTemplateUtils;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.models.EmailActivateSignupModel;
import com.drphamesl.models.UnsubscribeModel;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.services.SignupService;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.Services;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller
public class SignupController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ActionParser actionParser;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected SignupService signupService;

	@Inject
	protected VerifyService verifyService;

	@Inject
	protected MailMsgService mailMsgService;

	@HttpPost
	public Result index(RequestAccessor request, HttpServletResponse response, @Model EmailModel model) throws Exception {
		// INVALID?
		if (!request.getModelState().isValid()) {

			Message error = request.getModelState().findError("email");
			throw new BadRequestException(error.getText());
		}

		// Already SignUp?
		if (signupService.checkEmail(model.getEmail())) {
			return new Result().setMessage(request.res("signup_index.signup_existed_already"));
		}

		// Activate Email
		EmailActivateSignupModel activateModel = new EmailActivateSignupModel();
		activateModel.setSubject(request.res("signup_index.signup_email_subject"));

		// activateUrl
		final SeriesToken seriesToken = verifyService.saveToken(model.getEmail(), null, TimeUnit.MILLISECONDS.convert(360, TimeUnit.DAYS));
		Params params = new Params().set("series", seriesToken.getSeries()).set("token", seriesToken.getToken()).set("email", model.getEmail());

		String activateUrl = actionParser.toActionUrl(request, "signup", "activate", params, true);
		activateModel.setActivateUrl(activateUrl);

		String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/signup_news_email.jsp", activateModel);

		// MailMsg
		MailMsg mailMsg = new MailMsg();
		mailMsg.setReplyToEmail(AppUtils.DRPHAM_ESL_EMAIL);
		mailMsg.setToEmail(model.getEmail());

		mailMsg.setSubject(activateModel.getSubject());
		mailMsg.setContent(htmlContent);
		mailMsg.setIsHtml(BitBool.TRUE);

		mailMsg.setMailerId(MailMsgs.MAILER_1);
		mailMsg.setPriority(MailMsgs.PRIORITY_2);
		mailMsg.setServiceId(Services.TYPE_SIGNUP_NEWS);

		mailMsg.setClientId(request.getRequestContext().getClientId());
		mailMsg.setTimeCreated(System.currentTimeMillis());

		mailMsgService.add(mailMsg);
		return new Result().setMessage(request.res("signup_index.signup_news_successfully"));
	}

	@HttpGet
	public ActionResult activate(RequestAccessor request, HttpServletResponse response, @Model EmailSeriesToken model) throws Exception {
		request.assertValidModel();

		try {
			// Verify Token
			Out<String> failureCode = new Out<String>();
			if (verifyService.verifyToken(model.getSeries(), model.getToken(), model.getEmail(), null, 0, failureCode)) {

				this.signupService.activate(model.getEmail());
				verifyService.removeToken(model.getSeries());

				request.getMessages().addNotice(request.res("signup_activate.signup_activated_successfully"));
				return new RedirectResult("index", "main");
			}

			// Invalid?
			request.getMessages().addError(request.res("signup_activate.signup_activate_invalid"));
			return new RedirectResult("index", "main");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			return new RedirectResult("index", "main");
		}
	}

	@HttpGet
	public ActionResult remove(RequestAccessor request, HttpServletResponse response, @Model UnsubscribeModel model) throws Exception {
		request.assertValidModel();

		this.signupService.remove(model.getEmail(), model.getSid(), model.getType());

		request.getMessages().addNotice(request.res("signup_remove.signup_removed_successfully"));
		return new RedirectResult("index", "main");
	}
}
