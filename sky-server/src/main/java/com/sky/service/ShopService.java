package com.sky.service;

import org.springframework.stereotype.Service;


public interface ShopService {

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    void setStatus(Integer status);

    /**
     * 获取店铺营业状态
     * @return
     */
    Integer getStatus();
}
