package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.NotBind;
import com.appslandia.common.models.SelectItem;
import com.appslandia.common.utils.URLEncoding;
import com.drphamesl.entities.Vocab;
import com.drphamesl.entities.VocabList;
import com.drphamesl.utils.VocabOrders;
import com.drphamesl.utils.VocabUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FlashcardModel {

	private Integer vocabOrder;

	private String nextType;

	// 1+
	private Integer index;

	@NotBind
	private int recordCount;

	@NotBind
	private Vocab vocab;

	@NotBind
	private VocabList vocabList;

	@NotBind
	private List<SelectItem> vocabOrders;

	@NotBind
	private List<SelectItem> nextTypes;

	@NotBind
	private long nextInMs;

	public Integer getNextIndex() {
		if (vocabOrder == VocabOrders.RANDOM_ORDER) {
			return null;
		}
		if (index == recordCount)
			return 1;
		return index + 1;
	}

	public String getImgQuery() {
		return VocabUtils.encodeWords(this.vocab.getWords()) + "+" + URLEncoding.encodeParam(this.vocabList.getTag());
	}

	public Integer getVocabOrder() {
		return vocabOrder;
	}

	public void setVocabOrder(Integer vocabOrder) {
		this.vocabOrder = vocabOrder;
	}

	public String getNextType() {
		return nextType;
	}

	public void setNextType(String nextType) {
		this.nextType = nextType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public Vocab getVocab() {
		return vocab;
	}

	public void setVocab(Vocab vocab) {
		this.vocab = vocab;
	}

	public VocabList getVocabList() {
		return vocabList;
	}

	public void setVocabList(VocabList vocabList) {
		this.vocabList = vocabList;
	}

	public List<SelectItem> getVocabOrders() {
		return vocabOrders;
	}

	public void setVocabOrders(List<SelectItem> vocabOrders) {
		this.vocabOrders = vocabOrders;
	}

	public List<SelectItem> getNextTypes() {
		return nextTypes;
	}

	public void setNextTypes(List<SelectItem> nextTypes) {
		this.nextTypes = nextTypes;
	}

	public long getNextInMs() {
		return nextInMs;
	}

	public void setNextInMs(long nextInMs) {
		this.nextInMs = nextInMs;
	}
}
