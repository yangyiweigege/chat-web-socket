package com.chat.springboot.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

/**
 * 返回页面的数据包装类
 */
public class ResultMap {
	
	
	public static  Map<String,Object> result(boolean status,String msg,PageInfo<?> page) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("msg", msg);
		map.put("total", page.getTotal());
		map.put("rows", page.getList());
		map.put("pageSize", page.getPageSize());
		map.put("pageNum", page.getPageNum());
		return map;
	}

	public static String result(boolean status,int type,String msg) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("type",type);
		map.put("backMsg", msg);
		return JSON.toJSONString(map);
	}
	
	public static  Map<String,Object> result(boolean status,String msg) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("msg", msg);
		return map;
	}
	
	public static  Map<String,Object> result(boolean status,String msg,List<?> list) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("msg", msg);
		map.put("rows", list);
		return map;
	}
	
	public static  Map<String,Object> result(boolean status,String msg,Object object) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("msg", msg);
		map.put("object", object);
		return map;
	}
	
}
