package org.mqtt.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Override
    public void handleMessage(String payload) {
        log.info("业务处理订阅信息：{}", payload);
    }
}
