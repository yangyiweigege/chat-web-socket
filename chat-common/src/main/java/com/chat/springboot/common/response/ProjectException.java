package com.chat.springboot.common.response;

import com.chat.springboot.common.response.ResultStatus;

/**
 * <pre>
 * 功       能: 自定义项目中出现的异常
 * 涉及版本: V3.0.0 
 * 创  建  者: yangyiwei
 * 日       期: 2018年3月9日 下午2:19:46
 * Q    Q: 2873824885
 * </pre>
 */
public class ProjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 错误状态
	 */
	private Integer code;

	/**
	 * 状态码
	 */
	private ResultStatus resultStatus;

	/**
	 * 具体原因
	 */
	private String detailMsg;

	public ProjectException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public ProjectException(ResultStatus resultStatus) {
		super(resultStatus.getMessage());
		this.code = resultStatus.getCode();
		this.resultStatus = resultStatus;
	}

	public ProjectException(ResultStatus resultStatus, String detailMsg) {
		super(resultStatus.getMessage());
		this.code = resultStatus.getCode();
		this.resultStatus = resultStatus;
		this.detailMsg = detailMsg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public ResultStatus getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(ResultStatus resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

}
