package org.mqtt.test.controller;

import org.mqtt.config.MqttProperties;
import org.mqtt.publisher.MqttPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mqtt")
public class TestMqttController {
    private final MqttProperties mqttProperties;
    private final MqttPublisher mqttPublisher;
    public TestMqttController(MqttPublisher mqttPublisher, MqttProperties mqttProperties) {
        this.mqttPublisher = mqttPublisher;
        this.mqttProperties = mqttProperties;
    }
    @GetMapping(value = "/publish")
    @ResponseBody
    public String publish(String text) throws Exception{
        mqttPublisher.publishMessage(text, mqttProperties.getTopics().getTopic1());
        return "success";
    }
}
