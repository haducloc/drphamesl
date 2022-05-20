package com.drphamesl.beans;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;
import jakarta.transaction.UserTransaction;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class UserTransactionProducer {

	@Produces
	@Alternative
	@Dependent
	@Resource(mappedName = "java:jboss/UserTransaction")
	protected UserTransaction tx;
}
