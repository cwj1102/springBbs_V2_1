package com.pknu.bbs.bbs.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*System.out.println("content : requestURI  "  + request.getRequestURI());
		System.out.println("content : requestURL  "  + request.getRequestURL());
		System.out.println("content : requestContextPath  "  + request.getContextPath());
		System.out.println("content : requestContextPath  "  + request.getServletPath());
		System.out.println("content : requestContextPath  "  + request.getPathInfo());
		System.out.println("content : requestContextPath  "  + request.getReader());
		System.out.println("content : requestContextPath  "  + request.getLocalAddr());
		System.out.println("content : requestContextPath  "  + request.getCookies());
		System.out.println("content : requestContextPath  "  + request.getQueryString());*/
		String reqFull = request.getRequestURI()+"?"+request.getQueryString();
		/*System.out.println(request.getRequestURI()+"?"+request.getQueryString());*/
		if((String)request.getSession().getAttribute("id")==null) {
			System.out.println(request.getServletPath());
			if(request.getServletPath().equals("/content.bbs")) {
				request.getSession().setAttribute("reqFull", reqFull);
				response.sendRedirect("/bbs/loginForm.bbs");
				return false;
			}
//			request.getSession().setAttribute("reqFull", reqFull);
			response.sendRedirect("/bbs/loginForm.bbs");
			return false;
		}
		return true;
	}
}
