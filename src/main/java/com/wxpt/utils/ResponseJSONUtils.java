package com.wxpt.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.wxpt.base.spaypo.PayParameter;
import com.wxpt.controller.app.AppDispenseCouponController;

/**
 * 单例模式 <br>
 * 系统参数表
 * <p>
 * Copyright: Copyright (c) 2016-9-19 下午3:23:50
 * <p> 
 * Company: 善友汇网络科技股份有限公司
 * <p>
 * @author 姚成龙
 * @version 1.0.0
 */
public class ResponseJSONUtils{
	private static Logger log = Logger.getLogger(AppDispenseCouponController.class);
	private static Map<String,String> mapResults = new HashMap<String,String>();
	private static ResponseJSONUtils singletonUtils;

	private ResponseJSONUtils() {

	}
	public static ResponseJSONUtils instance() {
		if (CollectionUtils.isEmpty(mapResults)) {
			singletonUtils = new ResponseJSONUtils();
			Map<String,String> map = getMaps();
			mapResults.putAll(map);
			return singletonUtils;
		}
		return singletonUtils;
	}

	/**
	 * 
	 * 相当于getAll方法，从数据库中获取参数表
	 * @return list
	 */
	private static Map<String,String> getMaps() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<PayParameter> list = getPayParameter();
			if(list!=null&&list.size()>0){
				for (PayParameter payParameter : list) {
					map.put(payParameter.getCode(), payParameter.getValue());
				}
			}
			
		} catch (IOException e) {
			log.error("获取系统参数列表信息错误", e);
		}
		return map;
	}
	
	
	/**
	 * @param key  提示信息对应的key
	 * @param o    需要携带的数据对象
	 * @return
	 */
	public static ResponseJSON getCode(String key,Object o){
		instance();
		Map<String,Object> mapData = new HashMap<String,Object>();
		mapData.put("code", key);
		mapData.put("msg", mapResults.get(key));
		mapData.put("data", o);
		ResponseJSON json = new ResponseJSON();
		json.setStatus("true");
		json.setCode("200");
		json.setData(mapData);
		return json;
	}
	
	
	
	public static ResponseJSON getCode(String key){
		instance();
		Map<String,Object> mapData = new HashMap<String,Object>();
		ResponseJSON json = new ResponseJSON();
		mapData.put("code", key);
		mapData.put("msg", mapResults.get(key));
		mapData.put("data", "");
		json.setStatus("true");
		json.setCode("200");
		json.setData(mapData);
		json.setMsg("");
		return json;
	}
	
	/**
	 * 调用Service方法异常时候调用此方法
	 * @return
	 */
	public static ResponseJSON getErrorCode(){
		ResponseJSON json = new ResponseJSON();
		return json;
	}
	/**
	 * 获取参数表数据
	 * @return
	 * @throws IOException
	 */
	private static List<PayParameter> getPayParameter() throws IOException{
		String url = SyhDbServiceAddress.getURL(SyhDbServiceAddress.GET_PARAMETER);
		HttpResponse httpRespons = HttpRequester.sendPost(url);
		if (httpRespons != null) {
			String content = httpRespons.getContent();
			content = EnAndDeCodeUtils.decode(content);
			List<PayParameter> list = JSON.parseArray(content, PayParameter.class);
			return list;
		}
		return null;
	}
	
	
	
}
