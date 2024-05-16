package com.designpattern.responsibilitychain.service;

import com.designpattern.responsibilitychain.Entity.GatewayEntity;

public interface GatewayService {
    /**
     * 根据 handlerId 获取配置项
     * @param handlerId
     * @return
     */
    GatewayEntity getGatewayEntity(Integer handlerId);

    /**
     * 获取第一个处理者
     * @return
     */
    GatewayEntity getFirstGatewayEntity();
}
