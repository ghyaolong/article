package com.wxpt.utils.intercecptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wxpt.utils.ContextHolderUtils;

/**
 * 拦截器 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-9-24 下午7:59:28
 * <p>
 * Company: 善友汇网络科技股份有限公司
 * <p>
 * 
 * @author 姚成龙
 * @version 1.0.0
 */
public class ValidTokenInterceptor implements HandlerInterceptor {

	private List<String> excludeUrls;

	/**
	 * 获得请求路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

	/**
	 * 在controller前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestPath = ValidTokenInterceptor.getRequestPath(request);// 用户访问的资源地址

		HttpSession session = ContextHolderUtils.getSession();
		if (excludeUrls.contains(requestPath)) {
			return true;
		} else {
			// 验证token
			return true;
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView e) throws Exception {
		// TODO Auto-generated method stub
	}

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}
