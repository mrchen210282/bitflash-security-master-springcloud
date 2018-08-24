package cn.bitflash.userutil;

import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements FallbackFactory<UserUtils> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public UserUtils create(Throwable throwable) {
        return new UserUtils() {
            @Override
            public UserInvitationCodeEntity selectOne(String invitationCode) {
                log.error("查询邀请码出错原因-----:"+throwable.getMessage());
                return new UserInvitationCodeEntity();
            }

            @Override
            public boolean insert(UserInfoEntity userInfoEntity) {
                log.error("初始化用户信息出错原因-----:"+throwable.getMessage());
                return false;
            }
        };
    }
}
