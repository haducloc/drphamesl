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
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.base.Resources;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.appslandia.plum.utils.JspTemplateUtils;
import com.drphamesl.entities.BlogPost;
import com.drphamesl.entities.MailMsg;
import com.drphamesl.entities.Signup;
import com.drphamesl.models.BlogSearchModel;
import com.drphamesl.models.EmailBlogNewsModel;
import com.drphamesl.services.BlogPostService;
import com.drphamesl.services.MailMsgService;
import com.drphamesl.services.SignupService;
import com.drphamesl.utils.AppUtils;
import com.drphamesl.utils.MailMsgs;
import com.drphamesl.utils.NewsTypes;
import com.drphamesl.utils.Services;
import com.drphamesl.utils.TimeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(value = "manage-post", module = Modules.ADMIN)
@Authorize(appscope = true, policies = AdminPolicy.ADMIN)
public class ManagePostController {

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
	protected BlogPostService blogPostService;

	@Inject
	protected SignupService signupService;

	@Inject
	protected MailMsgService mailMsgService;

	@HttpGet
	public ActionResult index(RequestAccessor request, HttpServletResponse response, @Model BlogSearchModel model) throws Exception {
		model.setPageIndex(ValueUtils.valueOrMin(model.getPageIndex(), 1));

		// All Posts
		List<BlogPost> posts = this.blogPostService.query(model.getTag(), model.getPageIndex(), PAGE_SIZE, model.getRecordCount());
		model.setBlogPosts(posts);
		model.setPagerModel(new PagerModel(model.getPageIndex(), model.getRecordCount().val(), PAGE_SIZE));

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	@HttpGetPost
	public ActionResult edit(RequestAccessor request, HttpServletResponse response, Integer blogPostId) throws Exception {
		// GET
		if (request.isGetOrHead()) {
			BlogPost model = null;

			if (blogPostId == null) {
				model = new BlogPost();
				model.setActive(BitBool.FALSE);
			} else {
				model = this.blogPostService.findByPk(blogPostId);
				request.assertNotNull(model);
			}

			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		// POST
		BlogPost model = new BlogPost();
		modelBinder.bindModel(request, model);

		// New
		if (model.getBlogPostId() == null) {
			request.getModelState().remove("timeCreated");
		}

		if (!request.getModelState().isValid("title_path")) {
			request.getModelState().addError("titleText", request.res(Resources.ERROR_FIELD_INVALID, request.res("blogPost.titleText")));
		}

		if (!request.getModelState().isValid()) {
			request.storeModel(model);
			return JspResult.DEFAULT;
		}

		try {
			// Remove?
			if (request.isRemoveAction()) {
				blogPostService.remove(model.getBlogPostId());

				request.getMessages().addNotice(request.res("record.deleted_successfully", request.res("blogPost")));

				return new RedirectResult("index");
			}

			// New
			if (model.getBlogPostId() == null) {
				model.setTimeCreated(TimeUtils.nowAtGMT7());
			}

			// Save
			this.blogPostService.save(model);
			request.getMessages().addNotice(request.res("record.saved_successfully", request.res("blogPost")));

			return new RedirectResult("edit").query("blogPostId", model.getBlogPostId());

		} catch (Exception ex) {
			logger.error(ex);
			request.getMessages().addError(this.exceptionHandler.getProblem(request, ex).getTitle());

			request.storeModel(model);
			return JspResult.DEFAULT;
		}
	}

	@HttpGet
	@PathParams("/{blogPostId}-{title_path}")
	public ActionResult preview(RequestAccessor request, HttpServletResponse response, int blogPostId, String title_path) throws Exception {
		BlogPost blogPost = blogPostService.findByPk(blogPostId);
		request.assertNotFound(blogPost != null);

		request.storeModel(blogPost);
		request.store("blogTags", blogPostService.getBlogTags());

		return new JspResult().path("/blog/post.jsp");
	}

	@HttpGet
	public ActionResult notify(RequestAccessor request, HttpServletResponse response, int blogPostId) throws Exception {
		BlogPost blogPost = blogPostService.findByPk(blogPostId);
		request.assertNotNull(blogPost);

		request.assertTrue(blogPost.getActive() == BitBool.TRUE);
		request.assertTrue(blogPost.getNotified() == BitBool.FALSE);

		this.blogPostService.markNotified(blogPostId);

		// EMAIL
		EmailBlogNewsModel model = new EmailBlogNewsModel();

		model.setSubject(request.res("manage_post_notify.new_post_email_subject", blogPost.getTitleText()));
		model.setBlogPostUrl(actionParser.toActionUrl(request, "blog", "post",
				new Params().set("blogPostId", blogPostId).set("title_path", blogPost.getTitle_path()), true));

		List<Signup> signups = this.signupService.queryAll();
		Map<String, Object> params = new HashMap<>();
		int count = 0;

		for (Signup signup : signups) {
			if ((signup.getNewsMask() & NewsTypes.NEWS_BLOG) != NewsTypes.NEWS_BLOG) {
				continue;
			}

			params.put("email", signup.getEmail());
			params.put("sid", signup.getSid());
			params.put("type", NewsTypes.NEWS_BLOG);

			String unsubscribeUrl = actionParser.toActionUrl(request, "signup", "remove", params, true);
			model.setUnsubscribeUrl(unsubscribeUrl);

			String htmlContent = JspTemplateUtils.executeString(request, response, "/WEB-INF/templates/news_blog_email.jsp", model);

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

		request.getMessages().addNotice(request.res("manage_post_notify.post_notified_successfully", count));
		return new RedirectResult("index");
	}
}
