package org.mqtt.publisher;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 消息发布
 */
@Slf4j
@Component
public class MqttPublisher {

    private MqttClient mqttClient;
    @Value("${ mqtt.qos:1} ")
    private int qosLevel;
    private final Object publishLock = new Object();

    /**
     * 发布消息
     */
    public void publishMessage(String msg, String topic) {
        try {
            synchronized (publishLock) { // 线程安全锁
                MqttMessage message = new MqttMessage();
                message.setPayload(msg.getBytes());
                message.setQos(qosLevel);
                message.setRetained(false); // 不保留消息
                mqttClient.publish(topic, message);
                log.info("Topic:{} 响应已发送: {}", topic, msg);
            }
        } catch (MqttException e) {
            log.error("MQTT发布失败: {}", e.getMessage());
            handlePublishFailure(msg, topic, e);
        }
    }

    /**
     * 消息失败重试机制
     */
    private void handlePublishFailure(String msg, String topic, MqttException e) {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            mqttClient.connect();
            publishMessage(msg, topic); // 重试发布
        } catch (MqttException ex) {
            log.error("消息重试失败: {}", ex.getMessage(), ex);
        }
    }

    /**
     * 消息持久化存储（QoS 1保障）
     */
    private void saveFailedMessage(String msg) {
        // 实现数据库存储逻辑（此处需补充DAO操作）
        log.warn("持久化失败消息: {}", msg);
    }
}