package com.sunno.viewservice;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import javax.servlet.http.Cookie;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;

@EnableEurekaClient
@SpringBootApplication
public class ViewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViewServiceApplication.class, args);

	}

}
