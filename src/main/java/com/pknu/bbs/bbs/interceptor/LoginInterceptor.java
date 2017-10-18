package com.pknu.bbs.bbs.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	long mesure;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		mesure = System.currentTimeMillis();
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

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		System.out.println("메소드 걸린시간 -> " + ((System.currentTimeMillis() - mesure)/1000) + "초");
	}
	
}
