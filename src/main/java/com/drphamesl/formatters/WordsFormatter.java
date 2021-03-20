package com.drphamesl.formatters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterException;
import com.appslandia.common.utils.StringUtils;
import com.drphamesl.utils.VocabUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class WordsFormatter implements Formatter {

	public static final String ERROR_MSG_KEY = WordsFormatter.class.getName() + ".message";

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
		return VocabUtils.toWords(str);
	}
}
