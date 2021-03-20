package com.drphamesl.utils;

import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.ConstDesc;
import com.appslandia.common.models.SelectItem;
import com.appslandia.common.models.SelectItemImpl;
import com.appslandia.plum.base.Resources;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ShareTypes {

	@ConstDesc
	public static final int OFF = 0;

	@ConstDesc
	public static final int ON_PRIVATE = 1;

	@ConstDesc
	public static final int ON_PUBLIC = 2;

	public static List<SelectItem> createList(Resources resources, boolean emptyOption) {
		List<SelectItem> list = new ArrayList<>();

		if (emptyOption) {
			list.add(SelectItemImpl.EMPTY);
		}

		list.add(new SelectItemImpl(OFF, resources.get("shareTypes.off")));
		list.add(new SelectItemImpl(ON_PRIVATE, resources.get("shareTypes.on_private")));
		list.add(new SelectItemImpl(ON_PUBLIC, resources.get("shareTypes.on_public")));

		return list;
	}
}
