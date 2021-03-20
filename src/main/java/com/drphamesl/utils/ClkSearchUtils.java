package com.drphamesl.utils;

import com.appslandia.plum.utils.XmlEscaper;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ClkSearchUtils {

	public static String toClkSearch(String words) {
		if (words == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(words.length() * 2);
		addClickSpan(sb, words);
		return sb.toString();
	}

	public static String toClkSearchSt(String text) {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(text.length() * 2);

		int i = 0;
		while (i < text.length()) {
			int j = i;
			while (j < text.length() && !Character.isWhitespace(text.charAt(j)))
				j++;

			if (j < text.length()) {
				if (sb.length() > 0)
					sb.append(" ");

				addClickSpan(sb, text.substring(i, j));

				i = j;
				while (i < text.length() && Character.isWhitespace(text.charAt(i)))
					i++;

			} else {
				if (sb.length() > 0)
					sb.append(" ");

				addClickSpan(sb, text.substring(i, j));
				break;
			}

		}
		return sb.toString();
	}

	static void addClickSpan(StringBuilder sb, String words) {
		String cssClass = "clk-search";

		if (words.equals(VocabUtils.DEFS_SEP)) {
			cssClass = "defs-sep";

		} else if (words.equals("[x]")) {
			cssClass = "marked-def";
		}
		sb.append("<span class=\"").append(cssClass).append("\">").append(XmlEscaper.escapeXmlContent(words)).append("</span>");
	}
}
