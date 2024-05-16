package com.designpattern.observer.listener;

import com.designpattern.observer.LotteryResult;

public interface EventListener {
    void doEvent(LotteryResult result);
}
