package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.NotBind;
import com.appslandia.common.base.Out;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.models.SelectItem;
import com.appslandia.common.validators.ValidInts;
import com.appslandia.plum.base.PagerModel;
import com.drphamesl.entities.VocabList;
import com.drphamesl.utils.ShareTypes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ManageVocabListIndexModel {

	@Bind(fmt = Formatter.TAG)
	private String tag;

	@ValidInts({ ShareTypes.OFF, ShareTypes.ON_PRIVATE, ShareTypes.ON_PUBLIC })
	private Integer shareType;

	private Integer pageIndex;

	@NotBind
	private Out<Integer> recordCount = new Out<>();

	@NotBind
	private PagerModel pagerModel;

	@NotBind
	private List<VocabList> vocabLists;

	@NotBind
	private String robots;

	@NotBind
	private List<SelectItem> shareTypes;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Out<Integer> getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Out<Integer> recordCount) {
		this.recordCount = recordCount;
	}

	public PagerModel getPagerModel() {
		return pagerModel;
	}

	public void setPagerModel(PagerModel pagerModel) {
		this.pagerModel = pagerModel;
	}

	public List<VocabList> getVocabLists() {
		return vocabLists;
	}

	public void setVocabLists(List<VocabList> vocabLists) {
		this.vocabLists = vocabLists;
	}

	public String getRobots() {
		return robots;
	}

	public void setRobots(String robots) {
		this.robots = robots;
	}

	public List<SelectItem> getShareTypes() {
		return shareTypes;
	}

	public void setShareTypes(List<SelectItem> shareTypes) {
		this.shareTypes = shareTypes;
	}
}
