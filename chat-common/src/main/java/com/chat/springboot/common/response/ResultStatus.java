package com.chat.springboot.common.response;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * <pre>
 * 功       能:返回状态枚举类 
 * 涉及版本: V3.0.0 
 * 创  建  者: yangyiwei
 * 日       期: 2018年3月9日 下午1:12:21
 * Q    Q: 2873824885
 * </pre>
 */
public enum ResultStatus {
	
	/**
	 * 请求结果
	 */
	UNKNOW_ERROR("出现未知错误", -1), //系统抛出异常
	SUCCESS("请求成功", 0),  //请求成功
	DEFINE_ERROR("自定义错误", -2), 
	
	/**基本参数校验**/
	LACK_PARAM("缺乏基本参数",  InnerCode.getIncrmentI()),
	
	/**基本枚举类型 插入失败 更新失败 添加失败 未查询到匹配数据**/
	DATA_NOT_FIND("数据没有找到",  InnerCode.getIncrmentI()), 
	UPDATE_FAIL("未更新到匹配记录", InnerCode.getIncrmentI()),
	INSERT_FAIL("插入数据失败",  InnerCode.getIncrmentI()),
	DELETE_FAIL("删除数据失败",  InnerCode.getIncrmentI()),
	TRANSACTION_FAIL("多表操作失败", InnerCode.getIncrmentI()),

	/**用户模块枚举类型**/
	USER_IS_REGISTER("用户已经被注册过了",  InnerCode.getIncrmentI()),
	LOGIN_FAIL("账号或者密码错误",  InnerCode.getIncrmentI()),
	USER_IS_NULL("请登录后再试",  InnerCode.getIncrmentI()),
	USER_NOT_EXIST("账号不存在", InnerCode.getIncrmentI()),
	
	/**好友模块枚举类型**/
	CAN_NOT_ADD_SELF("不能添加自身为好友", InnerCode.getIncrmentI()),
	CAN_NOT_ADD_REPEAT_FRIEND("不可重复添加好友", InnerCode.getIncrmentI()),

	/**
	 * 令牌校验
	 */
	TOKEN_IS_NULL("请求令牌为空,请登录后再试", InnerCode.getIncrmentI()),
	TOKEN_IS_INVALID("请求令牌无效或者过期,请登录后再试", InnerCode.getIncrmentI()),

	/**
	 * 调用第三方服务
	 */
	INVOKE_REMOTOE_FAIL("调用远程服务失败!", InnerCode.getIncrmentI()),
	MICRO_SERVICE_DIED("暂时没有可用的服务", InnerCode.getIncrmentI()),
	;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 状态码
	 */
	private Integer code;
	
	
	static class InnerCode { //内部定义状态码自增
		public static AtomicInteger i = new AtomicInteger(1);
		
		/**
		 * juc下的 atomic cas自增长 i 
		 * @return
		 */
		public static int getIncrmentI() {
			return i.getAndIncrement();
		}
	}
	

	ResultStatus(String message, Integer code) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public static void showAllType() {
		for (ResultStatus e : ResultStatus.values()) {
			System.out.println(e.getMessage() + "  " + e.getCode());
		}
	}

	
	/*  public static void main(String[] args) {
		for (ResultStatus resultStatus : ResultStatus.values())  {
			System.out.println(resultStatus.getCode() + " 消息 " + resultStatus.message );
		}
	  }*/
	 
}
