package com.wxpt.utils.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wxpt.utils.DateUtils;
import com.wxpt.utils.EnAndDeCodeUtils;
import com.wxpt.utils.IDGeneratorUtils;
import com.wxpt.utils.IpUtils;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-9-9 下午6:03:06
 * <p>
 * Company: 善友汇网络科技股份有限公司
 * <p>
 * 
 * @author yaochenglong
 * @version 1.0.0
 */
@Aspect
@Component
public class SystemLogAspect {

	// 注入Service用于把日志保存数据库
	@Resource
	// 这里我用resource注解，一般用的是@Autowired，他们的区别如有时间我会在后面的博客中来写
	private PayUserOperationLogService systemLogService;
	
	@Resource
	private PayUserSucceedOperationService payUserSucceedOperationService;

	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	// service层切点
	@Pointcut("execution (* com.wxpt.service..*.*(..))")
	public void serviceAspect() {
	}

	/**
	 * 前置通知 用于拦截service层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	/*@Before("serviceAspect()")
	public void doBefore(JoinPoint joinPoint) {
		System.out.println("==========执行service前置通知===============");
		if (logger.isInfoEnabled()) {
			logger.info("before " + joinPoint);
		}
	}*/

	// 配置service环绕通知,使用在方法aspect()上注册的切入点
	/*@Around("serviceAspect()")
	public void around(JoinPoint joinPoint) {
		System.out.println("==========开始执行service环绕通知===============");
		long start = System.currentTimeMillis();
		try {
			((ProceedingJoinPoint) joinPoint).proceed();
			long end = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
			}
			System.out.println("==========结束执行service环绕通知===============");
		}
		catch (Throwable e) {
			long end = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
			}
		}
	}*/

	/**
	 * 后置通知 用于拦截service层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@After("serviceAspect()")
	public void after(JoinPoint joinPoint) {

		
		 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		 HttpSession session = request.getSession();
		// 读取session中的用户
		PayUserVO payUser = (PayUserVO) session.getAttribute("user");
		 if(payUser==null){
			 return;
		 }
		 PayUserSucceedOperationVO log = new PayUserSucceedOperationVO();
		// 请求的IP
		log.setCareateBy(payUser!=null?payUser.getId():"");
		String ip =IpUtils.getIpAddr(request);
		try {

			String targetName = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] arguments = joinPoint.getArgs();
			Class<?> targetClass = Class.forName(targetName);
			Method[] methods = targetClass.getMethods();
			String operationType = "";
			String operationName = "";
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Class<?>[] clazzs = method.getParameterTypes();
					if (clazzs.length == arguments.length) {
						operationType = method.getAnnotation(Log.class).operationType();
						operationName = method.getAnnotation(Log.class).operationName();
						break;
					}
				}
			}
			// *========控制台输出=========*//
			System.out.println("=====service后置通知开始=====");
			System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			System.out.println("方法描述:" + operationName);
			System.out.println("请求人:" + payUser.getUserName());
			System.out.println("请求IP:" + ip);
			// *========数据库日志=========*//
			
			log.setId(IDGeneratorUtils.getUUID32());
			log.setDescripition(EnAndDeCodeUtils.encode(operationName));
			log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			log.setLogType("0");
			log.setRequestIp(ip);
			//log.setExceptionCode("");
			//log.setExceptionDetail("");
			log.setParams("");
			log.setCareateBy(payUser.getId()!=null?payUser.getId():"");
			log.setCreateDate(DateUtils.getCurrTime());
			log.setCategory(EnAndDeCodeUtils.encode(operationType));
			// 保存数据库TODO
			int account=payUserSucceedOperationService.saveOperation(log);
			logger.info("保存成功日志操作", account);
			System.out.println("=====service后置通知结束=====");
		}
		catch (Exception e) {
			// 记录本地异常日志
			logger.error("==后置通知异常==");
			logger.error("异常信息:{}", e.getMessage());
		}
	}

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning("serviceAspect()")
	public void afterReturn(JoinPoint joinPoint) {
		System.out.println("=====执行service后置返回通知=====");
		if (logger.isInfoEnabled()) {
			logger.info("afterReturn " + joinPoint);
		}
	}
	
	/**
	 * 异常通知 用于拦截记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		
		 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		 HttpSession session = request.getSession(); //读取session中的用户 User user
		/* = (User) session.getAttribute(WebConstants.CURRENT_USER); //获取请求ip*/
		 
		// 获取用户请求方法的参数并序列化为JSON格式字符串
		PayUserVO payUser = (PayUserVO) session.getAttribute("user");
		if(payUser==null){
			return;
		}
		PayUserOperationLogVO user = new PayUserOperationLogVO();
		user.setCareateBy(payUser.getId()!=null?payUser.getId():"");
		 String ip = IpUtils.getIpAddr(request);
	
		String params = "";
		/*if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += JsonUtil.getJsonStr(joinPoint.getArgs()[i]) + ";";
				String aa = (String) joinPoint.getArgs()[i];
				System.out.println(aa);
			}
		}*/
		try {

			String targetName = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] arguments = joinPoint.getArgs();
			Class<?> targetClass = Class.forName(targetName);
			Method[] methods = targetClass.getMethods();
			String operationType = "";
			String operationName = "";
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Class<?>[] clazzs = method.getParameterTypes();
					if (clazzs.length == arguments.length) {
						operationType = method.getAnnotation(Log.class).operationType();
						operationName = method.getAnnotation(Log.class).operationName();
						break;
					}
				}
			}
			/* ========控制台输出========= */
			System.out.println("=====异常通知开始=====");
			System.out.println("异常代码:" + e.getClass().getName());
			System.out.println("异常信息:" + e.getMessage());
			System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "."
			        + operationType);
			System.out.println("方法描述:" + operationName);
			System.out.println("请求人:" + user.getCareateBy());
			System.out.println("请求IP:" + ip);
			System.out.println("请求参数:" + params);
			/* ==========数据库日志========= */
			PayUserOperationLogVO log = new PayUserOperationLogVO();
			log.setId(IDGeneratorUtils.getUUID32());
			log.setDescription(operationName);
			log.setExceptionCode(e.getClass().getName());
			log.setLogType("1");
			log.setExceptionDetail(e.getMessage());
			log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			
			log.setParams(params);
			log.setCareateBy(payUser.getId()!=null?payUser.getId():"");
			log.setCreateDate(DateUtils.getCurrTime());
			log.setRequestIp(ip);
			log.setCategory(EnAndDeCodeUtils.encode(operationType));
			// 保存数据库
			systemLogService.addLog(log);
			System.out.println("=====异常通知结束=====");
		}
		catch (Exception ex) {
			// 记录本地异常日志
			logger.error("==异常通知异常==");
			logger.error("异常信息:{}", ex.getMessage());
		}
		/* ==========记录本地异常日志========== */
		/*logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass()
		        .getName(), e.getMessage(), params);*/

	}
}