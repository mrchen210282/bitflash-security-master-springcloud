package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashUserFeign;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements FallbackFactory<BitflashUserFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public BitflashUserFeign create(Throwable throwable) {
        return new BitflashUserFeign() {
            @Override
            public R userInfo() {
                log.error("用户信息查询失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R accountInfo() {
                log.error("登录后获取用户信息失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R getInvitationcode() {
                log.error("获取推广码失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R getRelation() {
                log.error("获取用户体系失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R changePwd(String oldPwd, String newPwd) {
                log.error("修改密码失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R changePwd2(String mobile, String newPwd) {
                log.error("修改密码失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R updateNickName(String nickname) {
                log.error("修改昵称失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R addPayPassword(String payPassword) {
                log.error("添加交易密码失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R updatePayPwd(String oldPwd, String newPwd) {
                log.error("更新交易密码失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R upload(String img, String imgType) {
                log.error("上传的图片失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R userInfoImg() {
                log.error("取得上传的图片失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R uploadImgMessage(String img, String imgType) {
                log.error("上传身份证图片信息失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R getSFZAdress(String imgType) {
                log.error("上传支付宝/微信图片失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R getVipLevel() {
                log.error("获取用户vip信息失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R updateVipLevel() {
                log.error("修改用户vip信息失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public UserInvitationCodeEntity selectOne(EntityWrapper entityWrapper) {
                log.error("邀请码查询失败：-----"+throwable.getMessage());
                return new UserInvitationCodeEntity();
            }

            @Override
            public boolean insert(UserInfoEntity userInfoEntity) {
                log.error("用户信息添加失败：-----"+throwable.getMessage());
                return false;
            }
        };
    }
}
