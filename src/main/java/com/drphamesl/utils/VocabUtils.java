package com.drphamesl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.appslandia.common.base.Out;
import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.StringFormat;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.URLEncoding;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class VocabUtils {

	public static String toWords(String words) {
		if (StringUtils.isNullOrEmpty(words)) {
			return null;
		}

		if (words.startsWith(":")) {
			return NormalizeUtils.normalizeText((words.substring(1)));
		}
		words = NormalizeUtils.normalizeText(words);

		int diff = 0;
		String lc = words.toLowerCase(Locale.ROOT);

		for (int i = 0; i < words.length(); i++) {
			if (words.charAt(i) != lc.charAt(i)) {
				diff++;
			}
		}
		if ((diff == 1) && (words.charAt(0) != lc.charAt(0))) {
			return lc;
		}
		return words;
	}

	public static String encodeWords(String words) {
		if (words.contains(" ")) {
			return "\"" + URLEncoding.encodeParam(words) + "\"";
		}
		return URLEncoding.encodeParam(words);
	}

	public static String toQueryText(String query, Out<Boolean> isValid) {
		isValid.value = true;
		if (query == null) {
			return query;
		}

		// WORDS
		if (!query.startsWith("#")) {
			return NormalizeUtils.normalizeText(query);
		}

		// #tag
		query = query.toLowerCase(Locale.ROOT);
		String nmlTag = NormalizeUtils.normalizeLabel(query);

		if (nmlTag == null) {
			isValid.value = false;
			return null;
		}
		return '#' + nmlTag;
	}

	public static String nullOrLower(String query) {
		if (query == null) {
			return null;
		}
		return query.toLowerCase(Locale.ROOT);
	}

	public static String emptyOrLower(String query) {
		if (query == null) {
			return StringUtils.EMPTY_STRING;
		}
		return query.toLowerCase(Locale.ROOT);
	}

	static final Pattern DOT3_PATTERN = Pattern.compile("\\.{3,}");

	public static String toSentence(String sentence) {
		if (sentence == null) {
			return null;
		}
		sentence = sentence.replaceAll("\u2026", " ...");
		sentence = sentence.replaceAll(Pattern.quote(" : "), ": ");

		sentence = DOT3_PATTERN.matcher(sentence).replaceAll(" ...");
		sentence = NormalizeUtils.normalizeText(sentence);
		sentence = StringUtils.firstUpperCase(sentence, Locale.ROOT);
		return fixQuotes(sentence);
	}

	public static String fixQuotes(String str) {
		if (str == null) {
			return null;
		}
		return str.replace((char) 8217, '\'');
	}

	static final Pattern WORDS_CLASS_PATTEN = Pattern.compile("\\[.+]", Pattern.CASE_INSENSITIVE);

	public static String toDbDefinitions(String definitions, Out<Boolean> isValid) {
		isValid.value = true;
		TextBuilder defs = new TextBuilder();

		boolean hasMarked = false;
		try (BufferedReader br = new BufferedReader(new StringReader(definitions))) {
			String line;
			boolean hasDef = false;

			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}

				// class
				if (WORDS_CLASS_PATTEN.matcher(line).matches()) {
					line = NormalizeUtils.normalizeText(line.substring(1, line.length() - 1));
					if (line != null) {
						defs.appendlnIfNotEmpty().append("[").append(line.toLowerCase(Locale.ROOT)).append("]");
					}
					continue;
				}

				// sentence
				if (line.startsWith("-")) {
					line = toSentence(line.substring(1));
					if (line != null) {
						defs.appendlnIfNotEmpty().append(" -").append(line);
					}
					if (!hasDef) {
						isValid.value = false;
					}
					continue;
				}

				// definition
				line = line.startsWith(":") ? toSentence(line.substring(1)) : toSentence(line);
				if (line != null) {
					hasDef = true;

					boolean last3Ast = line.endsWith(" ***");
					hasMarked = hasMarked || line.endsWith(" [x]") || last3Ast;

					// Convert *** to [x]
					if (last3Ast) {
						line = line.substring(0, line.length() - 3) + "[x]";
					}
					defs.appendlnIfNotEmpty().append(":").append(line);
				}
			}
		} catch (IOException ex) {
			isValid.value = false;
		}

		// No marked [x]
		if (!hasMarked) {
			isValid.value = false;
		}
		return defs.length() > 0 ? defs.toString() : null;
	}

	public static List<String> getMarkedDefs(List<DefGroup> groups) {
		List<String> list = new ArrayList<>(8);

		for (DefGroup g : groups) {
			if (!g.getDef().endsWith("[x]")) {
				continue;
			}
			list.add(g.getDef().substring(0, g.getDef().length() - 4));
		}
		return list;
	}

	public static final String DEFS_SEP = "\u2730";
	public static final String DEFS_SEP_SP = StringFormat.fmt(" {} ", DEFS_SEP);

	public static String toDefs1Line(List<DefGroup> groups) {
		return String.join(DEFS_SEP_SP, getMarkedDefs(groups));
	}

	public static List<DefGroup> getDefGroups(String dbDefinitions) {
		List<DefGroup> list = new ArrayList<DefGroup>();

		DefGroup defGroup = null;
		try (BufferedReader br = new BufferedReader(new StringReader(dbDefinitions))) {

			String prevLine = null;
			String line = null;

			while (true) {
				prevLine = line;
				if ((line = br.readLine()) == null) {
					break;
				}

				if (line.startsWith(":")) {
					defGroup = new DefGroup();
					defGroup.setDef(line);

					if (prevLine != null && prevLine.startsWith("[")) {
						defGroup.setClazz(prevLine);
					}
					list.add(defGroup);
				}

				if (line.startsWith(" -")) {
					defGroup.getSents().add(line);
				}
			}
		} catch (IOException ex) {
		}
		return list;
	}

	public static final Map<String, String> WORDS_TYPES;

	static {
		Map<String, String> map = new HashMap<>();
		map.put("noun", "noun");
		map.put("verb", "verb");

		map.put("pronoun", "pron");
		map.put("adjective", "adj");
		map.put("adverb", "adv");
		map.put("preposition", "prep");
		map.put("conjunction", "conj");
		map.put("interjection", "interj");
		map.put("abbreviation", "abbr");

		WORDS_TYPES = Collections.unmodifiableMap(map);
	}
}
