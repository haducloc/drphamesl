package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.NotBind;
import com.appslandia.common.models.SelectItem;
import com.appslandia.common.utils.URLEncoding;
import com.drphamesl.entities.Vocab;
import com.drphamesl.entities.VocabList;
import com.drphamesl.utils.TestOrders;
import com.drphamesl.utils.VocabUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class VocabTestModel {

	private Integer testOrder;

	private String nextType;

	// 1+
	private Integer index;

	@NotBind
	private int recordCount;

	@NotBind
	private List<Vocab> vocabs;

	@NotBind
	private int vocabId;

	@NotBind
	private VocabList vocabList;

	@NotBind
	private List<SelectItem> testOrders;

	@NotBind
	private List<SelectItem> nextTypes;

	@NotBind
	private long nextInMs;

	@NotBind
	private Vocab _vocab;

	public Integer getNextIndex() {
		if (testOrder == TestOrders.RANDOM_ORDER) {
			return null;
		}
		if (index == recordCount)
			return 1;
		return index + 1;
	}

	public Vocab getVocab() {
		if (vocabs == null)
			return null;

		if (_vocab == null) {
			_vocab = vocabs.stream().filter(e -> e.getVocabId() == vocabId).findFirst().get();
		}
		return _vocab;
	}

	public String getImgQuery() {
		return VocabUtils.encodeWords(this.getVocab().getWords()) + "+" + URLEncoding.encodeParam(this.vocabList.getTag());
	}

	public Integer getTestOrder() {
		return testOrder;
	}

	public void setTestOrder(Integer testOrder) {
		this.testOrder = testOrder;
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

	public List<Vocab> getVocabs() {
		return vocabs;
	}

	public void setVocabs(List<Vocab> vocabs) {
		this.vocabs = vocabs;
	}

	public int getVocabId() {
		return vocabId;
	}

	public void setVocabId(int vocabId) {
		this.vocabId = vocabId;
	}

	public VocabList getVocabList() {
		return vocabList;
	}

	public void setVocabList(VocabList vocabList) {
		this.vocabList = vocabList;
	}

	public List<SelectItem> getTestOrders() {
		return testOrders;
	}

	public void setTestOrders(List<SelectItem> testOrders) {
		this.testOrders = testOrders;
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
