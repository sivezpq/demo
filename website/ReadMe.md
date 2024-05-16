#### 1，mybatis的sql打印

springboot在默认情况下是不开启mybatis日志输出的，需要手动配置开启debug级别打印。
由于SpringBoot默认已经引入了spring-boot-starter-logging，所以只需配置mybatis属性即可

方案一：指定mapper

```
logging:
  level:
    com.**.web.mapper: debug
```

说明：“com.**.web.mapper”为mapper包路径

方案二： 打印所有

```
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

