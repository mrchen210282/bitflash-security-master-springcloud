package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserComplaintService;
import cn.bitflash.trade.UserComplaintEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiUserComplaintController {

    @Autowired
    private UserComplaintService userComplaintService;

    /**
     * 添加申拆
     * @param uid 用户id
     * @param orderId 订单id
     * @param complaintState 买入(0)卖出(1)申拆状态
     */
    @Login
    @PostMapping("complaint" )
    public R complaint(@RequestParam String uid, @RequestParam String orderId, @RequestParam String complaintState) {
        if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(complaintState)) {
            UserComplaintEntity userComplaintEntity = userComplaintService.selectOne(new EntityWrapper<UserComplaintEntity>().eq("user_trade_id",orderId));
            if (null != userComplaintEntity) {
                return R.error().put("code","500");
            }
            userComplaintEntity = new UserComplaintEntity();
            userComplaintEntity.setComplaintState(complaintState);
            userComplaintEntity.setComplaintUid(uid);
            userComplaintEntity.setOrderId(Integer.valueOf(orderId));
            userComplaintEntity.setOrderState(Common.COMPLAINT_NO);
            userComplaintEntity.setCreateTime(new Date());
            userComplaintService.insert(userComplaintEntity);
        }
        return R.ok();
    }
}
