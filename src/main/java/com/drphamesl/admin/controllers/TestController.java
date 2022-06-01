package com.drphamesl.admin.controllers;

import java.time.OffsetDateTime;

import com.appslandia.plum.base.Controller;
import com.appslandia.plum.base.EnableAsync;
import com.appslandia.plum.base.HttpGet;
import com.appslandia.plum.base.Modules;
import com.drphamesl.entities.Account;

import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Controller(module = Modules.ADMIN)
public class TestController {

	@EnableAsync
	@HttpGet
	public Account index() {
		Account acc = new Account();
		acc.setAccountId(1);
		acc.setEmail("test@gmail.com");
		acc.setFirstName("Loc");
		acc.setLastName("Ha");
		acc.setStatus(1);
		acc.setTimeCreated(OffsetDateTime.now());

		return acc;
	}
}
