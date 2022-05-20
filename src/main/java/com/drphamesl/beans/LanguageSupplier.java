package com.drphamesl.beans;

import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.Language;
import com.appslandia.common.cdi.CDISupplier;
import com.appslandia.common.cdi.Supplier;

import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Supplier(Language.class)
public class LanguageSupplier implements CDISupplier {

	@Override
	public List<Language> get() {
		List<Language> langs = new ArrayList<>();

		langs.add(Language.VI);
		return langs;
	}
}
