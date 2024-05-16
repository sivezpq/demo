package com.bsc;

import java.util.List;

/**
 * 白山云CDN缓存刷新结果bean
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */
public class CdnRefresh {

    private String task_id;
    private int count;
    private List<String> err_urls;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getErr_urls() {
        return err_urls;
    }

    public void setErr_urls(List<String> err_urls) {
        this.err_urls = err_urls;
    }
}