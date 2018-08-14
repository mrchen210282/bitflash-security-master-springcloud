package cn.bitflash.feign.impl;

import cn.bitflash.feign.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.user.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserFallback implements FallbackFactory<UserFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFallback.class);

    @Override
    public UserFeign create(Throwable throwable) {
        return new UserFeign() {
            @Override
            public UserInfoEntity selectUserInfoById(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public List<UserInfoEntity> selectUserInfoList(Map<String, Object> params) {
                return null;
            }

            @Override
            public UserRelationEntity selectRelation(Map<String, Object> params) {
                return null;
            }

            @Override
            public UserPayPwdEntity selectUserPayPwd(Map<String, Object> params) {
                return null;
            }

            @Override
            public UserInvitationCodeEntity selectRelation(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return new UserInvitationCodeEntity();
            }

            @Override
            public List<UserRelationJoinAccountEntity> selectTreeNodes(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return new ArrayList<UserRelationJoinAccountEntity>();
            }
        };
    }
}
