package com.designpattern.observer;

import com.designpattern.observer.listener.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    /**
     * 事件订阅的观察者映射列表
     */
    Map<Enum<EventType>, List<EventListener>> listenerMap = new HashMap<>();

    public EventManager(Enum<EventType>... events) {
        for (Enum<EventType> event : events) {
            this.listenerMap.put(event, new ArrayList<>());
        }
    }

    /**
     * 订阅
     *
     * @param eventType 事件类型
     * @param listeners  观察者
     */
    public void subscribe(Enum<EventType> eventType, EventListener... listeners) {
        List<EventListener> eventWatchers = listenerMap.get(eventType);
        for (EventListener listener : listeners) {
            eventWatchers.add(listener);
        }
    }

    /**
     * 取消订阅
     *
     * @param eventType 事件类型
     * @param listeners  观察者
     */
    public void unsubscribe(Enum<EventType> eventType, EventListener... listeners) {
        List<EventListener> eventWatchers = listenerMap.get(eventType);
        for (EventListener listener : listeners) {
            eventWatchers.remove(listener);
        }
    }

    /**
     * 事件处理
     *
     * @param eventType 事件类型
     * @param result  事件结果
     */
    public void notify(Enum<EventType> eventType, LotteryResult result) {
        List<EventListener> eventWatchers = listenerMap.get(eventType);
        for (EventListener listener : eventWatchers) {
            listener.doEvent(result);
        }
    }

    /**
     * 事件类型
     */
    public enum EventType {
        /**
         * MQ：存入消息队列
         * MSG：发送短信
         */
        MQ, MSG
    }
}
