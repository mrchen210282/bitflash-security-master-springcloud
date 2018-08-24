package cn.bitflash;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"cn.bitflash.tradeutil"})
@ComponentScan({"cn.bitflash.tradeutil"})
@EnableCircuitBreaker
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
@MapperScan("cn.bitflash.dao")
public class BitflashSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitflashSysApplication.class, args);
    }
}
