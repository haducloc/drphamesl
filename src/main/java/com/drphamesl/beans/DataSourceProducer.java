package com.drphamesl.beans;

import javax.sql.DataSource;

import com.drphamesl.utils.DBUtils;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class DataSourceProducer {

	@Produces
	@Dependent
	@Resource(mappedName = DBUtils.DS_NAME)
	protected DataSource ds;
}
