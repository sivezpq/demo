package com.designpattern.responsibilitychain.handle.impl;

import com.designpattern.responsibilitychain.handle.AbstractGatewayHandler;

/**
 * 具体处理者
 */
public class ThirdPassHandler extends AbstractGatewayHandler {

    private int play(){
        return 95;
    }

    @Override
    public int handler(){
        System.out.println("第三关-->ThirdPassHandler");
        int score = play();
        if(score >= 95){
            //分数>=95 并且存在下一关才进入下一关
            if(this.next != null){
                return this.next.handler();
            }
        }
        return score;
    }
}
