package com.drphamesl.formatters;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.appslandia.common.cdi.CDISupplier;
import com.appslandia.common.cdi.Supplier;
import com.appslandia.common.formatters.Formatter;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Supplier(Formatter.class)
public class FormatterSupplier implements CDISupplier {

	@Override
	public Map<String, Formatter> get() {
		Map<String, Formatter> map = new HashMap<>();

		map.put(Formatters.RELAXED_HTML, new RelaxedHtmlFormatter());

		map.put(Formatters.WORDS, new WordsFormatter());
		map.put(Formatters.DEFINITIONS, new DefinitionsFormatter());

		map.put(Formatters.QUERY_TEXT, new QueryTextFormatter());
		return map;
	}
}
