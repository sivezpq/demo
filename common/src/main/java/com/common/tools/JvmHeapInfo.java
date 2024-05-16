package com.common.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

public class JvmHeapInfo {

    public static void main(String[] args) throws Exception {
        byte[] array = new byte[700 * 1024 * 1024];
        for (MemoryPoolMXBean memoryPoolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
            long committed = memoryPoolMXBean.getUsage().getCommitted() / (1024 * 1024);
            long used = memoryPoolMXBean.getUsage().getUsed() / (1024 * 1024);
            System.out.println(memoryPoolMXBean.getName() + "  总量:" + committed  + "m   使用的内存:" + used + "m");
        }
    }
}
