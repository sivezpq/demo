package org.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
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

    public MqttConfig(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    public MqttAsyncClient mqttClient() throws MqttException {
        MqttAsyncClient client =
//                new MqttAsyncClient(mqttProperties.getBrokerUrl(), generateClientId(),
//                        new MemoryPersistence());
                new MqttAsyncClient(mqttProperties.getBrokerUrl(), generateClientId(),
                        new MqttDefaultFilePersistence("/Users/huagang/software/intellij/customize /demo/mqtt/mqtt-data"));

        client.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("Received: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttToken token) {
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            }

            @Override
            public void authPacketArrived(int i, org.eclipse.paho.mqttv5.common.packet.MqttProperties mqttProperties) {

            }

            @Override
            public void disconnected(MqttDisconnectResponse disconnectResponse) {
            }

            @Override
            public void mqttErrorOccurred(MqttException exception) {
            }
        });
        return client;
    }

    private MqttClient createMqttClient() throws MqttException {
        return new MqttClient(mqttProperties.getBrokerUrl(), generateClientId(), new MqttDefaultFilePersistence("/Users/huagang/software/intellij/customize/demo/mqtt/mqtt-data"));
    }

    private String generateClientId() {
        return Optional.ofNullable(mqttProperties.getClientId())
                        .filter(StringUtils::hasText)
                        .orElseGet(() -> "CLIENT_" + System.currentTimeMillis());
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
