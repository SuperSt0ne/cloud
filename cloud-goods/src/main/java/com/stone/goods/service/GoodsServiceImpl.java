package com.stone.goods.service;

import com.stone.goods.dao.GoodsDao;
import com.stone.sdk.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

}
