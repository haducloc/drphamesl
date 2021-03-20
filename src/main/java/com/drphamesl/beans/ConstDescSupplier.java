package com.drphamesl.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.appslandia.common.base.ConstDesc;
import com.appslandia.common.cdi.CDISupplier;
import com.appslandia.common.cdi.Supplier;
import com.drphamesl.utils.ShareTypes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Supplier(ConstDesc.class)
public class ConstDescSupplier implements CDISupplier {

	@Override
	public List<Class<?>> get() {
		List<Class<?>> list = new ArrayList<>();

		list.add(ShareTypes.class);
		return list;
	}
}
