package org.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * MQTT配置类
 */
@Slf4j
@Configuration
public class MqttConfig {
    private static final List DEFAULT_TOPICS = Collections.singletonList("defaultTopic");
    private final MqttProperties mqttProperties;
    private final MqttCallback mqttCallback;

    public MqttConfig(MqttProperties mqttProperties, MqttCallback mqttCallback) {
        this.mqttProperties = mqttProperties;
        this.mqttCallback = mqttCallback;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = createMqttClient();
        MqttConnectOptions options = buildMqttConnectOptions();
        try {
            client.connect(options);
            log.info("MQTT连接成功，Broker地址: {}", mqttProperties.getBrokerUrl());
            subscribeTopics(client);
        } catch (MqttException e) {
            log.error("MQTT连接异常: {}，错误码: {}", e.getMessage(), e.getReasonCode(), e);
            throw new RuntimeException("MQTT连接失败", e);
        }
        client.setCallback(mqttCallback);
        return client;
    }

    private MqttClient createMqttClient() throws MqttException {
        return new MqttClient(mqttProperties.getBrokerUrl(), generateClientId(), new MemoryPersistence());
    }

    private String generateClientId() {
        return Optional.ofNullable(mqttProperties.getClientId())
                        .filter(StringUtils::hasText)
                        .orElseGet(() -> "CLIENT_" + System.currentTimeMillis());
    }

    private MqttConnectOptions buildMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(mqttProperties.isAutomaticReconnect());
        options.setCleanSession(mqttProperties.isCleanSession());
        Optional.ofNullable(mqttProperties.getUsername())
                .filter(StringUtils::hasText)
                .ifPresent(options::setUserName);
        Optional.ofNullable(mqttProperties.getPassword())
                .filter(StringUtils::hasText)
                .map(String::toCharArray)
                .ifPresent(options::setPassword);
        options.setConnectionTimeout(mqttProperties.getConnectionTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        return options;
    }

    private void subscribeTopics(MqttClient client) throws MqttException {
        List<String> topics = getTopicsToSubscribe();
        for (String topic : topics) {
            try {
                client.subscribe(topic, mqttProperties.getQos());
                log.info("成功订阅主题: {}", topic);
            } catch (MqttException e) {
                log.error("订阅主题[{}]失败，错误码: {}", topic, e.getReasonCode(), e);
                throw e;
            }
        }
    }

    private List<String> getTopicsToSubscribe() {
        return Optional.ofNullable(mqttProperties.getTopics())
                        .map(t -> Arrays.asList(t.getTopic1()))
                        .filter(list -> !list.contains(null))
                        .orElse(DEFAULT_TOPICS);
    }

    @Bean("mqttExecutor")
    public Executor mqttExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(mqttProperties.getPoolConfig().getCoreSize());
        executor.setMaxPoolSize(mqttProperties.getPoolConfig().getMaxSize());
        executor.setQueueCapacity(mqttProperties.getPoolConfig().getQueueCapacity());
        executor.setThreadNamePrefix(mqttProperties.getPoolConfig().getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
