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



@EnableFeignClients(basePackages = {"cn.bitflash.userutil","cn.bitflash.tradeutil"})
@EnableCircuitBreaker
@EnableEurekaClient
@MapperScan(basePackages = {"cn.bitflash.dao"})
@SpringBootApplication
public class BitflashLoginApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BitflashLoginApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BitflashLoginApplication.class);
    }
}
