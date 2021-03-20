package com.drphamesl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.appslandia.common.models.SelectItem;
import com.appslandia.common.models.SelectItemImpl;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.plum.base.Resources;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class NextTypes {

	public static List<SelectItem> createList(Resources resources) {
		List<SelectItem> list = new ArrayList<>();
		list.add(new SelectItemImpl("na", resources.get("nextTypes.next_na")));

		list.add(new SelectItemImpl("5s", resources.get("nextTypes.next_5s")));
		list.add(new SelectItemImpl("7s", resources.get("nextTypes.next_7s")));
		list.add(new SelectItemImpl("10s", resources.get("nextTypes.next_10s")));
		list.add(new SelectItemImpl("15s", resources.get("nextTypes.next_15s")));
		list.add(new SelectItemImpl("30s", resources.get("nextTypes.next_30s")));
		list.add(new SelectItemImpl("1m", resources.get("nextTypes.next_1m")));
		list.add(new SelectItemImpl("5m", resources.get("nextTypes.next_5m")));

		return list;
	}

	static final Set<String> NEXT_TYPES = CollectionUtils.unmodifiableSet("na", "5s", "7s", "10s", "15s", "30s", "1m", "5m");

	public static String toNextType(String type) {
		if (type == null)
			return "10s";

		if (NEXT_TYPES.contains(type))
			return type;

		return "10s";
	}

	public static long toNextInMs(String type) {
		if ("na".equalsIgnoreCase(type))
			return 0;

		return DateUtils.translateToMs(type);
	}
}
