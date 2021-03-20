package com.drphamesl.models;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class LoadVocabResult {

	private Integer vocabId;
	private String words;

	private String tags;
	private String definitions;

	private String message;

	public Integer getVocabId() {
		return vocabId;
	}

	public void setVocabId(Integer vocabId) {
		this.vocabId = vocabId;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
