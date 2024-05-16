package com.designpattern.observer;

import com.designpattern.observer.listener.impl.MQEventListener;
import com.designpattern.observer.listener.impl.MessageEventListener;

public abstract class AbstractLotteryService {
    private EventManager eventManager;

    /**
     * 初始化注册/订阅监听者
     */
    public AbstractLotteryService() {
        eventManager = new EventManager(EventManager.EventType.MQ, EventManager.EventType.MSG);
        eventManager.subscribe(EventManager.EventType.MQ, new MQEventListener());
        eventManager.subscribe(EventManager.EventType.MSG, new MessageEventListener());
    }

    public LotteryResult draw(String uId) {
        LotteryResult lotteryResult = doDraw(uId);
        // 需要通知的事件
        eventManager.notify(EventManager.EventType.MQ, lotteryResult);
        eventManager.notify(EventManager.EventType.MSG, lotteryResult);
        return lotteryResult;
    }



    protected abstract LotteryResult doDraw(String uId);
}
