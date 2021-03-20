package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.NotBind;
import com.drphamesl.entities.VocabList;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class VocabIndexModel {

	@NotBind
	private List<VocabList> vocabLists;

	public List<VocabList> getVocabLists() {
		return vocabLists;
	}

	public void setVocabLists(List<VocabList> vocabLists) {
		this.vocabLists = vocabLists;
	}
}
