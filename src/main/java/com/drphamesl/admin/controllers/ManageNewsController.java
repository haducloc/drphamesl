package com.drphamesl.admin.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.BitBool;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.JspTemplateUtils;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.entities.Signup;
import com.drphamesl.models.NewsletterModel;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.services.SignupService;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.NewsTypes;
import com.drphamesl.utils.Services;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-news", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageNewsController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ActionParser actionParser;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected MailMsgService mailMsgService;

	@Inject
	protected SignupService signupService;

	@HttpGetPost
	public ActionResult index(RequestAccessor request, HttpServletResponse response, @Model NewsletterModel model) throws Exception {
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
			List<Signup> signups = this.signupService.queryAll();

			Map<String, Object> params = new HashMap<>();
			int count = 0;

			for (Signup signup : signups) {
				if ((signup.getNewsMask() & NewsTypes.NEWS_LETTER) != NewsTypes.NEWS_LETTER) {
					continue;
				}

				params.put("email", signup.getEmail());
				params.put("sid", signup.getSid());
				params.put("type", NewsTypes.NEWS_LETTER);

				String unsubscribeUrl = actionParser.toActionUrl(request, "signup", "remove", params, true);
				model.setUnsubscribeUrl(unsubscribeUrl);

				String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/news_letter_email.jsp", model);

				// MailMsg
				MailMsg mailMsg = new MailMsg();
				mailMsg.setReplyToEmail(AppUtils.DRPHAM_ESL_EMAIL);
				mailMsg.setToEmail(signup.getEmail());

				mailMsg.setSubject(model.getSubject());
				mailMsg.setContent(htmlContent);
				mailMsg.setIsHtml(BitBool.TRUE);

				mailMsg.setMailerId(MailMsgs.MAILER_2);
				mailMsg.setPriority(MailMsgs.PRIORITY_3);
				mailMsg.setServiceId(Services.TYPE_SEND_NEWS);

				mailMsg.setClientId(request.getRequestContext().getClientId());
				mailMsg.setTimeCreated(System.currentTimeMillis());

				mailMsgService.add(mailMsg);

				count++;
			}

			request.getMessages().addNotice(request.res("manage_news_index.newsletter_sent_successfully", count));
			return new RedirectResult("index");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}
}
