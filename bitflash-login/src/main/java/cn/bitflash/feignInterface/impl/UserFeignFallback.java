package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserFeignFallback implements FallbackFactory<UserFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public UserFeign create(Throwable throwable) {
        return new UserFeign() {
            @Override
            public UserInvitationCodeEntity selectOne(Wrapper wrapper) {
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
