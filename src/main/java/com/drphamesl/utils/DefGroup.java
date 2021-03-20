package com.drphamesl.utils;

import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.ToStringBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DefGroup {

	private String clazz;
	private String def;
	private List<String> sents = new ArrayList<String>(3);

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public List<String> getSents() {
		return sents;
	}

	public void setSents(List<String> sents) {
		this.sents = sents;
	}

	@Override
	public String toString() {
		return new ToStringBuilder().toString(this);
	}
}
