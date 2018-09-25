package com.chat.springboot.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * <pre>
 * 功       能: 配置定时任务(定期从redis中同步日志到mongdodb)
 * 涉及版本: V3.0.0 
 * 创  建  者: yangyiwei
 * 日       期: 2018年3月29日 下午1:58:59
 * Q    Q: 2873824885
 * </pre>
 */
//@Component
public class AsynLogFromRedis {

	private final static Logger logger = Logger.getLogger(AsynLogFromRedis.class);
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * <pre>
	 * 功       能: 每隔10分钟 执行日志定期同步到mongodb
	 * 涉及版本: V3.0.0 
	 * 创  建  者: yangyiwei
	 * 日       期: 2018年3月29日 下午2:01:26
	 * Q    Q: 2873824885
	 * </pre>
	 */
	@Scheduled(fixedRate = 1000 * 60 * 3)
	// @Scheduled(cron = "0 */1 * * * ?")
	public void saySpringBoot() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("当前时间:" + df.format(new Date()) + "..开始执行日志同步任务......");
		long startTime = System.currentTimeMillis();
		Jedis jedis = jedisPool.getResource();
		List<String> logLists = jedis.lrange("chat-web-log", 0, 499);// 截取最前面的99条日志
		jedis.ltrim("chat-web-log", 500, -1);// 只保留99条记录之后的数据
		List<JSONObject> dealList = new ArrayList<JSONObject>();
		for (String item : logLists) {
			JSONObject jsonItem = JSONObject.parseObject(item);
			jsonItem.remove("tags");
			jsonItem.remove("type");
			jsonItem.remove("path");
			try {
				jsonItem.put("create_time", DateUtils.formatUTC(jsonItem.getString("@timestamp")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonItem.remove("@timestamp");
			dealList.add(jsonItem);
		}
		mongoTemplate.insert(dealList, "log"); // 批量写入
		long endTime = System.currentTimeMillis(); // 获取结束时间
		logger.info("本次执行日志同步任务....耗时" + (endTime - startTime) + "毫秒.....");
		jedis.close();
	}
}
