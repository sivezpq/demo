package com.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.ShardingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleXxlJob {

    private static final Logger logger = LoggerFactory.getLogger(SampleXxlJob.class);

    @XxlJob(value = "sampleJobHandler", init = "init", destroy = "destroy")
    public ReturnT<String> sampleJobHandler(String param) {
        logger.info("xxl-job callback sampleJobHandler:{}", param);

        /**
         * 2.2.0以上版本后，做了一下升级：
         * 1，新增任务辅助工具 “XxlJobHelper”：提供统一任务辅助能力，包括：任务上下文信息维护获取（任务参数、任务ID、分片参数）、日志输出、任务结果设置……等
         *   1.1，”ShardingUtil” 组件废弃：改用 “XxlJobHelper.getShardIndex()/getShardTotal();” 获取分片参数；
         *,  1.2，”XxlJobLogger” 组件废弃：改用 “XxlJobHelper.log” 进行日志输出；
         * 2，【优化】任务核心类 “IJobHandler” 的 “execute” 方法取消出入参设计。改为通过 “XxlJobHelper.getJobParam” 获取任务参数并替代方法入参，通过
         *        @XxlJob("demoJobHandler")
         *        public void execute() {
         *           String param = XxlJobHelper.getJobParam();    // 获取参数
         *           XxlJobHelper.handleSuccess();                 // 设置任务结果
         *        }
         */
        int shardIndex = ShardingUtil.getShardingVo().getIndex();
        int shardTotal = ShardingUtil.getShardingVo().getTotal();

        //业务处理
//        throw new IllegalStateException("sampleJobHandler");

        return new ReturnT(500, "sampleJobHandler error");
    }

    public void init(){
        logger.info("init");
    }
    public void destroy(){
        logger.info("destory");
    }
}
