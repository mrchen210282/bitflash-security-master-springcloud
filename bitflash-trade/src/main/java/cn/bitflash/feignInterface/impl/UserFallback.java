package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.user.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
                log.error("selectUserInfoById进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public List<UserInfoEntity> selectUserInfoList(Map<String, Object> params) {
                log.error("selectUserInfoList进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public UserRelationEntity selectRelation(Map<String, Object> params) {
                log.error("selectRelation进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public UserPayPwdEntity selectUserPayPwd(Map<String, Object> params) {
                log.error("selectUserPayPwd进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public List<UserRelationJoinAccountEntity> selectTreeNodes(String uid) {
                log.error("selectTreeNodes进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public UserInvitationCodeEntity selectUserInvitationCode(String uid) {
                log.error("selectUserInvitationCode进入降级处理，错误原因："+throwable);
                return null;
            }

            @Override
            public List<UserPayUrlEntity> selectUserPayUrl(String uid) {
                log.error("selectUserPayUrl，错误原因："+throwable);
                return null;
            }
        };
    }
}
