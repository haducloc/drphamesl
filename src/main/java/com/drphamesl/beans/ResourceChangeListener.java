package com.drphamesl.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import com.appslandia.plum.base.ResourcesProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class ResourceChangeListener {

	@Inject
	protected ResourcesProvider resourcesProvider;

	public void onEventAsync(@ObservesAsync ResourceChange event) {
		resourcesProvider.clearResources();
	}
}
