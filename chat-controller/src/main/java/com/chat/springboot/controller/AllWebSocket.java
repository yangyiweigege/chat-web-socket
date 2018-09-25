package com.chat.springboot.controller;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 存储所有websocket连接
 * @author yangyiwei
 * @date 2018年6月19日
 * @time 下午4:34:45
 */
public class AllWebSocket {
	public static CopyOnWriteArraySet<WebSocketServer> set = new CopyOnWriteArraySet<WebSocketServer>();
}
