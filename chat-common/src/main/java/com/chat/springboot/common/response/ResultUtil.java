package com.chat.springboot.common.response;

import com.chat.springboot.common.response.ResultStatus;

/**
 * 返回一个result
 * @author yangyiwei
 * @date 2018年7月3日
 * @time 下午5:39:10
 */
public class ResultUtil {

	
	/**
	 * 设置结果集，带返回对象
	 * @param resultStatus 状态码
	 * @param data 对象
	 * @return
	 */
	public static <T> ResponseResult<T> setResult(ResultStatus resultStatus, T data) {
		return new ResponseResult<T>(resultStatus, data);
	}
	
	/**
	 * 设置结果集，不带返回对象
	 * @param resultStatus 状态码
	 * @return
	 */
	public static ResponseResult<?> setResult(ResultStatus resultStatus) {
		ResponseResult<?> result = new ResponseResult<>();
		result.setCode(resultStatus);
		return result;
	}
	
	/**
	 * 设置出错结果集合
	 * @param resultStatus 状态码
	 * @param errorDetail 对出错情况的补充补充
	 * @return
	 */
	public static ResponseResult<String> setResult(ResultStatus resultStatus, String errorDetail) {
		ResponseResult<String> result = new ResponseResult<String>();
		result.setCode(resultStatus).setData(errorDetail);
		return result;
	}
	
	
}
