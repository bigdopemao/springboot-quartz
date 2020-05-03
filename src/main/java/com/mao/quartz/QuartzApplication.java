package com.mao.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * java -jar springboot-quartz.jar --server.port=8081
 * java -jar springboot-quartz.jar --server.port=8082
 * @author bigdope
 * @create 2020-01-07
 **/
@SpringBootApplication
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }

}
