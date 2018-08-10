package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashUserFeign;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements FallbackFactory<BitflashUserFeign> {
    @Override
    public BitflashUserFeign create(Throwable throwable) {
        return new BitflashUserFeign() {
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
            public R updateNickName(String nickname) {
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
            public R upload(String img, String imgType) {
                return null;
            }

            @Override
            public R userInfoImg() {
                return null;
            }

            @Override
            public R uploadImgMessage(String img, String imgType) {
                return null;
            }

            @Override
            public R getSFZAdress(String imgType) {
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
            public UserInvitationCodeEntity selectOne(EntityWrapper entityWrapper) {
                return null;
            }

            @Override
            public boolean insert(UserInfoEntity userInfoEntity) {
                return false;
            }
        };
    }
}
