package com.designpattern.observer.listener.impl;

import com.designpattern.observer.LotteryResult;
import com.designpattern.observer.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MessageEventListener implements EventListener {
    private Logger logger = LoggerFactory.getLogger(MessageEventListener.class);

    @Override
    public void doEvent(LotteryResult result) {
        logger.info("给用户{} 发送短信通知(短信)：{}", result.getUId(), result.getMsg());
    }
}
