package cn.bitflash;

import cn.bitflash.config.IPFilter;
import cn.bitflash.config.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class BitflashZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitflashZuulApplication.class, args);
    }

    @Bean
    public TokenFilter getTokenFilter(){
        return  new TokenFilter();
    }
    @Bean
    public IPFilter getIPFilter(){
        return new IPFilter();
    }
}
