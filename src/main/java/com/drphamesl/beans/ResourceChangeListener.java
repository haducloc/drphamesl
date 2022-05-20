package com.drphamesl.beans;

import com.appslandia.plum.base.ResourcesProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

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
