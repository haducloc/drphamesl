package com.drphamesl.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.appslandia.common.utils.TagUtils;
import com.appslandia.common.utils.ValueUtils;
import com.appslandia.plum.base.ActionResult;
import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableEtag;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.Model;
import com.appslandia.plum.base.PagerModel;
import com.appslandia.plum.base.PathParams;
import com.appslandia.plum.base.RequestAccessor;
import com.appslandia.plum.results.JspResult;
import com.appslandia.plum.results.RedirectResult;
import com.drphamesl.entities.BlogPost;
import com.drphamesl.models.BlogSearchModel;
import com.drphamesl.services.BlogPostService;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller
public class BlogController {

	static final int PAGE_SIZE = 10;

	@Inject
	protected BlogPostService blogPostService;

	@HttpGet
	@EnableEtag
	public ActionResult index(RequestAccessor request, HttpServletResponse response, @Model BlogSearchModel model) throws Exception {
		model.setPageIndex(ValueUtils.valueOrMin(model.getPageIndex(), 1));

		final String wrapTag = (model.getTag() != null) ? TagUtils.wrapTag(model.getTag()) : null;

		// Posts
		List<BlogPost> posts = blogPostService.getBlogPosts().stream().filter(p -> (wrapTag != null) ? p.getTags().contains(wrapTag) : true)
				.collect(Collectors.toList());

		int startPos = (model.getPageIndex() - 1) * PAGE_SIZE;
		model.setBlogPosts(posts.stream().skip(startPos).limit(PAGE_SIZE).collect(Collectors.toList()));

		model.getRecordCount().value = posts.size();
		model.setPagerModel(new PagerModel(model.getPageIndex(), model.getRecordCount().val(), PAGE_SIZE));

		// TAGS
		model.setBlogTags(blogPostService.getBlogTags());

		request.storeModel(model);
		return JspResult.DEFAULT;
	}

	@HttpGet
	@EnableEtag
	@PathParams("/{blogPostId}-{title_path}")
	public ActionResult post(RequestAccessor request, HttpServletResponse response, int blogPostId, String title_path) throws Exception {
		// Active Posts
		List<BlogPost> activePosts = blogPostService.getBlogPosts();

		BlogPost blogPost = activePosts.stream().filter(p -> p.getBlogPostId() == blogPostId).findFirst().orElse(null);
		request.assertNotFound(blogPost != null);

		if (!blogPost.getTitle_path().equals(title_path)) {
			return new RedirectResult("post", "blog").status(HttpServletResponse.SC_MOVED_PERMANENTLY).query("blogPostId", blogPostId).query("title_path",
					blogPost.getTitle_path());
		}

		request.storeModel(blogPost);

		// TAGS
		request.store("blogTags", blogPostService.getBlogTags());
		return JspResult.DEFAULT;
	}
}
