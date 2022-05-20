package com.drphamesl.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.InitializeException;
import com.appslandia.plum.base.Resources;
import com.appslandia.plum.defaults.DefaultResourcesProvider;
import com.drphamesl.entities.Resource;
import com.drphamesl.services.ResourceService;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class ResourceManager extends DefaultResourcesProvider {

	@Inject
	protected ResourceService resourceService;

	@Override
	protected Resources loadResources(String language) throws InitializeException {
		Resources resources = super.loadResources(language);
		List<Resource> dbResources = this.resourceService.queryAll();

		Map<String, String> props = new HashMap<>();
		dbResources.stream().forEach(r -> props.put(r.getResourceId(), r.getResourceText()));

		resources.putResources(props);
		return resources;
	}
}
