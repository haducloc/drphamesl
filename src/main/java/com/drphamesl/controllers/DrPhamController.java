package com.drphamesl.controllers;

import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.EmailUtils;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.models.ContactUsModel;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller("dr-pham")
public class DrPhamController {

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected MailMsgService mailMsgService;

	@HttpGet
	@EnableEtag
	public ActionResult about(RequestAccessor request, HttpServletResponse response) throws Exception {
		return JspResult.DEFAULT;
	}

	@HttpGetPost
	@EnableEtag
	public ActionResult contact(RequestAccessor request, HttpServletResponse response) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			request.storeModel(new ContactUsModel());
			return JspResult.DEFAULT;
		}

		// POST
		ContactUsModel model = new ContactUsModel();
		this.modelBinder.bindModel(request, model);

		request.getModelState().remove("subject");

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// EMAIL
			model.setSubject(request.res("drpham_contact.message_email_subject", model.getYourName()));
			String fromAddress = EmailUtils.toPersonEmail(model.getYourEmail(), model.getYourName());

			// MailMsg
			MailMsg mailMsg = new MailMsg();
			mailMsg.setFromEmail(fromAddress);
			mailMsg.setReplyToEmail(fromAddress);
			mailMsg.setToEmail(AppUtils.DRPHAM_ESL_EMAIL);
			mailMsg.setSubject(model.getSubject());
			mailMsg.setContent(model.getMessage());
			mailMsg.setIsHtml(BitBool.FALSE);

			mailMsg.setMailerId(MailMsgs.MAILER_1);
			mailMsg.setPriority(MailMsgs.PRIORITY_2);
			mailMsg.setServiceId(Services.TYPE_SEND_MSG);

			mailMsg.setClientId(request.getRequestContext().getClientId());
			mailMsg.setTimeCreated(System.currentTimeMillis());

			mailMsgService.add(mailMsg);

			request.getMessages().addNotice(request.res("drpham_contact.message_sent_successfully"));
			return new RedirectResult("contact");

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}
}
