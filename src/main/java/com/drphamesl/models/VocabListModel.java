package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.NotBind;
import com.drphamesl.entities.Vocab;
import com.drphamesl.entities.VocabList;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class VocabListModel {

	@NotBind
	private VocabList vocabList;

	@NotBind
	private List<Vocab> vocabs;

	public int getVocabCount() {
		return vocabs.size();
	}

	public VocabList getVocabList() {
		return vocabList;
	}

	public void setVocabList(VocabList vocabList) {
		this.vocabList = vocabList;
	}

	public List<Vocab> getVocabs() {
		return vocabs;
	}

	public void setVocabs(List<Vocab> vocabs) {
		this.vocabs = vocabs;
	}
}
