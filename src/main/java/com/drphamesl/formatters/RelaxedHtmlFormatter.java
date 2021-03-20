package com.drphamesl.formatters;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterException;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RelaxedHtmlFormatter implements Formatter {

	public static final String ERROR_MSG_KEY = RelaxedHtmlFormatter.class.getName() + ".message";

	@Override
	public String getErrorMsgKey() {
		return ERROR_MSG_KEY;
	}

	@Override
	public Class<?> getArgType() {
		return String.class;
	}

	@Override
	public String format(Object obj, FormatProvider formatProvider) {
		return (String) obj;
	}

	@Override
	public String parse(String str, FormatProvider formatProvider) throws FormatterException {
		str = StringUtils.trimToNull(str);
		if (str == null) {
			return null;
		}

		// @formatter:off
		str = Jsoup.clean(str, Whitelist.relaxed()
				.addAttributes("a", "class", "target")
				.addAttributes("p", "class")
				.addAttributes("ol", "class")
				.addAttributes("ul", "class")
				.addAttributes("li", "class")
				.addAttributes("img", "class")
				.addAttributes("span", "class")
				.removeEnforcedAttribute("a", "rel")
				);
		// @formatter:on

		return str;
	}
}
