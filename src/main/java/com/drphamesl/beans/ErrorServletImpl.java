package com.drphamesl.beans;

import java.io.IOException;

import com.appslandia.plum.base.ErrorServlet;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.RequestContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@WebServlet(name = "ErrorServlet", urlPatterns = "/error")
public class ErrorServletImpl extends ErrorServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getErrorJsp(HttpServletRequest request, RequestContext requestContext) {

		// ADMIN
		if (Modules.ADMIN.equalsIgnoreCase(requestContext.getModule())) {
			return "/error_admin.jsp";
		}

		return "/error.jsp";
	}

	@Override
	protected void writeErrorDev(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		writeErrorProd(request, response);
	}
}
