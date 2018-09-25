package com.chat.springboot.common.request;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@Component
public class HttpRequestUtil {

	@Autowired
	private RestTemplate restTemplate;// 服务调用
	
	/**
	 *发送JSON请求 
	 */
	public JSONObject sendJSONRequest(String url, JSONObject param) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> formEntity = new HttpEntity<String>(param.toString(), headers);
		JSONObject result = restTemplate.postForObject(url, formEntity, JSONObject.class);
		return result;
	}
	
	/**
	 * 发送普通post请求
	 */
	public String sendPostRequest(String url, Map<String, String> param) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

		for (Map.Entry<String, String> item : param.entrySet()) { // 转换参数
			map.add(item.getKey(), item.getValue());
		}
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String response = restTemplate.postForObject(url, request, String.class);
		return response;
	}
	
	/*public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("name", "yangyiwei");
		map.put("nage", "50");
		for(Map.Entry<String, String> item : map.entrySet()) {
			System.out.println(item.getValue());
		}
	}*/
}
