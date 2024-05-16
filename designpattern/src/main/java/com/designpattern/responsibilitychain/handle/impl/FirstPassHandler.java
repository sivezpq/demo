package com.designpattern.responsibilitychain.handle.impl;

import com.designpattern.responsibilitychain.handle.AbstractGatewayHandler;

/**
 * 具体处理者
 */
public class FirstPassHandler extends AbstractGatewayHandler {

    private int play(){
        return 80;
    }

    @Override
    public int handler(){
        System.out.println("第一关-->FirstPassHandler");
        int score = play();
        if(score >= 80){
            //分数>=80 并且存在下一关才进入下一关
            if(this.next != null){
                return this.next.handler();
            }
        }
        return score;
    }
}
