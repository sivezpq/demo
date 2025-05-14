package org.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    /**
     * 是否启用MQTT功能
     */
    private boolean enabled;
    /**
     * MQTT Broker地址
     */
    private String brokerUrl;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * QoS级别 (0/1/2)
     */
    private int qos = 1;
    /**
     * 自动重连
     */
    private boolean automaticReconnect = false;
    /**
     * 清理会话
     */
    private boolean cleanSession = false;

    /**
     * 连接超时时间(ms)
     */
    private int connectionTimeout = 5000;

    /**
     * 保持连接间隔(秒)
     */
    private int keepAliveInterval = 30;

    /**
     * 主题配置
     */
    private Topics topics = new Topics();
    /**
     * 线程相关参数
     */
    private PoolConfig poolConfig = new PoolConfig();

    @Data
    public static class PoolConfig {
        private int coreSize = 8;
        private int maxSize = 16;
        private int queueCapacity = 1000;
        private String threadNamePrefix = "mqtt-worker-";
    }

    @Data
    public static class Topics {
        /**
         * topic1
         */
        private String topic1;

        /**
         * topic2
         */
        private String topic2;

        /**
         * topic3
         */
        private String topic3;

    }

    // 需要显式声明空构造器
    public MqttProperties() {
    }
}
