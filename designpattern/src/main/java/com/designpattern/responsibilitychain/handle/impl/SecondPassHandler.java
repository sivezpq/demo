package com.designpattern.responsibilitychain.handle.impl;

import com.designpattern.responsibilitychain.handle.AbstractGatewayHandler;

/**
 * 具体处理者
 */
public class SecondPassHandler extends AbstractGatewayHandler {

    private int play(){
        return 90;
    }

    @Override
    public int handler(){
        System.out.println("第二关-->SecondPassHandler");

        int score = play();
        if(score >= 90){
            //分数>=90 并且存在下一关才进入下一关
            if(this.next != null){
                return this.next.handler();
            }
        }

        return score;
    }
}
