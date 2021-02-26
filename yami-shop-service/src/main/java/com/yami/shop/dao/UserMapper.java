/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.vo.UserVO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

	User getUserByBizUserId(@Param("appId")Integer appId, @Param("bizUserId")String bizUserId);
}
