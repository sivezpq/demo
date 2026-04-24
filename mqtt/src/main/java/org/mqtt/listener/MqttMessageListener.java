package org.mqtt.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.mqtt.config.MqttProperties;
import org.mqtt.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息回调，处理接收的消息
 * MqttCallback三个接口方法
 * 1，连接中断处理
 * void connectionLost(Throwable var1);
 * 2，消息到达处理
 * void messageArrived(String var1, MqttMessage var2) throws Exception;
 * 3，投递完成处理
 * void deliveryComplete(IMqttDeliveryToken var1);
 */
@Slf4j
@Component
public class MqttMessageListener {
//    private static final int MAX_RETRY_ATTEMPTS = 10;
//    private static final long INITIAL_RETRY_DELAY = 1_000L;
//    private static final List DEFAULT_TOPICS = Collections.singletonList("defaultTopic");
//    private final ScheduledExecutorService reconnectScheduler = Executors.newSingleThreadScheduledExecutor();
//    private final AtomicInteger retryCounter = new AtomicInteger(0);
//    private final Map<String, MessageHandler> topicHandlers = new ConcurrentHashMap<>();
//    private final TestService testService;
//    private final MqttProperties mqttProperties;
//    @Lazy
//    @Autowired
//    private MqttClient mqttClient;
//    public MqttMessageListener(TestService testService, MqttProperties mqttProperties) {
//        this.testService = testService;
//        this.mqttProperties = mqttProperties;
//        initializeHandlers();
//    }
//
//    private void initializeHandlers() {
//        topicHandlers.put(mqttProperties.getTopics().getTopic1(), this::handleMessage);
//    }
//
//    @Override
//    public void connectionLost(Throwable cause) {
//        log.error("MQTT连接中断，原因: {}", cause.getMessage());
//        scheduleReconnect();
//    }
//
//    private synchronized void scheduleReconnect() {
//        int attempt = retryCounter.incrementAndGet();
//        if (attempt > MAX_RETRY_ATTEMPTS) {
//            log.error("达到最大重连次数[{}]，停止重连尝试", MAX_RETRY_ATTEMPTS);
//            return;
//        }
//        long delay = INITIAL_RETRY_DELAY * (long) Math.pow(2, attempt - 1);
//        log.info("将在{}ms后尝试第{}次重连...", delay, attempt);
//        reconnectScheduler.schedule(() -> {
//            try {
//                if (!mqttClient.isConnected()) {
//                    mqttClient.reconnect();
//                }
//                subscribeTopics(mqttClient);
//                mqttClient.setCallback(this);
//                retryCounter.set(0);
//                log.info("MQTT连接恢复成功");
//            } catch (MqttException e) {
//                log.error("第{}次重连失败: {}", attempt, e.getMessage(),e);
//                scheduleReconnect();
//            }
//        }, delay, TimeUnit.MILLISECONDS);
//    }
//
//    private void subscribeTopics(MqttClient client) throws MqttException {
//        List<String> topics = getTopicsToSubscribe();
//        for (String topic : topics) {
//            try {
//                client.subscribe(topic, mqttProperties.getQos());
//                log.info("成功订阅主题: {}", topic);
//            } catch (MqttException e) {
//                log.error("订阅主题[{}]失败，错误码: {}", topic, e.getReasonCode(), e);
//                throw e;
//            }
//        }
//    }
//
//    private List<String> getTopicsToSubscribe() {
//        return Optional.ofNullable(mqttProperties.getTopics())
//                        .map(t -> Arrays.asList(t.getTopic1()))
//                        .filter(list -> !list.contains(null))
//                        .orElse(DEFAULT_TOPICS);
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) {
//        try {
//            String payload = validatePayload(message.getPayload());
//            log.info("收到消息 [Topic:{}][QoS:{}] {}", topic, message.getQos(), payload);
//
//            MessageHandler handler = Optional.ofNullable(topicHandlers.get(topic))
//                            .orElseThrow(() -> new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION));
//
//            asyncProcessMessage(() -> {
//                try {
//                    handler.handle(payload);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        } catch (MqttException e) {
//            log.error("消息处理失败 [Topic:{}]: {}", topic, e.getMessage(), e);
//        } catch (Exception e) {
//            log.error("未知处理异常 [Topic:{}]: {}", topic, e.getMessage(), e);
//        }
//    }
//
//    private String validatePayload(byte[] payload) throws Exception {
//        String json = new String(payload);
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            // 解析json
//        } catch (Exception e) {
//            log.error("非法JSON格式: {}", json);
//            throw new Exception("非法JSON格式", e);
//        }
//        return json;
//    }
//
//    @Async("mqttExecutor")
//    public void asyncProcessMessage(Runnable task) {
//        task.run();
//    }
//
//    private void handleMessage(String payload) {
//        testService.handleMessage(payload);
//    }
//    // private void handleMessage3(String payload) {
//    //     testService.handleMessage3(payload);
//    // }
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//        try {
//            if (token.getException() != null) {
//                log.error("消息投递失败 [MessageId:{}]", token.getMessageId(), token.getException());
//            } else {
//                log.info("消息投递成功 [MessageId:{}]", token.getMessageId());
//            }
//        } catch (Exception e) {
//            log.error("获取投递状态失败", e);
//        }
//    }
//
//    @FunctionalInterface
//    private interface MessageHandler {
//        void handle(String payload) throws Exception;
//    }
}
