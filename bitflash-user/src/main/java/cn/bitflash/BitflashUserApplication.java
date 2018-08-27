package cn.bitflash;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients(basePackages = {"cn.bitflash.sysutils","cn.bitflash.tradeutil","cn.bitflash.loginutil"})
@SpringBootApplication
@EnableEurekaClient
@ComponentScan
@EnableCircuitBreaker
@EnableRedisHttpSession
@MapperScan(basePackages = {"cn.bitflash.dao"})
public class BitflashUserApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BitflashUserApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BitflashUserApplication.class);
    }
}
