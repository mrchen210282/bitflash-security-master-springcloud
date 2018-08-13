package cn.bitflash.feign.impl;

import cn.bitflash.feign.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserRelationEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
public class UserFallback implements FallbackFactory<UserFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFallback.class);

    @Override
    public UserFeign create(Throwable throwable) {
        return new UserFeign() {
            @Override
            public List<UserEntity> selectUserOne(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserInfoEntity selectUserById(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public List<UserInfoEntity> selectUserInfoOne(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserInfoEntity selectUserInfoById(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @PostMapping("/api/user/selectRelationOne")
            public UserRelationEntity selectRelationOne(@RequestParam("params") Map<String, Object> params) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserPayPwdEntity selectPayPwdOne(Map<String, Object> params) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }
        };
    }
}
