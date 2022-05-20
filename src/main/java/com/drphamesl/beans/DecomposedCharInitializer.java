package com.drphamesl.beans;

import com.appslandia.common.cdi.Eager;
import com.appslandia.common.utils.NormalizeUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Eager
public class DecomposedCharInitializer {

	@PostConstruct
	protected void initialize() {

		// Vietnamese
		NormalizeUtils.setDecomposedCharacterConverter(c -> {
			if (c == 272)
				return 'D';

			if (c == 273)
				return 'd';

			return c;
		});
	}
}
