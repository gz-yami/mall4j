/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.yami.shop.bean.app.dto.ProductItemDto;
import com.yami.shop.bean.model.UserAddr;

public interface TransportManagerService {

	Double calculateTransfee(ProductItemDto productItem, UserAddr userAddr);
}
