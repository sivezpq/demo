package com.designpattern.singleton;

public class StaticInner {

    // 使用静态内部类，实现单例模式
    private StaticInner(){};

    private static class SingletonHolder{
        // 静态初始化器，由JVM来保证线程安全
        private static StaticInner staticInner = new StaticInner();
    }

    /**
     * 获取单例
     * @return
     */
    public static StaticInner getSingleton(){
        return SingletonHolder.staticInner;
    }

}
