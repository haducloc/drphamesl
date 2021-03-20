package com.drphamesl.formatters;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.Out;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.formatters.FormatterException;
import com.appslandia.common.utils.StringUtils;
import com.drphamesl.utils.VocabUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class QueryTextFormatter implements Formatter {

	public static final String ERROR_MSG_KEY = QueryTextFormatter.class.getName() + ".message";

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
			return str;
		}
		Out<Boolean> isValid = new Out<Boolean>();
		String query = VocabUtils.toQueryText(str, isValid);

		if (!isValid.val()) {
			throw new FormatterException("Query is invalid (query=" + str + ")", getErrorMsgKey());
		}
		return query;
	}
}
