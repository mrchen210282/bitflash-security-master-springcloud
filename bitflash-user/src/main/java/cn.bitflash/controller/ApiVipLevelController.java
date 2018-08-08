package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.exception.RRException;
import cn.bitflash.service.*;
import cn.bitflash.user.*;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.CodeUtils;
import common.utils.Common;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api/vip" )
//@Api(tags = "获取用户vip信息" )
public class ApiVipLevelController {


    private final Logger logger = LoggerFactory.getLogger(ApiVipLevelController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @Autowired
    private UserInvitationCodeService userInvitationCodeService;

    @Autowired
    private UserRelationService userRelationService;

    /**
     * @author chen
     */
    @Login
    @PostMapping("getVipLevel" )
    //@ApiOperation("获取用户vip信息" )
    public R getVipLevel(@LoginUser UserEntity user) {
        String uid = user.getUid();
        UserInfoEntity userEntity = userInfoService.selectOne(new EntityWrapper<UserInfoEntity>().eq("uid", uid));
        if (userEntity != null) {
            UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", uid));
            return R.ok().put("vip", userEntity.getIsVip()).put("bit", userAccountEntity.getAvailableAssets());
        } else {
            return R.error("此用户不存在" );

        }
    }

    /**
     * @author chen
     */
    @Login
    @Transactional
    @PostMapping("updateVipLevel" )
    //@ApiOperation("提升vip等级" )
    public R updateVipLevel(@LoginUser UserEntity user) {
        /**
         *  1.查询是否是vip
         *		只有vip等级为0的用户才能升级操作
         *  2.查询可用资产(availableAssets)是否够用
         *      2.1 金额够升级vip2
         *          2.1.1 可用资产(可用资产=调节释放+调节收益)-=20000
         *          2.1.2 购买数量+=20000
         *          2.1.3 赠送数量+=20000*0.05
         *          2.1.4 冻结数量+=20000+赠送数量
         *      2.2 return 金额不足
         */
        String uid = user.getUid();
        UserInfoEntity userInfo = userInfoService.selectById(uid);
        String invitationCode = userInfo.getInvitationCode();
        if (StringUtils.isBlank(invitationCode)) {
            return R.error("非邀请码注册用户" );
        }
        //vip升级数量
        Double vip_count = Double.valueOf(platFormConfigService.getVal(Common.VIP_CONDITION));
        //赠送数量
        Double giveRatio = Double.valueOf(platFormConfigService.getVal(Common.GIVE_RATIO));
        //vip——count BigDecimal类型
        BigDecimal vip_number = new BigDecimal(vip_count);
        if (userInfo.getIsVip().equals(Common.VIP_LEVEL_0)) {
            UserAccountEntity userAccount = userAccountService.selectById(uid);
            BigDecimal acacilNum = userAccount.getAvailableAssets();
            // 可用资产>=2w
            if (acacilNum.compareTo(vip_number) == 1 || acacilNum.compareTo(vip_number) == 0) {
                /**
                 * 计算升级VIP之后的数据
                 * 2.1金额够升级vip2
                 * 2.1.1 可用资产(可用资产=调节释放+调节收益)-=20000
                 * 2.1.2 购买数量+=20000
                 * 2.1.3 赠送数量+=20000*0.5
                 * 2.1.4 冻结数量+=20000+赠送数量
                 */
                BigDecimal zero = new BigDecimal(0.00);
                double result = userAccount.getRegulateRelease().doubleValue() - vip_count;
                if (result <= 0) {
                    userAccount.setRegulateRelease(zero);
                    userAccount.setRegulateIncome(new BigDecimal(result + userAccount.getRegulateIncome().doubleValue()));
                } else {
                    userAccount.setRegulateRelease(new BigDecimal(result));
                }
                //2.1.1
                userAccount.setAvailableAssets(userAccount.getRegulateRelease().add(userAccount.getRegulateIncome()));
                //2.1.2
                userAccount.setPurchase(userAccount.getPurchase().add(vip_number));
                //2.1.3
                userAccount.setGiveAmount(userAccount.getGiveAmount().add(new BigDecimal(vip_count * giveRatio)));
                //2.1.4
                userAccount.setFrozenAssets(userAccount.getFrozenAssets().add(vip_number.add(new BigDecimal(vip_count * giveRatio))));
                userAccount.setTotelAssets(userAccount.getPurchase().add(userAccount.getGiveAmount()));
                userAccountService.updateById(userAccount);
                //更新会员等级
                userInfo.setIsVip(Common.VIP_LEVEL_2);
                userInfo.setVipCreateTime(new Date());
                userInfoService.updateById(userInfo);
                UserInvitationCodeEntity userInvit = new UserInvitationCodeEntity();
                userInvit.setUid(uid);
                userInvit.setLftCode(CodeUtils.genInvitationCode());
                userInvit.setRgtCode(CodeUtils.genInvitationCode());
                //生成会员的邀请码
                userInvitationCodeService.insert(userInvit);
                //插入体系内
                this.insertTradeCodes(invitationCode, uid);
                logger.info("升级vip的用户uid/手机号为：" + user.getUid() + "--" + user.getMobile());
                return R.ok();
            } else {
                return R.error("资金不足" );
            }
        }
        return R.error("已经是VIP用户，无法再次提升，敬请关注后续动态" );
    }


    /**
     * @param invitationCode
     * @param uid
     */
    public R insertTradeCodes(String invitationCode, String uid) {
        /**
         * 1.根据邀请码查询出父节点的uid
         * 2.找到父节点的下面的子节点
         * 3.判断左右区邀请码
         *
         * 情况1：o  情况2：o  情况3：o
         *      /         /\       /
         *     o         o  o     o
         *                        /
         *                       o
         *
         */
        UserInvitationCodeEntity pCode = userInvitationCodeService.selectOne(new EntityWrapper<UserInvitationCodeEntity>()
                .eq("lft_code", invitationCode).or().eq("rgt_code", invitationCode));

        List<UserRelationJoinAccountEntity> f_user = userRelationService.selectTreeNodes(pCode.getUid());
        List<UserRelationJoinAccountEntity> child_user = userRelationService.selectTreeNodes(uid);
        //左区邀请码
        if (invitationCode.equals(pCode.getLftCode()) && child_user.size() == 0 && f_user.size() > 0) {
            if (f_user.size() == 1) {
                userRelationService.insertTreeNode(pCode.getUid(), uid, invitationCode);

            } else if (f_user.size() == 2) {
                userRelationService.insertTreeNode(f_user.get(1).getUid(), uid, invitationCode);
            } else if (f_user.size() > 2) {
                //筛选出左区第一个子节点
                UserRelationJoinAccountEntity ue = f_user.stream().filter((u) -> u.getLft() == f_user.get(0).getLft() + 1).findFirst().get();
                List<UserRelationJoinAccountEntity> child2_user = f_user.stream().filter((u) ->
                        u.getLft() >= ue.getLft() && u.getRgt() <= ue.getRgt()).collect(Collectors.toList());
                if (child2_user.size() == 1) {
                    userRelationService.insertTreeNode(child2_user.get(0).getUid(), uid, invitationCode);
                } else if (child2_user.size() > 1) {
                    userRelationService.insertTreeNode(this.getChildNode(child2_user, new HashMap<>()), uid, invitationCode);
                }
            }
        } else if (invitationCode.equals(pCode.getRgtCode()) && child_user.size() == 0 && f_user.size() > 0) {
            if (f_user.size() == 1) {
                //return R.error("只能先插入左区" );
            	throw new RRException("只能先插入左区！");
            } else if (f_user.size() == 2) {
                userRelationService.insertTreeNode(pCode.getUid(), uid, invitationCode);
            } else if (f_user.size() > 2) {
                if (f_user.get(0).getRgt() == f_user.get(1).getRgt() + 1) {
                    userRelationService.insertTreeNode(pCode.getUid(), uid, invitationCode);
                    return R.ok();
                }
                //筛选出右区第一个子节点
                UserRelationJoinAccountEntity ue = f_user.stream().filter((u) -> u.getLft() == f_user.get(1).getRgt() + 1).findFirst().get();
                List<UserRelationJoinAccountEntity> child2_user = f_user.stream().filter((u) ->
                        u.getLft() >= ue.getLft() && u.getRgt() <= ue.getRgt()).collect(Collectors.toList());
                if (child2_user.size() == 1) {
                    userRelationService.insertTreeNode(child2_user.get(0).getUid(), uid, invitationCode);
                } else {
                    userRelationService.insertTreeNode(this.getChildNode(child2_user, new HashMap<>()), uid, invitationCode);
                }
            }
        }
        return R.ok();
    }

    public String getChildNode(List<UserRelationJoinAccountEntity> p1_user, Map<String, UserRelationJoinAccountEntity> map) {
        /**
         * 寻找收益最高的下面的点
         * 实现原理：递归筛选，直到筛选出末尾节点
         */
        UserRelationJoinAccountEntity last_node = null;
        if (p1_user.size() > 1) {
            //左区比右区收益高/相等 添加到左区
            UserRelationJoinAccountEntity p2_user;
            //左节点
            p2_user = p1_user.stream().filter((u) -> u.getLft() == p1_user.get(0).getLft() + 1).findFirst().get();
            List<UserRelationJoinAccountEntity> p3_user = p1_user.stream().filter((u) -> (
                    u.getLft() >= p2_user.getLft()) && u.getRgt() <= p2_user.getRgt()).collect(Collectors.toList());
            if (p3_user.size() == 1) {
                map.put("key", p3_user.get(0));
            } else {
                this.getChildNode(p3_user, map);
            }
        }
        last_node = map.get("key" );
        return last_node.getUid();
        /*if (p1_user.size() > 1) {
            //左区比右区收益高/相等 添加到左区
            UserRelationJoinAccountEntity p2_user;
            if (p1_user.get(0).getLftAchievement().compareTo(p1_user.get(0).getRgtAchievement()) >= 0) {
                //左节点
                p2_user = p1_user.stream().filter((u) -> u.getLft() == p1_user.get(0).getLft() + 1).findFirst().get();
            }
            //右区收益高
            else {
                //右节点
                p2_user = p1_user.stream().filter((u) -> u.getLft() == p1_user.get(1).getRgt() + 1).findFirst().get();
            }
            List<UserRelationJoinAccountEntity> p3_user = p1_user.stream().filter((u) -> (
                    u.getLft() >= p2_user.getLft()) && u.getRgt() <= p2_user.getRgt()).collect(Collectors.toList());
            if (p3_user.size() == 1) {
                map.put("key", p3_user.get(0));
            } else {
                this.getChildNode(p3_user, map);
            }
        }*/


    }

}
