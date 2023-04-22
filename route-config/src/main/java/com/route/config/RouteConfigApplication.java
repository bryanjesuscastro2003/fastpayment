package com.route.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RouteConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouteConfigApplication.class, args);
	}

}
