package com.drphamesl.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.appslandia.common.base.Out;
import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.BlogPost;
import com.drphamesl.utils.TimeUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class BlogPostService {

	static final String CACHE_KEY_BLOG_TAGS = "blog-tags";
	static final String CACHE_KEY_BLOG_POSTS = "blog-posts";

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public BlogPost findByPk(int pk) {
		return em.find(BlogPost.class, pk);
	}

	@Transactional
	public void remove(int pk) throws Exception {
		em.removeByPk(BlogPost.class, pk);

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CollectionUtils.toSet(CACHE_KEY_BLOG_POSTS, CACHE_KEY_BLOG_TAGS));
	}

	@Transactional
	public void save(BlogPost obj) throws Exception {
		if (obj.getPk() == null) {
			em.insert(obj);

		} else {
			BlogPost managed = em.find(BlogPost.class, obj.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, obj, "titleText", "contentText", "tags", "keywords", "descText", "imageUrl", "fbUrl", "active");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CollectionUtils.toSet(CACHE_KEY_BLOG_POSTS, CACHE_KEY_BLOG_TAGS));
	}

	@Transactional
	public void markNotified(int blogPostId) throws Exception {
		BlogPost managed = em.find(BlogPost.class, blogPostId);
		AssertUtils.assertNotNull(managed);

		managed.setNotified(BitBool.TRUE);
		managed.setTimeCreated(TimeUtils.nowAtGMT7());
	}

	public List<BlogPost> query(String tag, int pageIndex, int pageSize, Out<Integer> recordCount) {
		if (recordCount.value == null || recordCount.value <= 0) {
			recordCount.value = em.createNamedQuery("BlogPost.queryCount").setParameter("active", 0).setParameter("tag", tag)
					.setLike("wtag", TagUtils.wrapTag(tag)).getCount();
		}

		final int startPos = (pageIndex - 1) * pageSize;

		return em.createNamedQuery("BlogPost.query", BlogPost.class).setParameter("active", 0).setParameter("tag", tag).setLike("wtag", TagUtils.wrapTag(tag))
				.setStartPos(startPos).setMaxResults(pageSize).asReadonly().getResultList();
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_BLOG_POSTS)
	public List<BlogPost> getBlogPosts() {
		List<BlogPost> list = em.createNamedQuery("BlogPost.query", BlogPost.class).setParameter("active", 1).setParameter("tag", null)
				.setLike("wtag", TagUtils.wrapTag(null)).asReadonly().getResultList();
		return Collections.unmodifiableList(list);
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_BLOG_TAGS)
	public List<String> getBlogTags() throws Exception {
		List<String> list = em.createNamedQuery("BlogPost.queryDbTags").getList();
		Set<String> tags = new TreeSet<>();

		for (String dbTags : list) {
			CollectionUtils.toSet(tags, TagUtils.toTags(dbTags));
		}
		return Collections.unmodifiableList(new ArrayList<>(tags));
	}
}
