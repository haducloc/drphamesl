package com.drphamesl.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.StringUtils;
import com.appslandia.common.utils.URLEncoding;
import com.appslandia.plum.base.AppConfig;
import com.appslandia.sweetsop.http.HttpClient;
import com.appslandia.sweetsop.readers.TextReader;
import com.drphamesl.entities.Vocab;
import com.drphamesl.utils.VocabUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class VocabLoader {

	static final String DICT_API_URI_BASE = "https://dictionaryapi.com/api/v3/references/learners/json/";

	@Inject
	protected AppConfig appConfig;

	@Inject
	protected AppLogger logger;

	private String getLookupUrl(String words) {
		return DICT_API_URI_BASE + URLEncoding.encodePath(words) + "?key=" + this.appConfig.getRequiredString("dict_api_key");
	}

	public boolean load(Vocab vocab) {
		AssertUtils.assertNotNull(vocab);
		AssertUtils.assertNotNull(vocab.getWords());

		try {
			HttpClient http = HttpClient.get(getLookupUrl(vocab.getWords()));
			http.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");

			http.setResultReader(TextReader.INSTANCE);
			if (http.execute() != 200) {
				return true;
			}

			List<Definition> definitions = new ArrayList<Definition>();
			try (JsonReader jsonReader = Json.createReader(new BufferedReader(new BufferedReader(new StringReader(http.getResultObject()))))) {

				for (JsonValue mt : jsonReader.readArray()) {
					if (!(mt instanceof JsonObject)) {
						break;
					}
					JsonObject meta = (JsonObject) mt;

					String idLc = meta.getJsonObject("meta").getString("id").toLowerCase(Locale.ENGLISH);
					String wordsLc = vocab.getWords().toLowerCase(Locale.ENGLISH);

					if (!idLc.equals(wordsLc) && !idLc.startsWith(wordsLc + ":")) {
						continue;
					}

					// Type of words
					String type = meta.getString("fl", null);
					if (type == null) {
						continue;
					}
					type = type.trim();
					type = VocabUtils.WORDS_TYPES.getOrDefault(type, type);

					// Definitions
					JsonArray def = meta.getJsonArray("def");
					if (def != null) {

						for (int j = 0; j < def.size(); j++) {
							JsonArray sseq = def.getJsonObject(j).getJsonArray("sseq");

							for (int k = 0; k < sseq.size(); k++) {
								JsonArray sseqChildren = sseq.getJsonArray(k);

								for (int l = 0; l < sseqChildren.size(); l++) {
									JsonArray dtArray = sseqChildren.getJsonArray(l).getJsonObject(1).getJsonArray("dt");
									if (dtArray != null) {
										parseDt(dtArray, type, definitions);
									}
								}
							}
						}
					}
				}
			}

			// Build definitions
			TextBuilder defs = new TextBuilder();
			String prevType = null;

			for (Definition def : definitions) {
				if (!def.type.equals(prevType)) {
					defs.appendlnIfNotEmpty().append("[").append(def.type).appendln("]").append(":").append(VocabUtils.toSentence(def.definition));
				} else {
					defs.appendlnIfNotEmpty().append(":").append(VocabUtils.toSentence(def.definition));
				}
				if (definitions.size() == 1) {
					defs.append(" [x]"); // Default marked
				}
				prevType = def.type;

				if (!StringUtils.isNullOrEmpty(def.sentence)) {
					defs.appendln();
					defs.append(" -").append(VocabUtils.toSentence(def.sentence));
				}
			}
			vocab.setDefinitions(defs.length() > 0 ? defs.toString() : null);

		} catch (IOException ex) {
			logger.error(ex);
		}
		return true;
	}

	static String parsePron(JsonObject hwi) {
		JsonArray pr = hwi.getJsonArray("prs");
		if (pr == null || pr.isEmpty())
			return null;
		return pr.getJsonObject(0).getString("ipa");
	}

	static void parseDt(JsonArray dt, String type, List<Definition> definitions) {
		parseDtCt(dt, type, definitions);

		// No text before uns
		int startExclusive = 0;
		while (true) {
			int unsIdx = startExclusive;
			boolean hasText = false;

			while (unsIdx < dt.size()) {
				if (isDtElement(dt.get(unsIdx), "text")) {
					hasText = true;
				}
				if (isDtElement(dt.get(unsIdx), "uns")) {
					break;
				}
				unsIdx++;
			}

			if (unsIdx == dt.size())
				break;

			if (!hasText) {
				for (JsonValue subDt : ((JsonArray) dt.get(unsIdx)).getJsonArray(1)) {
					parseDtCt((JsonArray) subDt, type, definitions);
				}
				startExclusive = unsIdx + 1;
			} else {
				startExclusive = unsIdx + 1;
			}
		}
	}

	static void parseDtCt(JsonArray dt, String type, List<Definition> definitions) {
		int startExclusive = 0;
		while (true) {

			// textIdx
			int textIdx = startExclusive;
			while (textIdx < dt.size()) {
				if (isDtElement(dt.get(textIdx), "text")) {
					break;
				}
				textIdx++;
			}
			if (textIdx == dt.size())
				break;

			// textOrVisIndex
			int textOrVisIndex = textIdx + 1;
			while (textOrVisIndex < dt.size()) {
				if (isDtElement(dt.get(textOrVisIndex), "text") || isDtElement(dt.get(textOrVisIndex), "vis") || isDtElement(dt.get(textOrVisIndex), "uns")) {
					break;
				}
				textOrVisIndex++;
			}

			// No following text, vis or uns
			if (textOrVisIndex == dt.size()) {
				String definition = translate(((JsonArray) dt.get(textIdx)).getString(1));
				if (!definition.isEmpty())
					definitions.add(new Definition(type, definition, null));
				break;
			}

			// Has following text, vis or uns

			// textOrVisIndex: text
			if (isDtElement(dt.get(textOrVisIndex), "text")) {
				String definition = translate(((JsonArray) dt.get(textIdx)).getString(1));

				if (!definition.isEmpty())
					definitions.add(new Definition(type, definition, null));

				startExclusive = textOrVisIndex;

			} else if (isDtElement(dt.get(textOrVisIndex), "vis")) {
				String definition = translate(((JsonArray) dt.get(textIdx)).getString(1));

				String sentence = translate(((JsonArray) dt.get(textOrVisIndex)).getJsonArray(1).getJsonObject(0).getString("t"));
				definitions.add(new Definition(type, definition, sentence));

				startExclusive = textOrVisIndex + 1;
			} else {
				// uns
				String definition = translate(((JsonArray) dt.get(textIdx)).getString(1));

				JsonArray vis = findUnsVis(((JsonArray) dt.get(textOrVisIndex)).getJsonArray(1));
				String sentence = (vis != null) ? translate(vis.getJsonArray(1).getJsonObject(0).getString("t")) : null;

				definitions.add(new Definition(type, definition, sentence));
				startExclusive = textOrVisIndex + 1;
			}
		}
	}

	static JsonArray findUnsVis(JsonArray uns) {
		if (uns.size() == 0)
			return null;
		for (JsonValue jv : uns.getJsonArray(0)) {
			if (isDtElement(jv, "vis")) {
				return (JsonArray) jv;
			}
		}
		return null;
	}

	static Pattern WRAPPED_BY_CURLY_BRACE = Pattern.compile("^\\{[^{]+}$");

	static String translate(String text) {
		text = text.trim();
		StringBuilder sb = new StringBuilder(text.length()).append(text);

		replaceToken(sb, "", "{b}", "{/b}", "{inf}", "{/inf}", "{it}", "{/it}", "{sc}", "{/sc}", "{sup}", "{/sup}", "{parahw}", "{/parahw}", "{phrase}",
				"{/phrase}", "{qword}", "{/qword}", "{wi}", "{/wi}");

		replaceToken(sb, "\"", "{ldquo}", "{rdquo}");

		replaceToken(sb, "[", "{gloss}");
		replaceToken(sb, "]", "{/gloss}");

		deleteTokenCt(sb, "{dx}", "{/dx}");
		deleteTokenCt(sb, "{dx_def}", "{/dx_def}");
		deleteTokenCt(sb, "{dx_ety}", "{/dx_ety}");
		deleteTokenCt(sb, "{ma}", "{/ma}");

		translate(sb, "{a_link|", false);
		translate(sb, "{d_link|", false);
		translate(sb, "{i_link|", false);
		translate(sb, "{et_link|", false);

		translate(sb, "{sx|", true);

		// {bc}
		while (true) {
			int i = sb.indexOf("{bc}");
			if (i < 0) {
				break;
			}
			if (i == 0) {
				sb.delete(0, 4);
			} else {
				sb.replace(i, i + 4, " ");
			}
		}

		text = sb.toString();
		if (WRAPPED_BY_CURLY_BRACE.matcher(text).matches()) {
			return text.substring(1, text.length() - 1);
		}
		return text.trim();
	}

	public static void replaceToken(StringBuilder sb, String replacement, String... tokens) {
		for (String token : tokens) {
			while (true) {
				int i = sb.indexOf(token);
				if (i >= 0) {
					if (replacement.isEmpty()) {
						sb.delete(i, i + token.length());
					} else {
						sb.replace(i, i + token.length(), replacement);
					}
				} else {
					break;
				}
			}
		}
	}

	public static void deleteTokenCt(StringBuilder sb, String openToken, String closeToken) {
		while (true) {
			int i = sb.indexOf(openToken);
			if (i >= 0) {
				int j = sb.indexOf(closeToken, i + openToken.length());
				if (j >= 0) {
					sb.delete(i, j + closeToken.length());
				} else {
					break;
				}
			} else {
				break;
			}
		}
	}

	public static void translate(StringBuilder sb, String linkTokenStart, boolean isRef) {
		while (true) {
			int idx = sb.indexOf(linkTokenStart);
			if (idx < 0) {
				break;
			}
			int j = idx + 1;
			while (sb.charAt(j) != '}')
				j++;

			if (j < sb.length()) {
				String[] items = sb.substring(idx + 1, j).split("\\|");

				if (items.length >= 2) {
					if (!isRef) {
						sb.replace(idx, j + 1, items[1]);
					} else {
						sb.replace(idx + 1, j, items[1]);
					}
				} else {
					sb.replace(idx, j + 1, "");
				}
			}
		}
	}

	static class Definition {

		String type;
		String definition;
		String sentence;

		public Definition(String type, String definition, String sentence) {
			this.type = type;
			this.definition = definition;
			this.sentence = sentence;
		}
	}

	public static boolean isDtElement(JsonValue jv, String code) {
		AssertUtils.assertNotNull(jv);

		if (jv.getValueType() != ValueType.ARRAY)
			return false;

		JsonArray ja = (JsonArray) jv;
		if (ja.size() < 2)
			return false;

		if (ja.get(0).getValueType() != ValueType.STRING)
			return false;

		return code.equals(ja.getString(0));
	}
}
