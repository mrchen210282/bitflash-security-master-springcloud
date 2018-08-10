package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserRibbon;
import cn.bitflash.login.UserEntity;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;

import java.util.List;
import java.util.Map;

public class UserRibbonFallback implements FallbackFactory<UserRibbon> {
    @Override
    public UserRibbon create(Throwable throwable) {
        return new UserRibbon() {
            @Override
            public R userInfo() {
                return null;
            }

            @Override
            public R accountInfo() {
                return null;
            }

            @Override
            public R getInvitationcode() {
                return null;
            }

            @Override
            public R getRelation() {
                return null;
            }

            @Override
            public R changePwd(String oldPwd, String newPwd) {
                return null;
            }

            @Override
            public R changePwd2(String mobile, String newPwd) {
                return null;
            }

            @Override
            public R addPayPassword(String payPassword) {
                return null;
            }

            @Override
            public R updatePayPwd(String oldPwd, String newPwd) {
                return null;
            }

            @Override
            public R getVipLevel() {
                return null;
            }

            @Override
            public R updateVipLevel() {
                return null;
            }

            @Override
            public List<UserEntity> selectOne(Map<String, Object> params) {
                return null;
            }
        };
    }
}
