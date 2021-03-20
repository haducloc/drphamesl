package com.drphamesl.models;

import java.util.List;

import com.appslandia.common.base.NotBind;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MyTagsModel {

	@NotBind
	private List<String> mytags;

	public List<String> getMytags() {
		return mytags;
	}

	public void setMytags(List<String> mytags) {
		this.mytags = mytags;
	}
}
