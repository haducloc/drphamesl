package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.Bind;
import com.appslandia.common.base.NotBind;
import com.appslandia.common.base.Out;
import com.appslandia.plum.base.PagerModel;
import com.drphamesl.entities.Vocab;
import com.drphamesl.formatters.Formatters;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ManageVocabIndexModel {

	@Bind(fmt = Formatters.QUERY_TEXT)
	private String query;

	private Integer pageIndex;

	@NotBind
	private Out<Integer> recordCount = new Out<>();

	@NotBind
	private PagerModel pagerModel;

	@NotBind
	private List<Vocab> vocabs;

	public boolean isQueryTag() {
		return this.query != null && this.query.startsWith("#");
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public List<Vocab> getVocabs() {
		return vocabs;
	}

	public void setVocabs(List<Vocab> vocabs) {
		this.vocabs = vocabs;
	}
}
