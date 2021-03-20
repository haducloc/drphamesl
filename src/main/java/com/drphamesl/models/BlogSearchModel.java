package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.NotBind;
import com.appslandia.common.base.Out;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.plum.base.PagerModel;
import com.drphamesl.entities.BlogPost;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BlogSearchModel {

	@Bind(fmt = Formatter.TAG)
	private String tag;

	private Integer pageIndex;

	@NotBind
	private Out<Integer> recordCount = new Out<>();

	@NotBind
	private PagerModel pagerModel;

	@NotBind
	private List<BlogPost> blogPosts;

	@NotBind
	private List<String> blogTags;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Out<Integer> getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Out<Integer> recordCount) {
		this.recordCount = recordCount;
	}

	public PagerModel getPagerModel() {
		return pagerModel;
	}

	public void setPagerModel(PagerModel pagerModel) {
		this.pagerModel = pagerModel;
	}

	public List<BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(List<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}

	public List<String> getBlogTags() {
		return blogTags;
	}

	public void setBlogTags(List<String> blogTags) {
		this.blogTags = blogTags;
	}
}
