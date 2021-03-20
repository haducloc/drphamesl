package com.drphamesl.utils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.TagUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PreTagUtils {

	public static final Set<String> PRE_TAGS = CollectionUtils.unmodifiableSet(new LinkedHashSet<>(), TagUtils.UNSORTED_TAG, TagUtils.TBD_TAG);

	public static final Map<String, Integer> PRE_TAGS_ORDERS = CollectionUtils.unmodifiableMap(TagUtils.UNSORTED_TAG, 0, TagUtils.TBD_TAG, 1);
}
