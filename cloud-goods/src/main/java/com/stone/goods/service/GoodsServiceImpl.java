package com.stone.goods.service;

import com.stone.goods.dao.GoodsDao;
import com.stone.sdk.GoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

}
