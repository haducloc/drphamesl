package com.drphamesl.caching;

import java.time.Duration;
import java.util.List;

import com.appslandia.common.base.FluentList;
import com.appslandia.common.caching.AppCacheManager;
import com.appslandia.common.cdi.CDIFactory;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class CacheManagerFactoryImpl implements CDIFactory<AppCacheManager> {

	@Produces
	@Alternative
	@ApplicationScoped
	@Override
	public AppCacheManager produce() {

		//@formatter:off
		List<EhCacheConfig> configs = new FluentList<EhCacheConfig>()
				.ins(new EhCacheConfig().setCacheName(Caches.DEFAULT).setExpiration(Duration.ofDays(90)));
		//@formatter:on

		EhAppCacheManager impl = new EhAppCacheManager(EhCacheConfig.toBuilder(configs).build(true));
		return impl;
	}

	@Override
	public void dispose(@Disposes AppCacheManager impl) {
		impl.close();
	}
}
