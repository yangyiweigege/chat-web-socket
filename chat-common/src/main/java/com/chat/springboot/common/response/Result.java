package com.chat.springboot.common.response;

import com.chat.springboot.common.response.ResultStatus;

/**
 * <pre>
 * 功       能:用于分析返回结果 http
 * 涉及版本: V3.0.0 
 * 创  建  者: yangyiwei
 * 日       期: 2018年3月9日 下午1:01:09
 * Q    Q: 2873824885
 * </pre>
 */
public class Result<T> {

	/**
	 * 是否成功
	 */
	private boolean isSuccess;

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 返回消息
	 */
	private String message;

	/**
	 * 具体内容
	 */
	private T data;

	/**
	 * 设置状态码的构造函数，无返回对象
	 * 
	 * @param resultStatus
	 */
	public Result(ResultStatus resultStatus) {
		this.isSuccess = codeConvertIsSuccess(resultStatus.getCode());
		this.code = resultStatus.getCode();
		this.message = resultStatus.getMessage();
	}

	/**
	 * 设置状态码的构造函数，返回对象
	 * 
	 * @param resultStatus
	 */
	public Result(ResultStatus resultStatus, T data) {
		this.isSuccess = codeConvertIsSuccess(resultStatus.getCode());
		this.code = resultStatus.getCode();
		this.message = resultStatus.getMessage();
		this.data = data;
	}

	/**
	 * 空构造
	 */
	public Result() {

	}

	/**
	 * 根据code转换是否成功
	 * 
	 * @param code
	 * @return
	 */
	private boolean codeConvertIsSuccess(Integer code) {
		boolean isSuccess = true;
		if (code != 0) {
			isSuccess = false;
		}
		return isSuccess;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	public Result<T> setCode(ResultStatus resultStatus) {
		this.code = resultStatus.getCode();
		this.message = resultStatus.getMessage();
		return this;
	}

	public Result<T> returnView(ResultStatus resultStatus) {
		this.isSuccess = codeConvertIsSuccess(resultStatus.getCode());
		this.message = resultStatus.getMessage();
		return this;
	}

	public Result<T> returnView(ResultStatus resultStatus,T data) {
		this.isSuccess = codeConvertIsSuccess(resultStatus.getCode());
		this.message = resultStatus.getMessage();
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return "Result [isSuccess=" + isSuccess + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}

}
