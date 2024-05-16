package com.designpattern.responsibilitychain.handle;

/**
 * 抽象处理者
 */
public abstract class AbstractGatewayHandler implements GatewayHandler{

    /**
     * 下一关用当前抽象类来接收
     */
    protected GatewayHandler next;

    public void setNext(GatewayHandler next) {
        this.next = next;
    }

}
