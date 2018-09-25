package com.chat.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class }) 																										// 启动类
@MapperScan("com.chat.springboot.dao") // 配置mybatis-dao层扫描
@ComponentScan(basePackages = { "com.chat.springboot" }) // 基本扫包配置
@EnableScheduling // 定时任务配置开启
@EnableAsync // 开启异步任务
// @EnableEurekaClient//服务注册
// @EnableJms // 开启JMS消息服务
@ServletComponentScan
public class SpringBootChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootChatApplication.class, args);
	}
}
