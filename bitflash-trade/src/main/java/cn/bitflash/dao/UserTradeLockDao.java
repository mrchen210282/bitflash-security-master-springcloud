package cn.bitflash.dao;

import cn.bitflash.trade.UserTradeLockEntity;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.bitflash.trade.UserTradeLockEntity;

public interface UserTradeLockDao extends BaseMapper<UserTradeLockEntity> {

    Integer selectByDay(@Param("uid") String uid);
}
