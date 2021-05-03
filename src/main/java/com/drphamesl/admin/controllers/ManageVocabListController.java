package com.drphamesl.admin.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.base.Params;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.HexUtils;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.plum.base.ActionParser;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Authorize;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.ExceptionHandler;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.HttpGetPost;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.ModelBinder;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.PagerModel;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.base.Resources;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.JspTemplateUtils;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.entities.Signup;
import com.drphamesl.entities.VocabList;
import com.drphamesl.models.EmailVocabNewsModel;
import com.drphamesl.models.ManageVocabListIndexModel;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.services.SignupService;
import com.drphamesl.services.VocabListService;
import com.drphamesl.services.VocabService;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.NewsTypes;
import com.drphamesl.utils.Services;
import com.drphamesl.utils.ShareTypes;
import com.drphamesl.utils.TimeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-vocablist", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManageVocabListController {

	static final int PAGE_SIZE = 10;

	@Inject
	protected AppLogger logger;

	@Inject
	protected ModelBinder modelBinder;

	@Inject
	protected ActionParser actionParser;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected VocabService vocabService;

	@Inject
	protected VocabListService vocabListService;

	@Inject
	protected SignupService signupService;

	@Inject
	protected MailMsgService mailMsgService;

	void prepareIndex(RequestAccessor request, ManageVocabListIndexModel model) {
		model.setShareTypes(ShareTypes.createList(request.getResources(), true));
	}

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response, @Model ManageVocabListIndexModel model) throws Exception {
		model.setPageIndex(ValueUtils.valueOrMin(model.getPageIndex(), 1));

		if (!request.getModelState().isValid("shareType")) {
			model.setShareType(null);
		}

		// VocabList
		List<VocabList> list = this.vocabListService.query(model.getShareType(), model.getTag(), model.getPageIndex(), PAGE_SIZE, model.getRecordCount());
		model.setVocabLists(list);
		model.setPagerModel(new PagerModel(model.getPageIndex(), model.getRecordCount().val(), PAGE_SIZE));

		request.storeModel(model);
		prepareIndex(request, model);
		return JspResult.DEFAULT;
	}

	void prepareEdit(RequestAccessor request) {
		request.store("shareTypes", ShareTypes.createList(request.getResources(), false));
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, Integer vocabListId) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			VocabList model = null;

			if (vocabListId == null) {
				model = new VocabList();
			} else {
				model = this.vocabListService.findByPk(vocabListId);
				request.assertNotNull(model);
			}

			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		// POST
		VocabList model = new VocabList();
		modelBinder.bindModel(request, model);

		// New
		if (model.getVocabListId() == null) {
			request.getModelState().remove("spk", "timeCreated");
		}

		// Validate TAG
		if (request.isSaveAction()) {
			if (request.getModelState().isValid("tag")) {
				if (!vocabService.checkTag(model.getTag())) {
					request.getModelState().addError("tag", request.res("manage_vocablist_edit.tag_doesnt_exist"));
				}
			}
		}

		if (!request.getModelState().isValid("title_path")) {
			request.getModelState().addError("titleText", request.res(Resources.ERROR_FIELD_INVALID, request.res("vocabList.titleText")));
		}

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			prepareEdit(request);
			return JspResult.DEFAULT;
		}

		try {
			// Remove?
			if (model.getVocabListId() != null && request.isRemoveAction()) {
				vocabListService.remove(model.getVocabListId());

				request.getMessages().addNotice(request.res("record.deleted_successfully", request.res("vocabList")));
				return new RedirectResult("index");
			}

			// New
			if (model.getVocabListId() == null) {
				model.setTimeCreated(TimeUtils.nowAtGMT7());

				// 16 length
				model.setSpk(HexUtils.encodeHexToString(RandomUtils.nextBytes(8)));
			}

			// Save
			this.vocabListService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("vocabList")));

			if (model.getVocabListId() != null) {
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

	@HttpGet
	public ActionResult notify(RequestAccessor request, HttpServletResponse response, int vocabListId) throws Exception {
		VocabList vocabList = vocabListService.findByPk(vocabListId);
		request.assertNotNull(vocabList);

		request.assertTrue(vocabList.getShareType() == ShareTypes.ON_PUBLIC);
		request.assertTrue(vocabList.getNotified() == BitBool.FALSE);

		this.vocabListService.markNotified(vocabListId);

		// EMAIL
		EmailVocabNewsModel model = new EmailVocabNewsModel();

		model.setSubject(request.res("manage_vocablist_notify.new_list_email_subject", vocabList.getTitleText()));
		model.setVocabListUrl(
				actionParser.toActionUrl(request, "vocab", "list", new Params().set("pk", vocabListId).set("title_path", vocabList.getTitle_path()), true));

		List<Signup> signups = this.signupService.queryAll();
		Map<String, Object> params = new HashMap<>();
		int count = 0;

		for (Signup signup : signups) {
			if ((signup.getNewsMask() & NewsTypes.NEWS_VOCAB) != NewsTypes.NEWS_VOCAB) {
				continue;
			}

			params.put("email", signup.getEmail());
			params.put("sid", signup.getSid());
			params.put("type", NewsTypes.NEWS_VOCAB);

			String unsubscribeUrl = actionParser.toActionUrl(request, "signup", "remove", params, true);
			model.setUnsubscribeUrl(unsubscribeUrl);

			String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/news_vocab_email.jsp", model);

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

		request.getMessages().addNotice(request.res("manage_vocablist_notify.list_notified_successfully", count));
		return new RedirectResult("index");
	}
}
