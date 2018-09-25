package com.chat.springboot.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.chat.springboot.common.ChatCode;
import com.chat.springboot.common.ResultMap;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

@ServerEndpoint("/webSocket/{uuid}")
@Component
@Slf4j
public class WebSocketServer {

	private static JedisPool jedisPool;

	private Session session;
	// 当前用户
	private String currentUserName;

	// 匹配用户
	private String matchUserName;

	public WebSocketServer() {
	
	}

	@Autowired
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 连接建立调用
	 *
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("uuid") String uuid) {
		this.session = session;
		this.currentUserName = uuid;// 设置当前用户
		Jedis jedis = jedisPool.getResource();
		Pipeline pipeline = jedis.pipelined(); // redis管道技术
		pipeline.sadd("online_people", uuid);// 添加在线人数
		pipeline.incr("online_count");// 人数++
	//	pipeline.hset("match_peer", uuid, "noPeer");// 匹配的伙伴 一开始 为自身 + nopeer
		List<Object> list = pipeline.syncAndReturnAll();// 发送redis管道
		jedis.close();
		AllWebSocket.set.add(this);
		try {
//			sendMessage("收到服务器的消息：连接成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接关闭调用
	 */
	@OnClose
	public void onClose() {
		log.info(currentUserName + "用户退出了聊天.....");
		Jedis jedis = jedisPool.getResource();
		Transaction transaction = jedis.multi();
		transaction.decr("online_count");// 人数--
		transaction.srem("online_people", currentUserName);// 移除用户
		// 判定一下是否匹配过用户，如果有，则通知对方用户 该用户下线，并且解除两边的匹配关系
		if (matchUserName != null) { // 解除用户匹配关系
			transaction.hdel("match_peer", currentUserName);
			transaction.hdel("match_peer", matchUserName);

			// 通知对方用户我方已经下线
			try {
				for (WebSocketServer item : AllWebSocket.set) {
					if (matchUserName.equals(item.getCurrentUserName())) {
						item.setMatchUserName(null);
						item.sendMessage(ResultMap.result(true,3,"remove_random_friend"));
					}
				}
			} catch (Exception e) { // 如果浏览器被关闭了 会走到这里发生异常 无需关注
				e.printStackTrace();
			}

		}
		transaction.exec();
		jedis.disconnect();
		AllWebSocket.set.remove(this);
	}

	/*
	 *
	 * A匹配B B匹配C C匹配 A
	 *
	 *
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		if (matchUserName == null) { // 匹配用户为空，则进入匹配
			Jedis jedis = jedisPool.getResource();
			jedis.lpush("match_people_line", currentUserName);// 将自身写入匹配队列
			log.info("用户 " + currentUserName + " 加入了匹配队列.....");
			Thread.sleep(5000);
			Transaction transaction = jedis.multi();//开启redis事务
			transaction.rpop("match_people_line");
			transaction.rpop("match_people_line");
			List<Object> resultList =  transaction.exec();
			if (resultList.get(0) != null && resultList.get(1) != null) { //成功匹配
				String onePeople = resultList.get(0).toString();
				String twoPeople = resultList.get(1).toString();
				jedis.hset("match_peer", onePeople, twoPeople);// 设置配对关系
				jedis.hset("match_peer", twoPeople, onePeople);
			}
			//此处停顿3S.获取自身匹配结果
			Thread.sleep(2000);
			String matchPeer = jedis.hget("match_peer", currentUserName);
			if (matchPeer != null) {// 查询是否被匹配过
				log.info(currentUserName + "用户和" + matchPeer + "用户匹配成功了......");
				matchUserName = matchPeer;
				sendMessage(ResultMap.result(true,1,matchPeer));
			} else {
				log.info("用户匹配失败......");
				sendMessage(ResultMap.result(false,1,"未匹配到合适用户,请稍后再试"));
			}
			
			jedis.disconnect();// 关闭连接
		} else {
			if (message.equals(ChatCode.REMOVE.getMessage())){//如果得到的消息是解除匹配关系
				log.info("收到"+currentUserName+"解除"+matchUserName+"的请求");
				Jedis jedis = jedisPool.getResource();
				Transaction transaction = jedis.multi();//开启事务
				transaction.del(currentUserName);// 删除标志位
				transaction.del(matchUserName);
				transaction.hdel("match_peer",currentUserName);//删除自己方的信息
				transaction.hdel("match_peer",matchUserName);
				transaction.exec();
				jedis.disconnect();
				try {
					for (WebSocketServer item : AllWebSocket.set) {
						if (matchUserName.equals(item.getCurrentUserName())) {
							item.setMatchUserName(null);
							item.sendMessage(ResultMap.result(true,3,message));
						}
					}
				} catch (Exception e) { // 如果浏览器被关闭了 会走到这里发生异常 无需关注
					e.printStackTrace();
				}
			}else {
				sendToMatchUser(message,2, matchUserName);
			}
			// 发送指定消息过去
//			sendMessage(ResultMap.result(true,2,message).toString());
		}

	}

	/**
	 * 群发所有消息
	 *
	 * @param message
	 */
	public void sendAllMessage(String message) {
		for (WebSocketServer item : AllWebSocket.set) {
			try {
				item.sendMessage(currentUserName + "用户对你说: " + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送指定消息到用户
	 *
	 * @param message
	 * @param matchUserName
	 * @throws IOException
	 */
	public void sendToMatchUser(String message, int type, String matchUserName) throws IOException {
		for (WebSocketServer item : AllWebSocket.set) {

			if (item.getCurrentUserName().equals(matchUserName)) {
				Map<String, String> backMsg = new HashMap<>();
				backMsg.put("user", currentUserName);
				backMsg.put("message", message);
				log.info(message);
				item.sendMessage(ResultMap.result(true, type, JSON.toJSONString(backMsg)));
			}

		}
	}

	/*
	 * @RequestMapping(value = "/pushVideoListToWeb", method =
	 * RequestMethod.POST, consumes = "application/json")
	 * 
	 * @ResponseBody public Map<String, Object> pushVideoListToWeb(@RequestBody
	 * Map<String, Object> param) { Map<String, Object> result = new
	 * HashMap<String, Object>(); try {
	 * WebSocketServer.sendInfo("有新客户呼入,sltAccountId:" +
	 * param.get("sltAccountId")); result.put("operationResult", true); } catch
	 * (IOException e) { result.put("operationResult", true); } return result; }
	 */

	/**
	 * 发生错误
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}

	/**
	 * 调用发送方法
	 * 
	 * @param message
	 * @throws IOException
	 */
	private void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public String getMatchUserName() {
		return matchUserName;
	}

	public void setMatchUserName(String matchUserName) {
		this.matchUserName = matchUserName;
	}

	/**
	 * 
	 * 
	 * if (matchUserName == null) { // 匹配用户为空，则进入匹配 boolean isMatch = false;
	 * Jedis jedis = jedisPool.getResource(); String peer =
	 * jedis.srandmember("no_match_people");// 随机弹出一个未匹配的玩家 while
	 * (peer.equals(currentUserName)) {// 如果匹配到自身，则继续弹出 peer =
	 * jedis.srandmember("no_match_people"); } log.info("自己的id是：" +
	 * currentUserName + "弹出的匹配小伙伴是: " + peer); jedis.hset("match_peer",
	 * currentUserName, peer);// 暂定两边匹配成功 for (int i = 0; i < 10; i++) {
	 * log.info("已经进入匹配模式.....匹配次数...." + i + "次"); if
	 * (jedis.hget("match_peer", peer).equals(currentUserName)) { //
	 * 如果两边用户匹配的同一个人 isMatch = true; break; } try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { e.printStackTrace(); } } if (isMatch) {
	 * jedis.srem("no_match_people", currentUserName);// 将当前用过户从未匹配set中弹出
	 * matchUserName = peer;// 匹配用户 sendMessage("匹配成功，可以开始聊天了.....匹配到的用户id是:" +
	 * peer); } else { jedis.hset("match_peer", currentUserName, "noPeer");//
	 * 解除两边的匹配模式 sendMessage("未找到合适的人选,请稍后再试....."); } jedis.disconnect();
	 * 
	 * // 此处匹配十秒 } else { // 发送指定消息过去 sendToMatchUser(message, matchUserName); }
	 * 
	 */
	
	/**
	 * 老的匹配算法
	 * @param message
	 * @param session
	 * @throws Exception
	 */
	public void oldMatchWay(String message, Session session) throws Exception {
		if (matchUserName == null) { // 匹配用户为空，则进入匹配
			Jedis jedis = jedisPool.getResource();
			jedis.sadd("online_match_people", currentUserName);// 将自身写入匹配队列
			Thread.sleep(5000);
			String peer = jedis.spop("online_match_people");// 从set中随机弹出一个元素
			if (peer.equals(currentUserName)) {// 如果匹配到自身，则直接返回
				sendMessage(ResultMap.result(false,1,"匹配到了自己"));
				return;
			}
			log.info("自己的id是：" + currentUserName + "弹出的匹配小伙伴是: " + peer);
			// 成功弹出匹配的队友以后。用redis setnx指令 。迅速锁定。防止其他线程继续匹配
			Transaction transaction = jedis.multi();// 开始redis事务
			transaction.setnx(currentUserName, "1");// 设置自己标志
			transaction.setnx(peer, "1");// 设置对方标志
			transaction.expire(currentUserName, 10);
			transaction.expire(peer, 10);
			List<Object> result = transaction.exec();
			if (result.get(0).toString().equals("1") && result.get(1).toString().equals("1")) { // 此处说明匹配成功
				log.info("有玩家成功通过两个标志位匹配了.....");
				jedis.hset("match_peer", currentUserName, peer);// 设置配对关系
				jedis.hset("match_peer", peer, currentUserName);
				matchUserName = peer;// 赋值小伙伴
				sendMessage(ResultMap.result(true,1,peer));
			} else { // 配对自身指定玩家失败
				Thread.sleep(3000); // 等待匹配成功的结果
				String matchPeer = jedis.hget("match_peer", currentUserName);
				if (matchPeer != null) {// 查询是否被匹配过
					log.info("有玩家被动匹配了.....");
					matchUserName = matchPeer;
					sendMessage(ResultMap.result(true,1,peer));
				} else {
					sendMessage(ResultMap.result(false,1,null));
				}
			}
			jedis.disconnect();// 关闭连接
		}
	}
	

}
