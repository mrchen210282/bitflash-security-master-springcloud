package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.service.UserComplaintService;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.service.UserTradeService;
import cn.bitflash.trade.UserComplaintEntity;
import cn.bitflash.trade.UserTradeEntity;
import cn.bitflash.trade.UserTradeHistoryEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * wangjun
 */
@RestController
@RequestMapping("/api")
public class ApiUserComplaintController {

    @Autowired
    private UserComplaintService userComplaintService;

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;

    /**
     * 添加申拆
     * @param uid 用户id
     * @param orderId 订单id
     * @param complaintState 买入(0)卖出(1)申拆状态
     */
    @Login
    @PostMapping("complaint" )
    @Transactional
    public R complaint(@RequestParam String uid, @RequestParam String orderId, @RequestParam String complaintState) {
        if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(complaintState)) {
            UserComplaintEntity userComplaintEntity = userComplaintService.selectOne(new EntityWrapper<UserComplaintEntity>().eq("order_id",orderId));
            if (null != userComplaintEntity) {
                return R.error().put("code","500");
            }

            //设置交易订单状态为9申拆中
            UserTradeEntity userTradeEntity = new UserTradeEntity();
            userTradeEntity.setId(orderId);
            //申拆中
            userTradeEntity.setState(Common.STATE_COMPLAINT);
            userTradeService.insertOrUpdate(userTradeEntity);

            //查询卖入订单信息

            UserTradeHistoryEntity userTradeHistoryEntity =  userTradeHistoryService.selectOne(new EntityWrapper<UserTradeHistoryEntity>().eq("user_trade_id",orderId));

            if(null != userTradeHistoryEntity) {
                //添加申拆
                userComplaintEntity = new UserComplaintEntity();
                userComplaintEntity.setComplaintState(complaintState);

                userComplaintEntity.setComplaintUid(userTradeHistoryEntity.getSellUid());
                //订单发布人
                userComplaintEntity.setContactsUid(userTradeHistoryEntity.getPurchaseUid());
                userComplaintEntity.setOrderId(orderId);
                userComplaintEntity.setOrderState(Common.COMPLAINT_NO);
                userComplaintEntity.setCreateTime(new Date());
                userComplaintService.insert(userComplaintEntity);
            } else {
                //申拆订单不存在
                return R.error().put("code","501");
            }
        }
        return R.ok();
    }
}
