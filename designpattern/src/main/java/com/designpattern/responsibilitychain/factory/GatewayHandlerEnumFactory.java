package com.designpattern.responsibilitychain.factory;

import com.designpattern.responsibilitychain.Entity.GatewayEntity;
import com.designpattern.responsibilitychain.handle.AbstractGatewayHandler;
import com.designpattern.responsibilitychain.handle.GatewayHandler;
import com.designpattern.responsibilitychain.service.GatewayService;
import com.designpattern.responsibilitychain.service.GatewayServiceImpl;

public class GatewayHandlerEnumFactory {
    private static GatewayService gatewayService = new GatewayServiceImpl();

    // 提供静态方法，获取第一个handler
    public static GatewayHandler getFirstGatewayHandler() {

        GatewayEntity firstGatewayEntity = gatewayService.getFirstGatewayEntity();
        AbstractGatewayHandler firstGatewayHandler = newGatewayHandler(firstGatewayEntity);
        if (firstGatewayHandler == null) {
            return null;
        }

        GatewayEntity tempGatewayEntity = firstGatewayEntity;
        Integer nextHandlerId = null;
        AbstractGatewayHandler tempGatewayHandler = firstGatewayHandler;
        // 迭代遍历所有handler，以及将它们链接起来
        while ((nextHandlerId = tempGatewayEntity.getNextHandlerId()) != null) {
            GatewayEntity gatewayEntity = gatewayService.getGatewayEntity(nextHandlerId);
            AbstractGatewayHandler gatewayHandler = newGatewayHandler(gatewayEntity);
            tempGatewayHandler.setNext(gatewayHandler);
            tempGatewayHandler = gatewayHandler;
            tempGatewayEntity = gatewayEntity;
        }
        // 返回第一个handler
        return firstGatewayHandler;
    }

    /**
     * 反射实体化具体的处理者
     * @param firstGatewayEntity
     * @return
     */
    private static AbstractGatewayHandler newGatewayHandler(GatewayEntity firstGatewayEntity) {
        // 获取全限定类名
        String className = firstGatewayEntity.getConference();
        try {
            // 根据全限定类名，加载并初始化该类，即会初始化该类的静态段
            Class<?> clazz = Class.forName(className);
            return (AbstractGatewayHandler) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
