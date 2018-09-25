package com.chat.springboot.common.jms;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;  

/**
 * 消息监听消费者
 * @author yangyiwei
 * @date 2018年6月12日
 * @time 上午10:07:21
 */
//@Component  
public class LogConsumer {  
  
	private final static Logger logger = Logger.getLogger(LogConsumer.class);
  
	//此配置默认监听队列模式
    @JmsListener(destination = QueueName.LOG_QUEUE)  
    public void receivedQueue(String msg) {  
    	logger.info("Has received from " + QueueName.LOG_QUEUE + ", msg: " + msg);  
    }  
}  
