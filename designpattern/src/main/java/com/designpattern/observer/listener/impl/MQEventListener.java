package com.designpattern.observer.listener.impl;

import com.designpattern.observer.LotteryResult;
import com.designpattern.observer.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MQEventListener implements EventListener {
    private Logger logger = LoggerFactory.getLogger(MQEventListener.class);

    @Override
    public void doEvent(LotteryResult result) {
        logger.info("记录用户{}，摇号结果（MO）：{}", result.getUId(), result.getMsg());
    }
}
