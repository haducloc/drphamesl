package com.drphamesl.utils;

import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.models.SelectItem;
import com.appslandia.common.models.SelectItemImpl;
import com.appslandia.plum.base.Resources;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class VocabOrders {

	public static final int RANDOM_ORDER = 1;
	public static final int ALPHABETICAL = 2;

	public static List<SelectItem> createList(Resources resources) {
		List<SelectItem> list = new ArrayList<>();

		list.add(new SelectItemImpl(RANDOM_ORDER, resources.get("vocabOrders.random_order")));
		list.add(new SelectItemImpl(ALPHABETICAL, resources.get("vocabOrders.alphabetical")));
		return list;
	}

	public static int toVocabOrder(Integer order) {
		if (order != null) {
			if (order == RANDOM_ORDER || order == ALPHABETICAL) {
				return order;
			}
		}
		return RANDOM_ORDER;
	}
}
