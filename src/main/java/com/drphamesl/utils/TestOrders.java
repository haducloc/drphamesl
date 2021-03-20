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
public class TestOrders {

	public static final int RANDOM_ORDER = 1;
	public static final int PRE_DEFINED = 2;

	public static List<SelectItem> createList(Resources resources) {
		List<SelectItem> list = new ArrayList<>();

		list.add(new SelectItemImpl(RANDOM_ORDER, resources.get("testOrders.random_order")));
		list.add(new SelectItemImpl(PRE_DEFINED, resources.get("testOrders.pre_defined")));

		return list;
	}

	public static int toTestOrder(Integer order) {
		if (order != null) {
			if (order == RANDOM_ORDER || order == PRE_DEFINED) {
				return order;
			}
		}
		return RANDOM_ORDER;
	}
}
