package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.feignInterface.LoginFeign;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.*;
import cn.bitflash.trade.*;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author gao
 */
@RestController
@RequestMapping("/api/appeal" )
public class ApiAppealController {

    @Autowired
    private UserBuyService userBuyService;

    @Autowired
    private UserBuyHistoryService userBuyHistoryService;
    @Autowired
    private UserTradeConfigService userTradeConfigService;

    /**
     * ---------------����ҳ----------------
     */

    @Login
    @PostMapping("showlist")
    public R showNeedMessage(@LoginUser UserEntity user) {
        List<UserBuyBean> userBuyEntities = userBuyService.selectAppealList(user.getUid());
        List<UserBuyBean> userBuyEntitiesList = new LinkedList<UserBuyBean>();
        for(UserBuyBean userBuyBean :userBuyEntities){
            userBuyBean.setState("������");
            userBuyEntitiesList.add(userBuyBean);
        }

        return R.ok().put("userBuyEntitiesList", userBuyEntitiesList);
    }

    @Login
    @PostMapping("appealMessage")
    public R appealMessage(@RequestParam("id") String id) {
        UserBuyHistoryBean userBuyHistoryBean = userBuyHistoryService.selectBuyHistory(id);

        Map<String,Float> map = this.poundage(id);

        return R.ok().put("userBuyHistoryBean",userBuyHistoryBean).put("totalQuantity",map.get("totalQuantity")).put("price",map.get("price")).put("buyQuantity",map.get("buyQuantity")).put("totalMoney",map.get("totalMoney"));
    }

    /**
     * ----------------------------������+��������------------------------
     *
     */
    public Map<String,Float> poundage(String id){
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));

        DecimalFormat df = new DecimalFormat("#########.##" );
        //��������
        Float buyQuantity = Float.parseFloat(df.format(userBuy.getQuantity()));
        //�����ѱ���
        Float poundage = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "����������")).getPoundage();
        //Float poundage = Float.parseFloat(df.format(poundagePer));
        //����������
        Float totalPoundage = buyQuantity*poundage;
        //ʵ�ʽ���������
        Float totalQuantity = buyQuantity+totalPoundage;
        //����
        Float price = userBuy.getPrice();
        //�ܼ۸�
        Float totalMoney = buyQuantity*(price);

        Map<String,Float> map = new HashMap<String,Float>();
        map.put("buyQuantity",buyQuantity);
        map.put("poundage",poundage);
        map.put("totalPoundage",totalPoundage);
        map.put("totalQuantity",totalQuantity);
        map.put("price",price);
        map.put("totalMoney",totalMoney);
        return map;
    }


}