package com.example.cardApi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.cardApi.mapper")
public class cardApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(cardApiApplication.class, args);
		System.out.println(">>> 应用启动成功！Mapper 雷达已开启。");
	}

}
